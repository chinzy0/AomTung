package com.money.moneyx.main.homeScreen.fragments.report.incomeReport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class IncomeViewModel : ViewModel() {

    val onClickDialog = MutableLiveData<Pair<String, String>>()
    val onClick = MutableLiveData<String>()
    var incomeModel = ArrayList<IncomeReportModel>()
    val currentMonthIncome = getCurrentMonth()
    val currentYearIncome = getCurrentYear()


    fun dropdownClick() {
        onClick.value = "showDropdown"
    }

    fun addIncomeModel() {
        incomeModel.add(
            IncomeReportModel(
                "รายรับแน่นอน",
                "เงินเดือน",
                "+1,000.00",
                "22/12/2023 11:17"
            )
        )
        incomeModel.add(
            IncomeReportModel(
                "รายรับไม่แน่นอน",
                "ของขวัญ",
                "+500.00",
                "22/12/2023 11:17"
            )
        )
        incomeModel.add(
            IncomeReportModel(
                "รายรับไม่แน่นอน",
                "ของขวัญ",
                "+500.00",
                "22/12/2023 11:17"
            )
        )
    }

    private fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMMM", Locale("th","TH"))
        return dateFormat.format(calendar.time)

    }

    private fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy",Locale("en","EN"))
        return dateFormat.format(calendar.time)

    }


}