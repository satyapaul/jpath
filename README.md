# jpath
xpath for JSON

# Explanation
Each level in json is separated by "/". Here is how different types need to be represented

JSONObject : <name>[Object] or <name>[6] e.g. quoteSummary[Object]
JSONArray  : 
usage type-1 : <name>[Array] or <name>[4] e.g. result[Array]
usage type-2 : <name>[Array][0] or <name>[4][1] e.g. data[Array][1]
usage type-3 : <name>[Array][<name>=<value>] or <name>[4][<name>=<value>] e.g. data[Array][id=131272076894593_1420960724592382]


String     : <name>[String] or <name>[2] e.g. fmt[String]
Integer    : <name>[Integer] or <name>[1] e.g. raw[Integer]
Boolean    : <name>[Boolean] or <name>[3]
Double     : <name>[Double] or <name>[9] e.g. raw[Double]
Long       : <name>[Long] or <name>[0]


# Example-1 
Source JSON Data used in the example:

https://query2.finance.yahoo.com/v10/finance/quoteSummary/ABB.NS?modules=assetProfile,financialData,defaultKeyStatistics,incomeStatementHistory,cashflowStatementHistory,balanceSheetHistory

Here are few example jpaths:

/quoteSummary[Object]/result[Array][0]/defaultKeyStatistics[Object]/enterpriseValue[Object]/fmt[String]
quoteSummary[Object]/result[Array][0]/defaultKeyStatistics[Object]/forwardPE[Object]/raw[Double]
quoteSummary[Object]/result[Array][0]/defaultKeyStatistics[Object]/sharesOutstanding[Object]/raw[Integer]
/quoteSummary[6]/result[4][0]/defaultKeyStatistics[6]/sharesOutstanding[6]/raw[1]
quoteSummary[6]/result[Array][0]
quoteSummary[6]/result[Array]

# Example-2

http://www.fanffair.com/json?fetchsize=10&before=1490799314000&type=HOME_PAGE

/fbids[String]
/data[Array][1]/id[String]
/data[Array][1]/likes[Object]/summary[Object]/total_count[String]
/data[Array][1]/likes[Object]
/data[Array][id=131272076894593_1420960724592382]/likes[Object]/summary[Object]/total_count[String]

