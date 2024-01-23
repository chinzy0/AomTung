package com.money.moneyx.login.loginScreen

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.databinding.ActivityLoginBinding
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.login.enterPincode.EnterPinCodeActivity
import com.money.moneyx.login.otpScreen.OtpScreenActivity

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

        loadingScreen(this)
        validateButton()
        setEventClick()



    }

    private fun validateButton(){
        binding.editTextPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val enteredText = s.toString()
                if (enteredText.length == 10) {
                    binding.buttonGetOTP.backgroundTintList =
                        ColorStateList.valueOf(getColor(R.color.button))
                    binding.buttonGetOTP.isEnabled = true
                } else {
                    binding.buttonGetOTP.backgroundTintList =
                        ColorStateList.valueOf(getColor(R.color.button_disable))
                    binding.buttonGetOTP.isEnabled = false
                }
            }
        })
    }


    private fun setEventClick() {

        viewModel.onClick.observe(this, Observer {
            when (it) {
                "getOtp" -> {
                    AVLoading.startAnimLoading()
                    viewModel.generateOTP(binding.editTextPhone.text.toString()) { model ->
                        AVLoading.stopAnimLoading()
                        if (model.success) {
                            if (model.data.is_Duplicate){
                                val intent = Intent(this, EnterPinCodeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                intent.putExtra("PHONE",binding.editTextPhone.text.toString())
                                startActivity(intent)
                            }else{
                            val intent = Intent(this, OtpScreenActivity::class.java)
                            intent.putExtra("mDataModel", model.data)
                            intent.putExtra("PHONE",binding.editTextPhone.text.toString())
                            intent.putExtra("refCode",viewModel.mDataModel?.refCode)
                            startActivity(intent)
                            }

                        }
                    }
                }
            }
        })
    }


}