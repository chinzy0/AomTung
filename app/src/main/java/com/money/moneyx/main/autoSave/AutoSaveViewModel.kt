package com.money.moneyx.main.autoSave

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.Report
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class AutoSaveViewModel: ViewModel() {
    var autoSaveModel = ArrayList<GetListAutoData>()
}