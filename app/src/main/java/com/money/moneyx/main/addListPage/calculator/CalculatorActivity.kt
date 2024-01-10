package com.money.moneyx.main.addListPage.calculator

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.money.moneyx.R
import com.money.moneyx.databinding.ActivityCalculatorBinding
import com.money.moneyx.login.forgotPassword.ForgotPasswordActivity
import com.money.moneyx.main.addListPage.ReportViewModel
import com.money.moneyx.main.profile.ProfileViewModel

class CalculatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculatorBinding
    private lateinit var viewModel: ReportViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ReportViewModel::class.java]
        binding.reportViewModel = viewModel
        setContentView(binding.root)
        setEventClick()
    }

    private fun setEventClick() {
        viewModel.onClick.observe(this, Observer {
            when (it) {
                "onBack" -> {
                    onBackPressed()
                }

                "btn" -> {
                    val status =  intent.getStringExtra("status")

                    if (status != "true") {
                        intent.putExtra("number", 22222222)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        intent.putExtra("number", 11111111)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }

                }
            }
        })
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }


}