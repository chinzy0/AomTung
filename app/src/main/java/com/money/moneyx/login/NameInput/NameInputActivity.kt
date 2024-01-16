package com.money.moneyx.login.NameInput

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.money.moneyx.R
import com.money.moneyx.databinding.ActivityNameInputBinding
import com.money.moneyx.login.loginScreen.LoginViewModel
import com.money.moneyx.login.otpScreen.OtpScreenActivity
import com.money.moneyx.main.homeScreen.HomeActivity

class NameInputActivity : AppCompatActivity() {
    private lateinit var  binding : ActivityNameInputBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNameInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setEventClick()
        binding.textView4.paintFlags =  Paint.UNDERLINE_TEXT_FLAG

        binding.editTextText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val enteredText = s.toString()
                if (enteredText.isNotEmpty()) {
                    binding.buttonSubmitName.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button))
                    binding.buttonSubmitName.isEnabled = true
                }else{
                    binding.buttonSubmitName.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button_disable))
                    binding.buttonSubmitName.isEnabled = false
                }
            }

        })
    }
    private fun setEventClick() {
        viewModel.onClick.observe(this, Observer {
            when (it) {
                "Skip" -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                "SubmitName" -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        })
    }
}