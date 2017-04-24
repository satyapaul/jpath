package com.stockpeeker.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.stream.JsonReader;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * This program is a very simple implementation to emulate xpath APIs
 * for JSON Data in Java.
 * 
 * @author satyajitpaul
 * @version 1.0
 * @since   2017-04-24
 * @modified 2017-04-24
 * 
 */

/**
 * 
 *  MIT license
 *  ==========================================================================
 *  Copyright 2017 Satyajit Paul
 *  Permission is hereby granted, free of charge, to any person obtaining a 
 *  copy of this software and associated documentation files (the "Software"), 
 *  to deal in the Software without restriction, including without limitation 
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 *  and/or sell copies of the Software, and to permit persons to whom the 
 *  Software is furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in 
 *  all copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 *  IN THE SOFTWARE.
 *	==========================================================================
 */

public class SimpleJSONDataReader {

	/*
	 * SUPPRESS_JSON_EXCEPTION: The variable is introduced 
   * to deal with two different situations - 
	 * 
	 * design time : when a user is exploring the jpath
	 * for a particular JSON Content. During that time the 
	 * jpath may be wrong and may throw legitimate Exception
	 * when data is missing due to wrong path.
	 * 
	 * run time: Usually a user will use a jpath that has been 
	 * validated. In that case, an exception may be thrown as 
	 * data may be missing even when path is correct. In such 
	 * situations, one may like to supress the exceptions. You
	 * may set the value of SUPPRESS_JSON_EXCEPTION to true to 
	 * achieve this.
	 *      
	 */
	public boolean SUPPRESS_JSON_EXCEPTION = false;
	
	private static final String JSON_ARRAY_TYPE = "4";
	private static final String JSON_OBJECT_TYPE = "6";
	
	/**
	 * NOT a singleton class, this is just a helper method.
	 * Users are free to instantiate the class directly and
	 * use it. 
	 * @return
	 */
	public static SimpleJSONDataReader getInstance() {
		return new SimpleJSONDataReader();
	}

	public String getValue(String path, JSONObject jsonData) throws JSONException {
		path = path.startsWith("/") ? path.substring(1, path.length()) : path;
		String parts[] = path.split("/");
		Object value = getJSONValueSimple( parts, jsonData);
		return value.toString();
	}
	
	
	/**
	 * This recursive method is core method that traverses the JSON Data for return the requested jpath data. 
	 * This is a private method.
	 * @param parts
	 * @param jsonData
	 * @return
	 * @throws JSONException
	 */
	private Object getJSONValueSimple(String parts[], Object jsonData) throws JSONException {
		String pathValue = getPathValue(parts[0]);
		String dataType = getDataTypeSimple( parts[0], parts.length );
		if( "Object".equalsIgnoreCase(dataType) || JSON_OBJECT_TYPE.equals(dataType)) {
			JSONObject jsonDataObj = (JSONObject)jsonData;
			jsonData = jsonDataObj.getJSONObject(pathValue);
		} else if( "Array".equalsIgnoreCase(dataType) || JSON_ARRAY_TYPE.equals(dataType)) {
			JSONObject jsonDataObj = (JSONObject)jsonData;
			int dataLocInArr = getArrayLocation( parts[0], pathValue, jsonDataObj );
			if ( dataLocInArr > -1) {
				jsonData = jsonDataObj.getJSONArray(pathValue).get(dataLocInArr);
			}
			else {
				jsonData = jsonDataObj.getJSONArray(pathValue);
			}
		} else {
			String value = "";
			JSONObject jsonDataObj = (JSONObject)jsonData;
			
			if( SUPPRESS_JSON_EXCEPTION ) { 
				try {
					value = jsonDataObj.getString(pathValue);
				} catch(JSONException jsone) {
					System.err.println("User has used a wrong jpath or a non existing jpath.");
				}
			} else {
				value = jsonDataObj.getString(pathValue);
			}			
			return value;
		} 
		
		if( parts.length > 1) {
			return getJSONValueSimple( Arrays.copyOfRange(parts, 1, parts.length ), jsonData);
		} else {
			return jsonData;
		}
	}
	
	
	private String getPathValue(String pathlet) {
		String pathValue = pathlet.indexOf("[") != -1 ? pathlet.substring(0, pathlet.indexOf("[")) : pathlet;
		return pathValue;
	}
	

	
	private String getDataTypeSimple(String pathlet, int partsLength) {
		if( pathlet.indexOf("[") != -1 ) return "Array";
		if( partsLength == 1)  return "String";
		return "Object";
	}
	
	private int getArrayLocation(String pathlet, String pathValue, JSONObject jsonDataObj) throws JSONException {
		int location = -1 ;
		String loc = pathlet.substring(pathlet.lastIndexOf("[") + 1 , pathlet.lastIndexOf("]"));
		if( loc.indexOf("=") != -1 ) {
			String name = loc.split("=")[0];
			String value = loc.split("=")[1];
			JSONArray arr = jsonDataObj.getJSONArray(pathValue);
			for(int i = 0 ; i < arr.length(); i++) {
				String val = arr.getJSONObject(i).getString(name);
				if ( value.equals( val ) ) {
					return i;
				}
			}
			return location;
		}
		
		try {
			location = Integer.parseInt(loc);
		} catch(java.lang.NumberFormatException nfe) {
			//eat it
		}
		return location;
	}

