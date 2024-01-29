package com.money.moneyx.main.addListPage.addIncome

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.money.moneyx.login.loginScreen.CreateAccount
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddIncomeViewModel : ViewModel() {

    val onClick = MutableLiveData<String>()
    val text = ObservableField("text001")
    val date = getCurrentDate()
    val time = getCurrentTime()



    fun calculateClick() {
        onClick.value = "calculateClick"
    }
    fun incomeDateClick() {
        onClick.value = "incomeDateClick"
    }
    fun incomeTimeClick() {
        onClick.value = "incomeTimeClick"
    }
    fun incomeTypeClick() {
        onClick.value = "incomeTypeClick"
    }
    fun incomeCategoryClick() {
        onClick.value = "incomeCategoryClick"
    }
    fun incomeNoteClick() {
        onClick.value = "incomeNoteClick"
    }
    fun incomeAutoSaveClick() {
        onClick.value = "incomeAutoSaveClick"
    }
    fun incomeSaveClick() {
        onClick.value = "incomeSaveClickButton"
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(calendar.time)
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm")
        val timeFormat = DateFormat.getTimeInstance(DateFormat.LONG, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun createListIncome(
        description: String,
        amount: Double,
        idmember: Int,
        dateCreated: Long,
        idcategory: Int,
        idtype: Int,
        auto_schedule: Int,
        clickCallback: ((CreateListIncome) -> Unit)) {
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
            .url("http://zaserzafear.thddns.net:9973/api/Incomes/CreateListIncome")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = Gson().fromJson(responseBody.toString(), CreateListIncome::class.java)
                    clickCallback.invoke(apiResponse)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }


}