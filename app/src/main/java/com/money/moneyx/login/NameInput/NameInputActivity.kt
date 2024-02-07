package com.money.moneyx.login.NameInput

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivityNameInputBinding
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.login.loginScreen.LoginViewModel
import com.money.moneyx.login.otpScreen.OtpScreenActivity
import com.money.moneyx.main.homeScreen.HomeActivity

class NameInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNameInputBinding
    private lateinit var viewModel: LoginViewModel
    private val preferences = Preference.getInstance(this)
    private var phoneNumber = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNameInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        password = intent.getStringExtra("PASSWORD").toString()
        phoneNumber = intent.getStringExtra("PHONE").toString()
        loadingScreen(this)
        validateNameButton()
        setEventClick()
        binding.textView4.paintFlags = Paint.UNDERLINE_TEXT_FLAG


    }

    private fun setEventClick() {
        viewModel.onClick.observe(this, Observer {
            when (it) {
                "Skip" -> {
                    AVLoading.startAnimLoading()
                    viewModel.createAccount(
                        phone = phoneNumber,
                        username = "",
                        password = password
                    ) { model ->
                        AVLoading.stopAnimLoading()
                        if (model.data.is_Success) {
                            AVLoading.startAnimLoading()
                            viewModel.memberLogin(phone = phoneNumber, password = password) { member ->
                                AVLoading.stopAnimLoading()
                                if (member.success) {
                                    val intent = Intent(this, HomeActivity::class.java)
                                    preferences.saveInt("idmember", member.data.idmember)
                                    preferences.saveString("phone", phoneNumber)
                                    preferences.saveString("username", member.data.username)
                                    preferences.saveString("image", member.data.image)
                                    preferences.saveString("pincode", password)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                } else {

                                }
                            }
                        }
                    }
                }

                "SubmitName" -> {
                    AVLoading.startAnimLoading()
                    viewModel.createAccount(phone = phoneNumber, username = binding.editTextText.text.toString(), password = password
                    ) { model ->
                        AVLoading.stopAnimLoading()
                        if (model.data.is_Success) {
                            viewModel.memberLogin(phone = phoneNumber, password = password) { member ->
                                if (member.success) {
                                    val intent = Intent(this, HomeActivity::class.java)
                                    preferences.saveInt("idmember", member.data.idmember)
                                    preferences.saveString("phone", phoneNumber)
                                    preferences.saveString("username", member.data.username)
                                    preferences.saveString("image", member.data.image)
                                    preferences.saveString("pincode", password)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                } else {

                                }
                            }
                        }
                    }
                }
            }
        })
    }



    private fun validateNameButton() {
        binding.editTextText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val enteredText = s.toString()
                if (enteredText.isNotEmpty() && enteredText.matches("[a-zA-Z0-9ก-๙ ]+".toRegex())) {
                    binding.buttonSubmitName.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button))
                    binding.buttonSubmitName.isEnabled = true
                } else {
                    binding.buttonSubmitName.backgroundTintList =
                        ColorStateList.valueOf(getColor(R.color.button_disable))
                    binding.buttonSubmitName.isEnabled = false
                }
            }

        })
    }
}