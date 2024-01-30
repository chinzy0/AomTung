package com.money.moneyx.main.homeScreen

import android.app.Activity
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.money.moneyx.data.Preference
import com.money.moneyx.main.addListPage.addExpends.GetAllCategoryExpenses
import com.money.moneyx.main.addListPage.addExpends.GetAllTypeExpenses
import com.money.moneyx.main.addListPage.addIncome.GetAllCategoryincome
import com.money.moneyx.main.addListPage.addIncome.GetAllTypeIncome
import com.money.moneyx.main.addListPage.addIncome.ListScheduleAuto
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.ReportMonth
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class HomeViewModel : ViewModel() {

    val onClick = MutableLiveData<String>()
    val text = ObservableField("text001")
    val startTimestamp = ObservableField("")
    val endTimestamp = ObservableField("")
    var reportMonth: ReportMonth? = null
    fun getAllTypeIncome(clickCallback: ((GetAllTypeIncome) -> Unit)) {
        val jsonContent = JSONObject()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Type/GetAllTypeIncome")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse =
                        Gson().fromJson(responseBody.toString(), GetAllTypeIncome::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    fun getAllCategoryincome(clickCallback: ((GetAllCategoryincome) -> Unit)) {
        val jsonContent = JSONObject()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Category/GetAllCategoryincome")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse =
                        Gson().fromJson(responseBody.toString(), GetAllCategoryincome::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    fun getAllTypeExpenses(clickCallback: ((GetAllTypeExpenses) -> Unit)) {
        val jsonContent = JSONObject()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Type/GetAllTypeExpenses")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse =
                        Gson().fromJson(responseBody.toString(), GetAllTypeExpenses::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    fun getAllCategoryExpenses(clickCallback: ((GetAllCategoryExpenses) -> Unit)) {
        val jsonContent = JSONObject()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Category/GetAllCategoryExpenses")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse =
                        Gson().fromJson(responseBody.toString(), GetAllCategoryExpenses::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    fun listScheduleAuto(clickCallback: ((ListScheduleAuto) -> Unit)) {
        val jsonContent = JSONObject()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/ScheduleAutoSave/ListScheduleAuto  ")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse =
                        Gson().fromJson(responseBody.toString(), ListScheduleAuto::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    fun reportMonth(mContext: Activity, clickCallback: ((ReportMonth) -> Unit)) {
        val preferences = Preference.getInstance(mContext)
        val idMember = preferences.getInt("idmember", 0)
        val jsonContent = JSONObject()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(
                "http://zaserzafear.thddns.net:9973/api/Report/GetListReportMonth?datatype=income&idmember=${idMember}&start_timestamp=${
                    startTimestamp.get().toString()
                }&end_timestamp=${endTimestamp.get().toString()}"
            )
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse =
                        Gson().fromJson(responseBody.toString(), ReportMonth::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

}