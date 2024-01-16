package com.money.moneyx.main.addListPage

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportViewModel : ViewModel() {

    val onClick = MutableLiveData<String>()

    val text = ObservableField("text001")


    fun onBack() {
        onClick.value = "onBack"
    }
    fun btn() {
        onClick.value = "btn"
    }

}