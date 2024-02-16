package com.money.moneyx.splashScreen

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivitySplashScreenBinding
import com.money.moneyx.login.NameInput.NameInputActivity
import com.money.moneyx.login.enterPincode.EnterPinCodeActivity
import com.money.moneyx.login.loginScreen.LoginActivity
import com.money.moneyx.view.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    private  lateinit var binding: ActivitySplashScreenBinding
    private var savedPhone = ""
    private var status = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val preferences = Preference.getInstance(this)

        savedPhone = preferences.getString("phone","")

        check()

        Handler().postDelayed({
            if (status){
                val intent = Intent(this@SplashScreenActivity, EnterPinCodeActivity::class.java)
                intent.putExtra("PHONE",savedPhone)
                startActivity(intent)
            }else{
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            finish()
        },1000)



    }

    private fun check() {
        status = savedPhone.isNotEmpty()
    }


}