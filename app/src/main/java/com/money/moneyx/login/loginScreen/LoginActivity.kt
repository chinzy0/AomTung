package com.money.moneyx.login.loginScreen

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivityLoginBinding
import com.money.moneyx.login.otpScreen.OtpScreenActivity
import com.money.moneyx.viewModel.TestViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setEventClick()


        binding.editTextPhone.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                val enteredText = s.toString()
                if ( enteredText.length == 10){
                    binding.buttonGetOTP.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button))
                    binding.buttonGetOTP.isEnabled = true
                }else{
                    binding.buttonGetOTP.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button_disable))
                    binding.buttonGetOTP.isEnabled = false
                }
            }
        })


    }

    private fun setEventClick() {
        viewModel.onClick.observe(this, Observer {
            when (it) {
                "getOtp" -> {
                    val intent = Intent(this, OtpScreenActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }



}