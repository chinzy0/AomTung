package com.money.moneyx.login.enterPincode

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivityEnterPinCodeBinding
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.login.createPincode.CustomKeyboardAdapter
import com.money.moneyx.login.createPincode.CustomKeyboardModel
import com.money.moneyx.login.forgotPassword.ForgotPasswordActivity
import com.money.moneyx.login.loginScreen.LoginViewModel
import com.money.moneyx.main.homeScreen.HomeActivity

class EnterPinCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnterPinCodeBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var keyboardAdapter: CustomKeyboardAdapter
    private var listKeyboard = ArrayList<CustomKeyboardModel>()
    private val preferences = Preference.getInstance(this)
    private var isDialogShowing = false
    private var fingerprint = ""
    private var phoneNumber = ""
    private val positionClick = "forgot_password"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterPinCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        val preferences = Preference.getInstance(this)
        fingerprint = preferences.getString("FINGERPRINT", "")


        loadingScreen(this)
        setEventClick()
        keyboard()
        pinview()
        checkDeviceHasBiometric()


    }

    private fun keyboard() {
        listKeyboard.add(CustomKeyboardModel("1", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("2", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("3", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("4", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("5", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("6", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("7", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("8", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("9", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("-", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("0", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("delete", R.drawable.delete))


        keyboardAdapter = CustomKeyboardAdapter(listKeyboard) { number ->
            if (number == "delete") {
                val currentText = binding.PinView.text.toString()
                if (currentText.isNotEmpty()) {
                    val newText = currentText.substring(0, currentText.length - 1)
                    binding.PinView.setText(newText)
                }
            } else {
                val currentText = binding.PinView.text.toString()
                val newText = currentText + number
                binding.PinView.setText(newText)
            }

        }


        binding.textView2.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        binding.keyboard.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = keyboardAdapter
            keyboardAdapter.notifyDataSetChanged()
        }
    }

    private fun pinview() {

        binding.PinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val enteredText = s.toString()
                if (enteredText.length == 6) {
                    pinConfirmationSuccess()
                }

            }

        })
    }


    private fun setEventClick() {
        phoneNumber = intent.getStringExtra("PHONE").toString()
        viewModel.onClick.observe(this, Observer {
            when (it) {
                "ForgotPincode" -> {
                    AVLoading.startAnimLoading()
                    viewModel.otpForgotPassword(phoneNumber) { model ->
                        AVLoading.stopAnimLoading()
                        val intent = Intent(this, ForgotPasswordActivity::class.java)
                        intent.putExtra("PHONE",phoneNumber)
                        intent.putExtra("forgotPasswordDataModel",model.data)
                        startActivity(intent)
                    }

                }
            }
        })
    }

    private fun pinConfirmationSuccess() {
        AVLoading.startAnimLoading()
        phoneNumber = intent.getStringExtra("PHONE").toString()
        viewModel.memberLogin(phone = phoneNumber, password = binding.PinView.text.toString())
        { member ->
            AVLoading.stopAnimLoading()
            if (member.success) {
                if (member.data.is_Verified) {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    listKeyboard.clear()
                    preferences.saveString("idmember", member.data.idmember.toString())
                    preferences.saveString("phone", phoneNumber)
                    preferences.saveString("username", member.data.username)
                    preferences.saveString("image", member.data.image)
                    preferences.saveString("pincode",binding.PinView.text.toString())
                    startActivity(intent)
                } else {
                    runOnUiThread {
                        binding.PinView.text!!.clear()
                        showCustomDialog()
                        binding.textView.text = "ลองใหม่อีกครั้ง"
                        binding.textView.setTextColor(ContextCompat.getColor(this, R.color.red))

                    }

                }
            }
        }

    }

    private fun showCustomDialog() {
        if (isDialogShowing) {
            return
        }
        runOnUiThread {
            val dialog = Dialog(this)
            dialog.setCanceledOnTouchOutside(false)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dailog_wrong_pincode)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            isDialogShowing = true

            dialog.show()

            var ok = dialog.findViewById<TextView>(R.id.okDialog)
            ok.setOnClickListener {
                dialog.dismiss()
                binding.PinView.text?.clear()
                isDialogShowing = false
            }
        }
    }

    private fun checkDeviceHasBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                checkBiometric()
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("MY_APP_TAG", "No biometric features available on this device.")

            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")

            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {

            }

        }
    }

    private fun checkBiometric() {
        if (fingerprint == "ON") {
            showBiometricPrompt()
        }
    }

    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("ยืนยันตัวตนด้วยลายนิ้วมือ")
            .setSubtitle("โปรดสแกนลายนิ้วมือเพื่อเข้าสู่ระบบ")
            .setNegativeButtonText("ใช้รหัส PIN แทน")
            .build()

        val biometricPrompt = BiometricPrompt(
            this,
            ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val preferences = Preference.getInstance(this@EnterPinCodeActivity)
                    val savedPin = preferences.getString("pincode", "")
                    binding.PinView.setText(savedPin)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()

                }
            })

        biometricPrompt.authenticate(promptInfo)
    }
}