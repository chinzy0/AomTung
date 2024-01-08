package com.money.moneyx.viewModel

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

class TestViewModel : ViewModel() {

    val onClick = MutableLiveData<String>()

    val text = ObservableField("text001")

    fun click() {
        onClick.value = "click"
    }

    fun click2() {
        onClick.value = "click2"
    }

    val dataResult = MutableLiveData<String>()

    fun callService() {
        val client = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()
            .add("param", "value")
            .build()
        val request: Request = APICloud().GetService(formBody)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                dataResult.postValue("fail")
            }

            @SuppressLint("NotifyDataSetChanged")
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                dataResult.postValue("success")
            }
        })
    }

}