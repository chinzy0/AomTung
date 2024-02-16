package com.money.moneyx.main.profile.editProfile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivityDeleteAccountBinding
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.login.loginScreen.LoginActivity
import com.money.moneyx.main.homeScreen.HomeActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class DeleteAccountActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDeleteAccountBinding
    private val preferences = Preference.getInstance(this)
    private val positionClick = "ProfilePage"
    private var idMember = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteAccountBinding.inflate(layoutInflater)
        idMember = preferences.getInt("idmember", 0)
        loadingScreen(this)
        binding.imageView6.setOnClickListener { onBackPressed() }
        binding.buttonDeleteAcc.setOnClickListener { confirmDeleteDialog() }
        setContentView(binding.root)
    }
    private fun showDialogDeleteSuccess() {
        val dialog = Dialog(this)
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_acc_success)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val ok = dialog.findViewById<ConstraintLayout>(R.id.deleteAccountSuccessButton)
        val text = dialog.findViewById<TextView>(R.id.textViewDelDialog)
        text.setText("ลบบัญชีสำเร็จ")
        ok.setOnClickListener {
            dialog.dismiss()
            preferences.clear()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("positionClick",positionClick)
            startActivity(intent)
        }
    }
    private fun confirmDeleteDialog() {
        val dialog = Dialog(this)
        dialog.setCanceledOnTouchOutside(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_account_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
        val cancel = dialog.findViewById<ConstraintLayout>(R.id.cancle_delete_button)
        cancel.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("positionClick",positionClick)
            startActivity(intent)
        }
        val delete = dialog.findViewById<ConstraintLayout>(R.id.confirm_delete_button)
        delete.setOnClickListener {
            AVLoading.startAnimLoading()
            deleteAccount(idMember){ del ->
                AVLoading.stopAnimLoading()
                dialog.dismiss()
                if (del.success){
                    runOnUiThread{ showDialogDeleteSuccess() }
                }
            }
            }
        }
    }
private fun deleteAccount(
    idmember: Int,
    clickCallback: ((ResetUsernameAccount) -> Unit)) {
    val jsonContent = JSONObject()
        .put("idmember", idmember).toString()

    val client = OkHttpClient()
    val requestBody = jsonContent.toRequestBody("application/json".toMediaType())
    val request = Request.Builder()
        .url("http://zaserzafear.thddns.net:9973/api/Members/DeleteAccount?id=$idmember")
        .delete(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val apiResponse = Gson().fromJson(responseBody.toString(), ResetUsernameAccount::class.java)
                clickCallback.invoke(apiResponse)
            }
        }
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }
    })
}