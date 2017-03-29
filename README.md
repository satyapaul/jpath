# jpath
xpath for JSON

# Explanation
Source JSON Data used in the example:

https://query2.finance.yahoo.com/v10/finance/quoteSummary/ABB.NS?modules=assetProfile,financialData,defaultKeyStatistics,incomeStatementHistory,cashflowStatementHistory,balanceSheetHistory

Here are few example jpaths:

quoteSummary[Object]/result[Array][0]/defaultKeyStatistics[Object]/enterpriseValue[Object]/fmt[String]
quoteSummary[Object]/result[Array][0]/defaultKeyStatistics[Object]/forwardPE[Object]/raw[Double]
quoteSummary[Object]/result[Array][0]/defaultKeyStatistics[Object]/sharesOutstanding[Object]/raw[Integer]
quoteSummary[6]/result[4][0]/defaultKeyStatistics[6]/sharesOutstanding[6]/raw[1]
quoteSummary[6]/result[Array][0]
quoteSummary[6]/result[Array]
