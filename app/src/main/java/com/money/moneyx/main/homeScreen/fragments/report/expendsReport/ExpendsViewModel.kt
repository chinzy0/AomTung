package com.money.moneyx.main.homeScreen.fragments.report.expendsReport

import android.app.Activity
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.money.moneyx.data.Preference
import com.money.moneyx.main.addListPage.addExpends.UpdateExpenses
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.Report
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.ReportMonth
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExpendsViewModel : ViewModel() {

    val onClickDialog = MutableLiveData<Triple<String, String,String>>()
    val onClick = MutableLiveData<String>()
    var expendsModel = ArrayList<Report>()
    val currentMonthExpends = getCurrentMonth()
    val currentYearExpends = getCurrentYear()
    var startTimestampEx = ObservableField("")
    val endTimestampEx = ObservableField("")
    fun dropdownClick() {
        onClick.value = "showDropdown"
    }

    private fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMMM", Locale("th","TH"))
        return dateFormat.format(calendar.time)

    }

    private fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy")
        return dateFormat.format(calendar.time)

    }


    fun  reportMonth(mContext: Activity, clickCallback: ((ReportMonth) -> Unit)) {
        val preferences = Preference.getInstance(mContext)
        val idMember = preferences.getInt("idmember", 0)
        Log.i("idmember",idMember.toString())
        val jsonContent = JSONObject()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Report/GetListReportMonth?datatype=income&idmember=${idMember}&start_timestamp=${startTimestampEx.get().toString()}&end_timestamp=${endTimestampEx.get().toString()}")
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = Gson().fromJson(responseBody.toString(), ReportMonth::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }


}