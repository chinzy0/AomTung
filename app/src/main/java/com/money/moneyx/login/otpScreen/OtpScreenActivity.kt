package com.money.moneyx.login.otpScreen

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.money.moneyx.R
import com.money.moneyx.databinding.ActivityOtpScreenBinding
import com.money.moneyx.login.createPincode.CreatePinActivity
import com.money.moneyx.login.loginScreen.LoginViewModel
import java.util.Locale
import kotlin.random.Random

class OtpScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpScreenBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var countDownTimer: CountDownTimer
    var status = false
    private var timeLeftInMillis: Long = 60 * 1000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        viewModel.getOtp()
        viewModel.otp.observe(this, Observer {

            if (it.isNullOrEmpty())
                Toast.makeText(this,"พบข้อผิดพลาด",Toast.LENGTH_LONG).show()
            else
                binding.OtpPinview.setText(it)
        })

        setEventClick()
        startCountDownTimer()

        binding.OtpPinview.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                val textLength = s?.length ?:0
                if (textLength == 6){
                    if (s.toString() == viewModel.otp.value) {
                        binding.buttonSubmit.isEnabled = true
                        binding.buttonSubmit.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button))
                        status = true
                    } else {
                        binding.buttonSubmit.isEnabled = true
                        binding.buttonSubmit.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button))
                       status = false
                    }
                }
                else{
                    binding.buttonSubmit.isEnabled = false
                    binding.buttonSubmit.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button_disable))
                }
            }
        })

        binding.appbarOtp.BackPage.setOnClickListener{
            this.onBackPressed()
        }




}

    private fun genOTP():String{
        val OtpLength = 6
        val OTP = StringBuilder()

        repeat(OtpLength){
            val randomDigit = Random.nextInt(0,10)
            OTP.append(randomDigit)
        }
        return OTP.toString()
    }

    private fun setEventClick() {
        viewModel.onClick.observe(this, Observer {
            when (it) {
                "SubmitOtpButton" -> {
                    if (!status){
                        showCustomDialog()
                    }else{
                        val intent = Intent(this, CreatePinActivity::class.java)
                        startActivity(intent)
                    }

                }

            }
        })
    }

    private fun startCountDownTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerUI()
            }

            override fun onFinish() {
                binding.textView8.text = "ส่งรหัส OTP อีกครั้ง"
                binding.textView8.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                binding.imageView.visibility = View.VISIBLE
                binding.textView10.text = ""
                binding.reOtp.isEnabled = true


                binding.reOtp.setOnClickListener{
                    timeLeftInMillis = 60 * 1000
                    startCountDownTimer()
                    genOTP()
                    binding.textView8.text = "กรุณากรอกภายใน"
                    binding.textView8.paintFlags = 0
                    binding.imageView.visibility = View.GONE
                    binding.OtpPinview.setText(genOTP())




                }
            }


        }.start()
    }


    private fun updateTimerUI() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60

        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        binding.textView10.text = timeLeftFormatted
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.alert_otp)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
        var ok = dialog.findViewById<TextView>(R.id.okDialog)
        ok.setOnClickListener {
            dialog.dismiss()
        }

    }


}