package com.money.moneyx.main.profile.security

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivityPasswordAndSecurityBinding
import com.money.moneyx.databinding.ActivitySubmitPinBinding
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.login.enterPincode.EnterPinCodeActivity
import com.money.moneyx.login.forgotPassword.ResetPinCodeActivity
import com.money.moneyx.login.loginScreen.LoginViewModel
import com.money.moneyx.login.otpScreen.OtpScreenActivity
import com.money.moneyx.main.homeScreen.HomeActivity

class PasswordAndSecurityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordAndSecurityBinding
    private lateinit var sharePreferences: Preference
    private lateinit var viewModel: PasswordAndSecurityViewModel
    private var phonenumber =  ""
    private val positionClick = "ProfilePage"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordAndSecurityBinding.inflate(layoutInflater)
        sharePreferences = Preference.getInstance(applicationContext)
        val preferences = Preference.getInstance(this)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PasswordAndSecurityViewModel::class.java]
        binding.passwordAndSecurityViewModel = viewModel
        phonenumber =  preferences.getString("phone","")



        onBack()
        checkBiometric()
        setSwitchesFromPreferences()
        checkDeviceHasBiometric()
        setEventClick()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
    private fun onBack(){
        binding.imageView6.setOnClickListener{
            onBackPressed()
        }
    }
    private fun checkBiometric(){
        binding.switchFingerprint.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharePreferences.saveString("FINGERPRINT","ON")
            }else{
                sharePreferences.saveString("FINGERPRINT","OFF")
            }
        }

    }
    private fun setSwitchesFromPreferences() {
        val isFingerprintEnabled = sharePreferences.getString("FINGERPRINT", "") == "ON"
        binding.switchFingerprint.isChecked = isFingerprintEnabled
    }


    private fun checkDeviceHasBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->{
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
                binding.fingerPrint.visibility = View.GONE
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                binding.fingerPrint.visibility = View.GONE
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                binding.switchFingerprint.setOnClickListener{
                    Toast.makeText(this, "ไม่มีการลงทะเบียนลายนิ้วมื้อ", Toast.LENGTH_LONG).show()
                    binding.switchFingerprint.isChecked = false
                    binding.switchFingerprint.isEnabled = false
                }
            }

        }
    }

    private fun setEventClick() {
        viewModel.onClick.observe(this, Observer {
            when (it) {
                "changePasswordClick" -> {
                    val intent = Intent(this, ResetPinCodeActivity::class.java)
                    intent.putExtra("PHONE",phonenumber)
                    intent.putExtra("positionClick",positionClick)
                    startActivity(intent)
                    }
                }

        })
    }












}