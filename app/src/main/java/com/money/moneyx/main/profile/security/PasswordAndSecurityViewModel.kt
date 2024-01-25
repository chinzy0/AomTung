package com.money.moneyx.main.profile.security

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PasswordAndSecurityViewModel : ViewModel(){

    val onClick = MutableLiveData<String>()


    fun changePasswordClick(){
        onClick.value = "changePasswordClick"
    }
}