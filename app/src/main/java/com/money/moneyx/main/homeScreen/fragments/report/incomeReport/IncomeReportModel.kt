package com.money.moneyx.main.homeScreen.fragments.report.incomeReport

data class IncomeReportModel(
    val incomeType : String,
    val category: String,
    val money: String,
    val date: String,
)
data class ReportMonth(
    val `data`: List<ReportMonthData>,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class ReportMonthData(
    val report_month_list_Expenses: List<ReportMonthExpenses>,
    val report_month_list_income: List<Any>,
    val totalBalance: String,
    val totalExpenses: String,
    val totalIncome: String
)

data class ReportMonthExpenses(
    val month: String,
    val report_Necessary_List: List<Any>,
    val report_Unnecessary_List: List<Any>,
    val total_Expenses_Necessary: String,
    val total_Expenses_Unnecessary: String
)