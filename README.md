# jpath

It's a quick solution to read JSON Data. It works in a similar manner xpath works for xml data. Take a look at this single class library called 'jpath' that emulates xpath for JSON data.  The library is available in both Java and Javascript. Here is the demo url - http://www.jpath.org.s3-website.us-east-2.amazonaws.com/ Demo uses javascript library for demonstration purpose.

# Live Demo

Check out - http://www.jpath.org.s3-website.us-east-2.amazonaws.com/

# Third Party Library Dependencies 

You need following jars in your classpath in order to execute the code.

jettison-1.3.7.jar

jersey-client-1.91.1.jar

jersey-json-1.91.1.jar


# How It Works

Each level in json is separated by "/". Here is how different types need to be represented

JSONObject : 

{name} or {name} e.g. quoteSummary

JSONArray  : 

usage type-1 : {name} or {name} e.g. result

usage type-2 : {name}[0] or {name}[1] e.g. data[1]

usage type-3 : 

The value is expected to be unique. If the value has duplicate values, it picks up the one that comes first.

{name}[{name}={value}] e.g. data[id=131272076894593_1420960724592382]

# Example-1 
Source JSON Data used in the example:

https://query2.finance.yahoo.com/v10/finance/quoteSummary/ABB.NS?modules=assetProfile,financialData,defaultKeyStatistics,incomeStatementHistory,cashflowStatementHistory,balanceSheetHistory

Here are few example jpaths:

/quoteSummary/result[0]/defaultKeyStatistics/enterpriseValue/fmt

/quoteSummary/result[0]/defaultKeyStatistics/forwardPE/raw

/quoteSummary/result[0]/defaultKeyStatistics/sharesOutstanding/raw

/quoteSummary/result[0]

/quoteSummary/result

# Example-2

http://www.fanffair.com/json?fetchsize=10&before=1490799314000&type=HOME_PAGE

/fbids

/data[1]/id

/data[1]/likes/summary/total_count

/data[1]/likes

/data[id=131272076894593_1420960724592382]/likes/summary/total_count

# Output of the above Test cases

https://github.com/satyapaul/jpath/blob/master/output.txt
