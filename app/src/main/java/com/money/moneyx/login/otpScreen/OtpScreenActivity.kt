package com.money.moneyx.login.otpScreen

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivityOtpScreenBinding
import com.money.moneyx.function.dialogOtp
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.function.wrongOtpDialog
import com.money.moneyx.login.createPincode.CreatePinActivity
import com.money.moneyx.login.loginScreen.DataOTP
import com.money.moneyx.login.loginScreen.LoginViewModel
import com.money.moneyx.login.loginScreen.ResultOTP
import com.money.moneyx.main.homeScreen.HomeActivity
import java.util.Locale

class OtpScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpScreenBinding
    private lateinit var viewModel: LoginViewModel
    private val preferences = Preference.getInstance(this)
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftInMillis = 60L * 1000L
    private var phoneNumber = ""
    private var editTelNumber = ""
    private var otp = ""
    private var refCode = ""
    private var idMember = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        idMember = preferences.getInt("idmember", 0)
        viewModel.mDataModel = intent.getParcelableExtra<DataOTP>("mDataModel")
        phoneNumber = intent.getStringExtra("PHONE").toString()
        editTelNumber = intent.getStringExtra("EditTelNumber").toString()
        otp = viewModel.mDataModel!!.codeotp
        refCode = viewModel.mDataModel!!.refCode

        loadingScreen(this)
        setEventClick()
        buttonActivated()
        setOTPText()


        binding.appbarOtp.BackPage.setOnClickListener {
            this.onBackPressed()
        }
    }

    private fun buttonActivated() {
        binding.OtpPinview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val textLength = s?.length ?: 0
                if (textLength == 6) {
                    if (s.toString() == otp) {
                        binding.buttonSubmit.isEnabled = true
                        binding.buttonSubmit.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button))
                        viewModel.otpAuth = true
                    } else {
                        binding.buttonSubmit.isEnabled = true
                        binding.buttonSubmit.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button))
                        viewModel.otpAuth = false
                    }
                } else {
                    binding.buttonSubmit.isEnabled = false
                    binding.buttonSubmit.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button_disable))
                }
            }
        })
    }

    private fun setOTPText() {
        AVLoading.startAnimLoading()
        viewModel.mDataModel?.let { data ->
            AVLoading.stopAnimLoading()
            if (data.codeotp.isNullOrEmpty()) {
                if (editTelNumber == "EditTelNumber"){
                    showDialogIsDuplicate()
                }
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
                "SubmitOtpButton" -> {
                    binding.buttonSubmit.isEnabled = false
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
                                    binding.buttonSubmit.isEnabled = true
                                }
                                if (editTelNumber == "EditTelNumber"){
                                    viewModel.resetPhone(phone = phoneNumber, idmember = idMember){ update ->
                                        if (update.data.is_Seccess){
                                            runOnUiThread{ showSuccessDialog() }
                                        }
                                    }
                                }else{
                                    val intent = Intent(this, CreatePinActivity::class.java)
                                    intent.putExtra("mDataModel", model.data)
                                    intent.putExtra("PHONE", phoneNumber)
                                    startActivity(intent)
                                }

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
            viewModel.generateOTP(phoneNumber) { model ->
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
    private fun showSuccessDialog() {
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
        ok.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, HomeActivity::class.java)
            preferences.saveString("phone", phoneNumber)
            startActivity(intent)
        }
    }
    private fun showDialogIsDuplicate() {
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
        text.setText("หมายเลขนี้มีบัญชีอยู่แล้ว  \n" +
                "กรุณากรอกหมายเลขอื่น")
        ok.setOnClickListener {
            dialog.dismiss()
            onBackPressed()
        }
    }



}