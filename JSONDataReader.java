package com.satyajitpaul.jpath;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
 * @since   2017-03-29 
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

public class JSONDataReader {


	/**
	 * This method is for reading the leaf level value of DataType 'String'
	 * @param path The parameter provides the input 'jpath'
	 * @param jsonData Actual JSONObject. The jpath above will traverse this data to get the requested data
	 * @return
	 * @throws JSONException
	 */
	public static String getStringValue(String path, JSONObject jsonData) throws JSONException {
		String parts[] = path.split("/");
		Object value = getJSONValue( parts, jsonData);
		return (String)value;
	}
	
	/**
	 * This method is for reading the leaf level value of DataType 'Double'
	 * @param path The parameter provides the input 'jpath'
	 * @param jsonData Actual JSONObject. The jpath above will traverse this data to get the requested data
	 * @return
	 * @throws JSONException
	 */
	public static Double getDoubleValue(String path, JSONObject jsonData) throws JSONException {
		String parts[] = path.split("/");
		Object value = getJSONValue( parts, jsonData);
		return (Double)value;
	}
	
	/**
	 * This method is for reading the leaf level value of DataType 'Integer'
	 * @param path The parameter provides the input 'jpath'
	 * @param jsonData Actual JSONObject. The jpath above will traverse this data to get the requested data
	 * @return
	 * @throws JSONException
	 */	
	public static Integer getIntegerValue(String path, JSONObject jsonData) throws JSONException {
		String parts[] = path.split("/");
		Object value = getJSONValue( parts, jsonData);
		return (Integer)value;
	}
	
	/**
	 * This method is for reading the non-leaf level a child JSONObject data 
	 * @param path The parameter provides the input 'jpath'
	 * @param jsonData Actual JSONObject. The jpath above will traverse this data to get the requested data
	 * @return
	 * @throws JSONException
	 */	
	public static JSONObject getJSONObjectValue(String path, JSONObject jsonData) throws JSONException {
		String parts[] = path.split("/");
		Object value = getJSONValue( parts, jsonData);
		return (JSONObject)value;
	}
	
	/**
	 * This method is for reading the non-leaf level a child JSONArray data 
	 * @param path The parameter provides the input 'jpath'
	 * @param jsonData Actual JSONObject. The jpath above will traverse this data to get the requested data
	 * @return
	 * @throws JSONException
	 */		
	public static JSONArray getJSONArrayValue(String path, JSONObject jsonData) throws JSONException {
		String parts[] = path.split("/");
		Object value = getJSONValue( parts, jsonData);
		return (JSONArray)value;
	}
	
	/**
	 * This recursive method is core method that traverses the JSON Data for return the requested jpath data. 
	 * This is a private method.
	 * @param parts
	 * @param jsonData
	 * @return
	 * @throws JSONException
	 */
	private static Object getJSONValue(String parts[], Object jsonData) throws JSONException {
		String pathValue = getPathValue(parts[0]);
		String dataType = getDataType( parts[0] );
		
		if( "Object".equalsIgnoreCase(dataType) || "6".equals(dataType)) {
			JSONObject jsonDataObj = (JSONObject)jsonData;
			jsonData = jsonDataObj.getJSONObject(pathValue);
		} else if( "Array".equalsIgnoreCase(dataType) || "4".equals(dataType)) {
			int dataLocInArr = getArrayLocation( parts[0] );
			JSONObject jsonDataObj = (JSONObject)jsonData;
			if ( dataLocInArr > -1) {
				jsonData = jsonDataObj.getJSONArray(pathValue).get(dataLocInArr);
			}
			else {
				jsonData = jsonDataObj.getJSONArray(pathValue);
			}
		} else if( "String".equalsIgnoreCase(dataType) || "2".equals(dataType)) {
			JSONObject jsonDataObj = (JSONObject)jsonData;
			return jsonDataObj.getString(pathValue);
		} else if( "Integer".equalsIgnoreCase(dataType) || "1".equals(dataType)) {
			JSONObject jsonDataObj = (JSONObject)jsonData;
			return jsonDataObj.getInt(pathValue);
		} else if( "Double".equalsIgnoreCase(dataType) || "9".equals(dataType)) {
			JSONObject jsonDataObj = (JSONObject)jsonData;
			return jsonDataObj.getDouble(pathValue);
		} else if( "Boolean".equalsIgnoreCase(dataType) || "3".equals(dataType)) {
			JSONObject jsonDataObj = (JSONObject)jsonData;
			return jsonDataObj.getBoolean(pathValue);
		} else if( "Long".equalsIgnoreCase(dataType) || "0".equals(dataType)) {
			JSONObject jsonDataObj = (JSONObject)jsonData;
			return jsonDataObj.getBoolean(pathValue);
		}
		
		if( parts.length > 1) {
			return getJSONValue( Arrays.copyOfRange(parts, 1, parts.length ), jsonData);
		} else {
			return jsonData;
		}
	}
	
