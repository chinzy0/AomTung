package com.money.moneyx.main.homeScreen.fragments.report.incomeReport

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.money.moneyx.data.Preference
import com.money.moneyx.main.addListPage.addExpends.GetAllCategoryExpenses
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class IncomeViewModel : ViewModel() {

    val onClickDialog = MutableLiveData<Pair<String, String>>()
    val onClick = MutableLiveData<String>()
    var incomeModel = ArrayList<IncomeReportModel>()
    val currentMonthIncome = getCurrentMonth()
    val currentYearIncome = getCurrentYear()



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
        val dateFormat = SimpleDateFormat("yyyy",Locale("en","EN"))
        return dateFormat.format(calendar.time)
    }

    fun reportMonth(mContext: Activity,clickCallback: ((ReportMonth) -> Unit)) {
        val preferences = Preference.getInstance(mContext)
        val idMember = preferences.getInt("idmember", 0)
        val jsonContent = JSONObject()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("{{base_url}}/api/Report/GetListReportMonth?datatype=income&idmember={$idMember}&start_timestamp={}&end_timestamp={}")
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
    private fun convertDateTimeToUnixTimestamp(formattedDate: String, formattedTime: String): Long {
        val dateTimeString = "$formattedDate $formattedTime:00"

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())


        try {
            val date = dateFormat.parse(dateTimeString)
            return date?.time!! / 1000L
        } catch (e: Exception) {

            e.printStackTrace()
        }

        return 0L
    }


}