package com.money.moneyx.main.profile.editTelNumber

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivityEditTelNumberBinding
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.login.loginScreen.LoginViewModel
import com.money.moneyx.login.otpScreen.OtpScreenActivity
import com.money.moneyx.main.homeScreen.HomeActivity

class EditTelNumberActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditTelNumberBinding
    private lateinit var viewModel: EditTelNumberViewModel
    private lateinit var viewModelLogin: LoginViewModel
    private val preferences = Preference.getInstance(this)
    private var phone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTelNumberBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[EditTelNumberViewModel::class.java]
        viewModelLogin = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.editTelNumberViewModel = viewModel
        phone = preferences.getString("phone", "")
        validateButton()
        setEventClick()
        loadingScreen(this)
        binding.imageView6.setOnClickListener { onBackPressed() }
        binding.textView5.text = phone
        setContentView(binding.root)
    }
    private fun validateButton(){
        binding.editTextPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val enteredText = s.toString()
                if (enteredText.length == 10) {
                    binding.buttonGetOTP.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button))
                    binding.buttonGetOTP.isEnabled = true
                } else {
                    binding.buttonGetOTP.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button_disable))
                    binding.buttonGetOTP.isEnabled = false
                }
            }
        })
    }
    private fun setEventClick() {
        viewModel.onClick.observe(this, Observer {
            when (it) {
                "changeTelButton" -> {
                        AVLoading.startAnimLoading()
                        viewModelLogin.generateOTP(phone = binding.editTextPhone.text.toString()){ model ->
                            AVLoading.stopAnimLoading()
                            if (model.success){
                                val intent = Intent(this, OtpScreenActivity::class.java)
                                intent.putExtra("mDataModel", model.data)
                                intent.putExtra("PHONE", binding.editTextPhone.text.toString())
                                intent.putExtra("refCode", viewModelLogin.mDataModel?.refCode)
                                intent.putExtra("EditTelNumber", "EditTelNumber")
                                startActivity(intent)
                            }
                        }
                }
            }
        })
    }

}