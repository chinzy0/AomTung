package com.money.moneyx.main.addListPage.addExpends

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.money.moneyx.R
import com.money.moneyx.main.addListPage.addIncome.AddIncomeModel
import com.money.moneyx.main.addListPage.addIncome.CreateListIncome
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

class AddExpendsViewModel : ViewModel() {


    val onClick = MutableLiveData<String>()
    val text = ObservableField("text001")
    val date = getCurrentDate()
    val time = getCurrentTime()

    fun expendsCalculateClick() {
        onClick.value = "expendsCalculateClick"
    }
    fun expendsDateClick() {
        onClick.value = "expendsDateClick"
    }
    fun expendsTimeClick() {
        onClick.value = "expendsTimeClick"
    }
    fun expendsTypeClick() {
        onClick.value = "expendsTypeClick"
    }
    fun expendsCategoryClick() {
        onClick.value = "expendsCategoryClick"
    }
    fun expendsNoteClick() {
        onClick.value = "expendsNoteClick"
    }
    fun expendsAutoSaveClick() {
        onClick.value = "expendsAutoSaveClick"
    }
    fun expendsSaveClick() {
        onClick.value = "expendsSaveClick"
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(calendar.time)
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm")
        return dateFormat.format(calendar.time)
    }

    fun createListExpenses(
        description: String,
        amount: Double,
        idmember: Int,
        dateCreated: Long,
        idcategory: Int,
        idtype: Int,
        auto_schedule: Int,
        clickCallback: ((CreateListExpenses) -> Unit)) {
        val jsonContent = JSONObject().apply {
            put("description", description)
            put("amount", amount)
            put("idmember", idmember)
            put("dateCreated", dateCreated)
            put("idcategory", idcategory)
            put("idtype", idtype)
            put("auto_schedule", auto_schedule)
        }.toString()


        val client = OkHttpClient()
        val requestBody = jsonContent.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://zaserzafear.thddns.net:9973/api/Expenses/CreateListExpenses")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = Gson().fromJson(responseBody.toString(), CreateListExpenses::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}