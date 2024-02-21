package com.money.moneyx.main.incomeExpends.summary

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReportSummary(
    val `data`: List<ReportSummaryData>,
    val message: String,
    val status: Int,
    val success: Boolean
): Parcelable

@Parcelize
data class ReportSummaryData(
    val titie_Number: String,
    val total_Expenses_Necessary: String,
    val total_Expenses_Unnecessary: String,
    val total_Income_Certain: String,
    val total_Income_Uncertain: String,
    val total_expenses: String,
    val total_income: String
): Parcelable

@Parcelize
data class ReportListSummary(
    val `data`: List<ReportListSummaryData>,
    val message: String,
    val status: Int,
    val success: Boolean
): Parcelable

@Parcelize
data class ReportListSummaryData(
    val dataList_Of: String,
    val number_type: String,
    val report_List_ALL: List<ReportALL>
): Parcelable

@Parcelize
data class ReportALL(
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
): Parcelable

@Parcelize
data class ReportListGraph(
    val `data`: ReportListGraphData,
    val message: String,
    val status: Int,
    val success: Boolean
): Parcelable

@Parcelize
data class ReportListGraphData(
    val dataOfList: List<DataOf>,
    val numberDayOf: String
): Parcelable

@Parcelize
data class DataOf(
    val number: String,
    val totalExpenses: String,
    val totalIncome: String
): Parcelable
