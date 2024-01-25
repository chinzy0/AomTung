package com.money.moneyx.login.forgotPassword

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.databinding.ActivityForgotPasswordBinding
import com.money.moneyx.function.dialogOtp
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.function.wrongOtpDialog
import com.money.moneyx.login.createPincode.CreatePinActivity
import com.money.moneyx.login.loginScreen.LoginViewModel
import com.money.moneyx.login.loginScreen.OTPForgotPasswordData
import java.util.Locale

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftInMillis: Long = 60 * 1000
    private var otp = ""
    private var phoneNumber = ""
    private var refCode = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        viewModel.forgotPasswordModel = intent.getParcelableExtra<OTPForgotPasswordData>("forgotPasswordDataModel")
        phoneNumber = intent.getStringExtra("PHONE").toString()
        otp = viewModel.forgotPasswordModel!!.codeotp
        refCode = viewModel.forgotPasswordModel!!.refCode

        loadingScreen(this)
        buttonActivated()
        setOTPText()
        setEventClick()

        binding.appbarOtp.BackPage.setOnClickListener {
            this.onBackPressed()
        }


    }

    private fun buttonActivated() {
        binding.OtpPinview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val textLength = s?.length ?: 0
                if (textLength == 6) {
                    if (s.toString() == otp) {
                        binding.buttonSubmit.isEnabled = true
                        binding.buttonSubmit.backgroundTintList =
                            ColorStateList.valueOf(getColor(R.color.button))
                        viewModel.otpAuth = true
                    } else {
                        binding.buttonSubmit.isEnabled = true
                        binding.buttonSubmit.backgroundTintList =
                            ColorStateList.valueOf(getColor(R.color.button))
                        viewModel.otpAuth = false
                    }
                } else {
                    binding.buttonSubmit.isEnabled = false
                    binding.buttonSubmit.backgroundTintList =
                        ColorStateList.valueOf(getColor(R.color.button_disable))
                }
            }
        })
    }

    private fun setOTPText() {
        viewModel.forgotPasswordModel?.let { data ->
            if (data.codeotp.isNullOrEmpty()) {
                Toast.makeText(this, "พบข้อผิดพลาด", Toast.LENGTH_LONG).show()
            } else {
                dialogOtp(this, data.codeotp) {
                    binding.OtpPinview.setText(it)
                    startCountDownTimer()
                    binding.reOtp.isEnabled = false
                }
            }
        }
    }

    private fun setEventClick() {
        viewModel.onClick.observe(this, Observer {
            when (it) {
                "ForgotPasswordButton" -> {
                    if (viewModel.otpAuth && viewModel.otpExpired)
                    { viewModel.confirmOTP(phone = phoneNumber, refCode = refCode, otpCode = otp) { model ->
                        if (model.data.is_Success) {
                            runOnUiThread {
                                countDownTimer.cancel()
                                viewModel.otpExpired = false
                                binding.textView8.text = "ส่งรหัส OTP อีกครั้ง"
                                binding.textView8.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                                binding.imageView.visibility = View.VISIBLE
                                binding.textView10.text = ""
                                binding.reOtp.isEnabled = true
                                binding.OtpPinview.setText("")
                            }
                            val intent = Intent(this, ResetPinCodeActivity::class.java)
                            intent.putExtra("mDataModel", model.data)
                            intent.putExtra("PHONE", phoneNumber)
                            startActivity(intent)
                        } else {
                            wrongOtpDialog(this, model.data.message)
                        }
                    }
                    } else {
                        wrongOtpDialog(this, resources.getString(R.string.otp_dint_match))
                    }
                }
            }
        })


        binding.reOtp.setOnClickListener {
            AVLoading.startAnimLoading()
            viewModel.otpForgotPassword(phoneNumber) { model ->
                AVLoading.stopAnimLoading()
                runOnUiThread {
                    dialogOtp(this, model.data.codeotp) {
                        otp = it
                        refCode = model.data.refCode
                        binding.OtpPinview.setText(it)
                        timeLeftInMillis = 60 * 1000
                        startCountDownTimer()
                        viewModel.otpExpired = true

                    }
                }
            }
            binding.textView8.text = "กรุณากรอกภายใน"
            binding.textView8.paintFlags = 0
            binding.imageView.visibility = View.GONE
            binding.reOtp.isEnabled = false
        }

    }
    private fun startCountDownTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerUI()
            }
            override fun onFinish() {
                viewModel.otpExpired = false
                binding.textView8.text = "ส่งรหัส OTP อีกครั้ง"
                binding.textView8.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                binding.imageView.visibility = View.VISIBLE
                binding.textView10.text = ""
                binding.reOtp.isEnabled = true
            }
        }.start()
    }
    private fun updateTimerUI() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        binding.textView10.text = timeLeftFormatted
    }


}