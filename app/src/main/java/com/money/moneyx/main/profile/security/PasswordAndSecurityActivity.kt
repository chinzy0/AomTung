package com.money.moneyx.main.profile.security

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivityPasswordAndSecurityBinding
import com.money.moneyx.databinding.ActivitySubmitPinBinding

class PasswordAndSecurityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordAndSecurityBinding
    private lateinit var sharePreferences: Preference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordAndSecurityBinding.inflate(layoutInflater)
        sharePreferences = Preference.getInstance(applicationContext)
        setContentView(binding.root)
        onBack()
        checkBiometric()
        setSwitchesFromPreferences()
        checkDeviceHasBiometric()
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
                binding.switchFingerprint.isEnabled = false
            }

        }
    }












}