package com.money.moneyx.main.homeScreen.fragments.report.incomeReport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IncomeViewModel : ViewModel(){

    val onClickDialog = MutableLiveData<Pair<String, String>>()

    val onClick = MutableLiveData<String>()

    fun dropdownClick() {
        onClick.value = "showDropdown"
    }
}