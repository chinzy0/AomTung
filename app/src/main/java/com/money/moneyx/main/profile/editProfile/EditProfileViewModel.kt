package com.money.moneyx.main.profile.editProfile

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.UpdateIncome
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class EditProfileViewModel: ViewModel() {

    val onClick = MutableLiveData<String>()


    fun onBack() {
        onClick.value = "onBack"
    }
    fun saveNameChange() {
        onClick.value = "saveNameChange"
    }
    fun deleteAccount() {
        onClick.value = "deleteAccount"
    }

    fun resetUsernameAccount(
        idmember: Int,
        name: String,
        clickCallback: ((ResetUsernameAccount) -> Unit)) {
        val jsonContent = JSONObject()
            .put("idmember", idmember)
            .put("name", name).toString()

        val client = OkHttpClient()
        val requestBody = jsonContent.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Members/ResetUsernameAccount")
            .patch(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = Gson().fromJson(responseBody.toString(), ResetUsernameAccount::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }



}