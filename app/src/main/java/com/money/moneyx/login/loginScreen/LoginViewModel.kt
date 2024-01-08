package com.money.moneyx.login.loginScreen

import android.annotation.SuppressLint
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.money.moneyx.api.APICloud
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

class LoginViewModel  : ViewModel() {

    val text = ObservableField("Login")
    val onClick = MutableLiveData<String>()
    val Otp = MutableLiveData<String>()

    fun clickGetOtp(){
        onClick.value = "getOtp"
    }
    fun clickSubmitOtp(){
        onClick.value = "SubmitOtpButton"
    }
    fun clickSkip(){
        onClick.value = "Skip"
    }
    fun clickSubmitName(){
        onClick.value = "SubmitName"
    }
    fun clickForgotPin(){
        onClick.value = "ForgotPincode"
    }



    fun getOtp(){
        val client = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()
            .add("user_id", "1")
            .add("user_id", "1")
            .build()
        val request: Request = APICloud().GetOtp(formBody)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Otp.postValue("")
            }

            @SuppressLint("NotifyDataSetChanged")
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                Otp.postValue("213446")
            }
        })
    }




}