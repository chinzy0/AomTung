package com.money.moneyx.main.homeScreen.fragments.report.incomeReport

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class IncomeReportModel(
    val incomeType : String,
    val category: String,
    val money: String,
    val date: String,
)

@Parcelize
data class ReportMonth(
    val `data`: List<ReportMonthData>,
    val message: String,
    val status: Int,
    val success: Boolean
): Parcelable

@Parcelize

data class ReportMonthData(
    val report_month_list_Expenses: List<ReportMonthExpenses>,
    val report_month_list_income: List<ReportMonthIncome>,
    val totalBalance: String,
    val totalExpenses: String,
    val totalIncome: String
): Parcelable

@Parcelize

data class ReportMonthExpenses(
    val month: String,
    val report_List: List<Report>,
    val total_Expenses_Necessary: String,
    val total_Expenses_Unnecessary: String
): Parcelable

@Parcelize

data class ReportMonthIncome(
    val month: String,
    val report_List: List<Report>,
    val total_Income_Certain: String,
    val total_Income_Uncertain: String
): Parcelable


@Parcelize
data class Report(
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
data class UpdateIncome(
    val `data`: UpdateIncomeData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class UpdateIncomeData(
    val amount: String,
    val category_id: Int,
    val category_name: String,
    val createdateTime: Int,
    val description: String,
    val income_id: Int,
    val is_Updated: Boolean,
    val save_auto_id: Int,
    val save_auto_name: String,
    val type_id: Int,
    val type_name: String
)