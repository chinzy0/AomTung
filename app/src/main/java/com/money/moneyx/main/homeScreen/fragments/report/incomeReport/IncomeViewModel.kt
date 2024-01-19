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







}