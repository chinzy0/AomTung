package com.money.moneyx.main.incomeExpends.summary

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.money.moneyx.data.Preference
import com.money.moneyx.login.loginScreen.ResetPhone
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.ReportMonth
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
import java.util.Date
import java.util.Locale

class SummaryViewModel : ViewModel(){
    val date = getCurrentDate()
    val week = getCurrentWeek()
    val monthAndYear = getCurrentMonthCalendar()
    val year = getCurrentMonthCalendar()

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("th","TH"))
        return dateFormat.format(calendar.time)
    }
    private fun getCurrentWeek(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("th","TH"))
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val startDateString = dateFormat.format(calendar.time)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        val endDateString = dateFormat.format(calendar.time)

        return Pair(startDateString, endDateString)
    }
    private fun getCurrentMonthCalendar(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("th","TH"))
        return dateFormat.format(calendar.time)
    }

    private fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy", Locale("th","TH"))
        return dateFormat.format(calendar.time)
    }
    fun reportSummary(datatype: String,idmember: Int,start_timestamp: Long,end_timestamp: Long,clickCallback: ((ReportSummary) -> Unit)) {
        val jsonContent = JSONObject()
            .put("datatype", datatype)
            .put("idmember", idmember)
            .put("start_timestamp", start_timestamp)
            .put("end_timestamp", end_timestamp).toString()
        val client = OkHttpClient()
        val requestBody = jsonContent.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Report/ReportSummary")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = Gson().fromJson(responseBody.toString(), ReportSummary::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}