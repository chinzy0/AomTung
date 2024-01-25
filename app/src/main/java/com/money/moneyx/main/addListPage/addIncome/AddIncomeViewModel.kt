package com.money.moneyx.main.addListPage.addIncome

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.money.moneyx.R
import com.money.moneyx.login.loginScreen.ForgotPassword
import com.money.moneyx.login.loginScreen.ResultOTP
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

class AddIncomeViewModel : ViewModel() {

    val onClick = MutableLiveData<String>()
    val text = ObservableField("text001")
    var addIncomeModel = ArrayList<AddIncomeModel>()
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
        onClick.value = "incomeSaveClick"
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



}