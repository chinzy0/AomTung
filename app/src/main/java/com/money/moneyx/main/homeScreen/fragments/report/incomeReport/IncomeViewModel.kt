package com.money.moneyx.main.homeScreen.fragments.report.incomeReport

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import java.text.SimpleDateFormat
import java.util.Calendar

class IncomeViewModel : ViewModel(){

    val onClickDialog = MutableLiveData<Pair<String, String>>()
    val onClick = MutableLiveData<String>()
    var incomeModel = ArrayList<IncomeReportModel>()





    fun dropdownClick() {
        onClick.value = "showDropdown"
    }

    fun adapter() {
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

    fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMMM")
        return dateFormat.format(calendar.time)

    }

    fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy")
        return dateFormat.format(calendar.time)

    }


}