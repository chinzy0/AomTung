package com.money.moneyx.main.homeScreen.fragments.report.expendsReport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExpendsViewModel : ViewModel() {

    val onClickDialog = MutableLiveData<Pair<String, String>>()
    val onClick = MutableLiveData<String>()
    val currentMonthExpends = getCurrentMonth()
    val currentYearExpends = getCurrentYear()
    fun dropdownClick() {
        onClick.value = "showDropdown"
    }

    private fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMMM", Locale("th","TH"))
        return dateFormat.format(calendar.time)

    }

    private fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy")
        return dateFormat.format(calendar.time)

    }
}