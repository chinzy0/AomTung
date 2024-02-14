package com.money.moneyx.main.profile.editTelNumber

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.money.moneyx.login.loginScreen.ResultOTP
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class EditTelNumberViewModel: ViewModel() {

    val onClick = MutableLiveData<String>()

    fun changeTelButton() {
        onClick.value = "changeTelButton"
    }



}
