package com.money.moneyx.login.loginScreen

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
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
    var forgotPasswordModel: OTPForgotPasswordData? = null
    var status = String
    var otpAuth = false
    var otpExpired = true


    fun clickGetOtp() {
        onClick.value = "getOtp"
    }

    fun clickSubmitOtp() {
        onClick.value = "SubmitOtpButton"
    }
    fun clickForgotPassword() {
        onClick.value = "ForgotPasswordButton"
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


    fun generateOTP(phone: String, clickCallback: ((ResultOTP) -> Unit)) {
        val jsonContent = JSONObject().put("phone", phone).toString()

        val client = OkHttpClient()
        val requestBody = jsonContent.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/OTP/GenerateOTP")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = parseJson(responseBody.toString())
                    clickCallback.invoke(apiResponse)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }


    private fun parseJson(jsonString: String): ResultOTP {
        return Gson().fromJson(jsonString, ResultOTP::class.java)
    }


     fun confirmOTP(phone: String,refCode: String,otpCode : String,clickCallback: ((ConfirmOTP) -> Unit)) {
        val jsonContent = JSONObject()
            .put("refCode", refCode)
            .put("phone", phone)
            .put("otpCode",otpCode).toString()


        val client = OkHttpClient()
        val requestBody = jsonContent.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/OTP/ConfirmOTP")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = Gson().fromJson(responseBody.toString(), ConfirmOTP::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    fun createAccount(phone: String,username: String,password : String,clickCallback: ((CreateAccount) -> Unit)) {
        val jsonContent = JSONObject()
            .put("phone", phone)
            .put("username", username)
            .put("password",password).toString()


        val client = OkHttpClient()
        val requestBody = jsonContent.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Authentication/CreateAccount")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = Gson().fromJson(responseBody.toString(), CreateAccount::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }


    fun memberLogin(phone: String,password : String,clickCallback: ((MemberLogin) -> Unit)) {
        val jsonContent = JSONObject()
            .put("phone", phone)
            .put("password",password).toString()


        val client = OkHttpClient()
        val requestBody = jsonContent.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Authentication/MemberLogin")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = Gson().fromJson(responseBody.toString(), MemberLogin::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    fun otpForgotPassword(phone: String,clickCallback: ((OTPForgotPassword) -> Unit)) {
        val jsonContent = JSONObject()
            .put("phone", phone).toString()


        val client = OkHttpClient()
        val requestBody = jsonContent.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/OTP/OTPForgotPassword")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = Gson().fromJson(responseBody.toString(), OTPForgotPassword::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }


    fun forgotPassword(phone: String,newPassword: String,clickCallback: ((ForgotPassword) -> Unit)) {
        val jsonContent = JSONObject()
            .put("phone", phone)
            .put("newPassword", newPassword).toString()


        val client = OkHttpClient()
        val requestBody = jsonContent.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Members/ForgotPassword")
            .patch(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = Gson().fromJson(responseBody.toString(), ForgotPassword::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
    fun resetPhone(phone: String,idmember: Int,clickCallback: ((ResetPhone) -> Unit)) {
        val jsonContent = JSONObject()
            .put("phone", phone)
            .put("idmember", idmember).toString()
        val client = OkHttpClient()
        val requestBody = jsonContent.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Members/ResetPhone")
            .patch(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = Gson().fromJson(responseBody.toString(), ResetPhone::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }


}