	/**
	 * Method will be used to read the json data from a http url
	 * @param restUri provide the url for the json data source
	 */
	public JSONObject getContentFromHttpUri(String restUri) throws JSONException {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(
                        JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        WebResource webResource = client
                        .resource(restUri);
        ClientResponse response = webResource.accept("application/json")
                        .type("application/json").get(ClientResponse.class);
        if (response.getStatus() != 200) {
        	System.out.println("Problem reading "+restUri);
            throw new RuntimeException("Failed : HTTP error code : "
                                + response.getStatus());
        }
        String output = response.getEntity(String.class);
        return new JSONObject(output);
	}
	
	
	/**
	 * Method will be used to read the json data from local file system
	 * @param sourceLocation
	 * @return
	 * @throws JSONException 
	 */
	public JSONObject getFileContent(String sourceLocation) throws JSONException {
        StringBuilder strbldr = new StringBuilder();
        FileReader fr ;
		try {
			fr = new FileReader(sourceLocation);
	        BufferedReader br = new BufferedReader(fr); 
	        String s; 
	        while((s = br.readLine()) != null) { 
	        	strbldr.append(s);
	        	strbldr.append("\n"); 
	        } 
	        fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return new JSONObject(strbldr.toString().trim());
	}	
	
	
	/**
	 * main method has few examples of how you can invoke the methods 
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleJSONDataReader reader = SimpleJSONDataReader.getInstance();
		reader.runTests2();
		//runTests3();
	}
	


	private static void runTests3() {
		SimpleJSONDataReader jReader = SimpleJSONDataReader.getInstance();
		String sourceLocation = "/Users/satyajitpaul/Documents/workspace/StockPeeker/20170325/yahoo/stock-quotes/ABB.NS.json";
		String jsonDataUrl = "https://query2.finance.yahoo.com/v10/finance/quoteSummary/ABB.NS?modules=assetProfile,financialData,defaultKeyStatistics,incomeStatementHistory,cashflowStatementHistory,balanceSheetHistory";
		try {
			JSONObject jsonData = jReader.getFileContent(sourceLocation);
			//JSONObject jsonData = getContentFromHttpUri(jsonDataUrl);
			
			String jPath = "/quoteSummary/result[0]/defaultKeyStatistics/enterpriseValue/fmt";
			String value = jReader.getValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + value);
			
			jPath = "/quoteSummary/result[0]/defaultKeyStatistics/forwardPE/raw";
			String dValue = jReader.getValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + dValue);
			
			jPath = "quoteSummary/result[0]/defaultKeyStatistics/sharesOutstanding/raw";
			String intValue = jReader.getValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + intValue );
			
			
			jPath = "quoteSummary/result[0]/defaultKeyStatistics/sharesOutstanding/raw";
			System.out.println("jPath = " + jPath);
			intValue = jReader.getValue(jPath, jsonData);
			System.out.println("value = " + intValue);
			
			jPath = "quoteSummary/result[0]/defaultKeyStatistics/lastSplitDate/raw1";
			System.out.println("jPath = " + jPath);
			jReader.SUPPRESS_JSON_EXCEPTION = true;
			Object splitDateLong = jReader.getValue(jPath, jsonData);
			if( splitDateLong != null && !splitDateLong.equals("")) {
				Date splitDate = new Date(new Long(splitDateLong.toString()).longValue());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String _splitDate = formatter.format(splitDate);
				System.out.println("value = " + _splitDate);
			}
			
			jPath = "quoteSummary/result[0]";
			String jsonValue = jReader.getValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + new JSONObject(jsonValue).toString(4));
			
			jPath = "quoteSummary/result[Array]";
			String jsonArrValue = jReader.getValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + new JSONArray(jsonArrValue).toString(4));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	private void runTests2() {
		SimpleJSONDataReader jDataReader = SimpleJSONDataReader.getInstance();

		String jsonDataUrl = "http://www.fanffair.com/json?fetchsize=10&before=1490799314000&type=HOME_PAGE&noCache=1490801422724";
		try {
			JSONObject jsonData = jDataReader.getContentFromHttpUri(jsonDataUrl);
			String jPath = "/fbids";
			String value = jDataReader.getValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + value);
			
			jPath = "/data[4]/id";
			value = jDataReader.getValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + value);
			
			jPath = "/data[1]/likes/summary/total_count";
			System.out.println("jPath = " + jPath);
			value = jDataReader.getValue(jPath, jsonData);
			System.out.println("value = " + value);
			
			jPath = "/data[3]/likes";
			System.out.println("jPath = " + jPath);
			String jsonValue = jDataReader.getValue(jPath, jsonData);
			System.out.println("value = " + jsonValue);
			
			jPath = "/data[id=131272076894593_1420960724592382]/likes/summary/total_count";
			System.out.println("jPath = " + jPath);
			value = jDataReader.getValue(jPath, jsonData);
			System.out.println("value = " + value); // 142
			
			jPath = "/data";
			String jArrValue = jDataReader.getValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + jArrValue);
			
			jPath = "/data[3]";
			String jObjValue = jDataReader.getValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + jObjValue);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
