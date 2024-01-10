package com.money.moneyx.main.profile

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel: ViewModel() {

    val onClick = MutableLiveData<String>()

    val text = ObservableField("text001")

    fun exit() {
        onClick.value = "exit"
    }

}