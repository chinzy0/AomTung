package com.money.moneyx.main.homeScreen

import android.app.Activity
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.money.moneyx.data.Preference
import com.money.moneyx.main.addListPage.addExpends.GetAllCategoryExpenses
import com.money.moneyx.main.addListPage.addExpends.GetAllTypeExpenses
import com.money.moneyx.main.addListPage.addIncome.GetAllCategoryincome
import com.money.moneyx.main.addListPage.addIncome.GetAllTypeIncome
import com.money.moneyx.main.addListPage.addIncome.ListScheduleAuto
import com.money.moneyx.main.autoSave.GetListAuto
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
import java.util.Locale

class HomeViewModel : ViewModel() {

    val onClick = MutableLiveData<String>()
    val text = ObservableField("text001")
    val startTimestamp = ObservableField("")
    val endTimestamp = ObservableField("")
    var reportMonth: ReportMonth? = null


    fun convertDateTimeToUnixTimestamp(formattedMonth: String, formattedYear: String): Long {
        val dateTimeString = "01/$formattedMonth/$formattedYear 07:00:00"
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        try {
            val date = dateFormat.parse(dateTimeString)
            return date?.time!! / 1000L
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0L
    }

    fun convertEndDateTimeToUnixTimestamp(formattedMonthYear: String): Long {
        val dateTimeString = "$formattedMonthYear 23:59:59"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        try {
            val date = dateFormat.parse(dateTimeString)
            return date?.time!! / 1000L
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0L
    }

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

    fun reportMonth(
        mContext: Activity,
        startTimestamp: String?,
        endTimestamp: String?,
        clickCallback: ((ReportMonth) -> Unit)
    ) {
        val preferences = Preference.getInstance(mContext)
        val idMember = preferences.getInt("idmember", 0)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Report/GetListReportMonth?datatype=income&idmember=${idMember}&start_timestamp=${startTimestamp}&end_timestamp=${endTimestamp}")
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

    fun getListAuto(
        idmember: Int,
        clickCallback: ((GetListAuto) -> Unit)) {
        val jsonContent = JSONObject().apply {
            put("idmember", idmember)
        }.toString()

        val client = OkHttpClient()
        val requestBody = jsonContent.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Report/GetListAuto")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = Gson().fromJson(responseBody.toString(), GetListAuto::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

}