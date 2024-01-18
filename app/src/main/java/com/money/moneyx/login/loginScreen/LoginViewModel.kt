package com.money.moneyx.login.loginScreen

import android.app.Activity
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class LoginViewModel : ViewModel() {

    val text = ObservableField("Login")
    val onClick = MutableLiveData<String>()
    var mDataModel: DataOTP? = null
    var status = String


    fun clickGetOtp() {
        onClick.value = "getOtp"
    }

    fun clickSubmitOtp() {
        onClick.value = "SubmitOtpButton"
    }

    fun clickSkip() {
        onClick.value = "Skip"
    }

    fun clickSubmitName() {
        onClick.value = "SubmitName"
    }

    fun clickForgotPin() {
        onClick.value = "ForgotPincode"
    }


//    fun generateOTPx() {
//        val json = JSONObject().put("phone", "0214536954").toString()
//        val client = OkHttpClient()
//        val requestBody = json.toRequestBody("application/json".toMediaType())
//        val request: Request = APICloud().GenerateOTP(requestBody)
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//                otp.postValue("")
//            }
//
//            @SuppressLint("NotifyDataSetChanged")
//            @Throws(IOException::class)
//            override fun onResponse(call: Call, response: Response) {
//                otp.postValue("000000")
//            }
//
//        })
//    }

    fun generateOTP(phone: String, clickCallback: ((DataOTP) -> Unit)) {
        val jsonContent =
            JSONObject().put("phone", phone).toString() // Replace this with your JSON string

        val client = OkHttpClient()
        val requestBody = jsonContent.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/OTP/GenerateOTP") // Replace with your API endpoint
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                // Handle the response on the background thread
                if (response.isSuccessful) {


                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody.toString())
                    val jsonObject = jsonResponse.getJSONObject("data")

                    val dataObject = jsonResponse.getJSONObject("data")
                    val sendTo = dataObject.getString("sendTO")
                    val refCode = dataObject.getString("refCode")
                    val codeOtp = dataObject.getString("codeotp")
                    val expired = dataObject.getInt("expired")
                    val validateLengthPhone = dataObject.getBoolean("validateLegthPhone")
                    val isDuplicate = dataObject.getBoolean("is_Duplicate")

                    var dataModel = DataOTP(codeOtp,expired,isDuplicate,refCode,sendTo,validateLengthPhone)

                    clickCallback.invoke(dataModel)

                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle the failure on the background thread
                e.printStackTrace()
            }
        })
    }




}