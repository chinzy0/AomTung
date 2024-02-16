package com.money.moneyx.main.incomeExpends.summary


data class ReportSummary(
    val `data`: List<ReportSummaryData>,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class ReportSummaryData(
    val titie_Number: String,
    val total_Expenses_Necessary: String,
    val total_Expenses_Unnecessary: String,
    val total_Income_Certain: String,
    val total_Income_Uncertain: String,
    val total_expenses: String,
    val total_income: String
)