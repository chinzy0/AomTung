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
data class ReportListSummary(
    val `data`: List<ReportListSummaryData>,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class ReportListSummaryData(
    val dataList_Of: String,
    val number_type: String,
    val report_Expenses_Necessary: List<ReportExpensesNecessary>,
    val report_Expenses_Unnecessary: List<ReportExpensesUnnecessary>,
    val report_Income_Cartain: List<ReportIncomeCartain>,
    val report_Income_UnCartain: List<ReportIncomeUnCartain>
)

data class ReportExpensesNecessary(
    val amount: String,
    val category_id: Int,
    val category_name: String,
    val currency_symbol: String,
    val description: String,
    val save_auto_id: Int,
    val save_auto_name: String,
    val symbol_math: String,
    val timestamp: Int,
    val transaction_id: Int,
    val type_data: String,
    val type_id: Int,
    val type_name: String
)

data class ReportExpensesUnnecessary(
    val amount: String,
    val category_id: Int,
    val category_name: String,
    val currency_symbol: String,
    val description: String,
    val save_auto_id: Int,
    val save_auto_name: String,
    val symbol_math: String,
    val timestamp: Int,
    val transaction_id: Int,
    val type_data: String,
    val type_id: Int,
    val type_name: String
)

data class ReportIncomeCartain(
    val amount: String,
    val category_id: Int,
    val category_name: String,
    val currency_symbol: String,
    val description: String,
    val save_auto_id: Int,
    val save_auto_name: String,
    val symbol_math: String,
    val timestamp: Int,
    val transaction_id: Int,
    val type_data: String,
    val type_id: Int,
    val type_name: String
)

data class ReportIncomeUnCartain(
    val amount: String,
    val category_id: Int,
    val category_name: String,
    val currency_symbol: String,
    val description: String,
    val save_auto_id: Int,
    val save_auto_name: String,
    val symbol_math: String,
    val timestamp: Int,
    val transaction_id: Int,
    val type_data: String,
    val type_id: Int,
    val type_name: String
)