	private static String getPathValue(String pathlet) {
		String pathValue = pathlet.substring(0, pathlet.indexOf("["));
		return pathValue;
	}
	
	private static String getDataType(String pathlet) {
		String dataType = pathlet.substring(pathlet.indexOf("[") + 1 , pathlet.indexOf("]"));
		return dataType;
	}
	
	private static int getArrayLocation(String pathlet) {
		String loc = pathlet.substring(pathlet.lastIndexOf("[") + 1 , pathlet.lastIndexOf("]"));
		int location = -1 ;
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
	public static JSONObject getContentFromHttpUri(String restUri) throws JSONException {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(
                        JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        WebResource webResource = client
                        .resource(restUri);
        //System.out.println(jsonArray);
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
	public static JSONObject getFileContent(String sourceLocation) throws JSONException {
        StringBuilder strbldr = new StringBuilder();
        FileReader fr ;
		try {
			fr = new FileReader(sourceLocation);
	        BufferedReader br = new BufferedReader(fr); 
	        String s; 
	        while((s = br.readLine()) != null) { 
	        	//System.out.println("s >> " + s);
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
        //System.out.println("strbldr >> "+ strbldr.toString());
        return new JSONObject(strbldr.toString().trim());
	}	
	
	/**
	 * main method has few examples of how you can invoke the APIs 
	 * @param args
	 */
	public static void main(String[] args) {
		//String sourceLocation = "/Users/xxxxxxx/Documents/yahoo/stock-quotes/ABB.NS.json";
		//JSONObject jsonData = getFileContent(sourceLocation);
		
		String jsonDataUrl = "https://query2.finance.yahoo.com/v10/finance/quoteSummary/ABB.NS?modules=assetProfile,financialData,defaultKeyStatistics,incomeStatementHistory,cashflowStatementHistory,balanceSheetHistory";
		
		
		try {
			JSONObject jsonData = getContentFromHttpUri(jsonDataUrl);
			

			String jPath = "quoteSummary[Object]/result[Array][0]/defaultKeyStatistics[Object]/enterpriseValue[Object]/fmt[String]";
			String value = getStringValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + value);
			
			jPath = "quoteSummary[Object]/result[Array][0]/defaultKeyStatistics[Object]/forwardPE[Object]/raw[Double]";
			Double dValue = getDoubleValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + dValue.doubleValue());
			
			jPath = "quoteSummary[Object]/result[Array][0]/defaultKeyStatistics[Object]/sharesOutstanding[Object]/raw[Integer]";
			Integer intValue = getIntegerValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + intValue.intValue());
			
			
			jPath = "quoteSummary[6]/result[4][0]/defaultKeyStatistics[6]/sharesOutstanding[6]/raw[1]";
			intValue = getIntegerValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + intValue.intValue());
			
			jPath = "quoteSummary[6]/result[Array][0]";
			JSONObject jsonValue = getJSONObjectValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + jsonValue);
			
			jPath = "quoteSummary[6]/result[Array]";
			JSONArray jsonArrValue = getJSONArrayValue(jPath, jsonData);
			System.out.println("jPath = " + jPath);
			System.out.println("value = " + jsonArrValue);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}		
}
