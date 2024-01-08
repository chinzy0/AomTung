package com.money.moneyx.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.money.moneyx.R
import com.money.moneyx.databinding.ActivityMainBinding
import com.money.moneyx.viewModel.TestViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TestViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        viewModel = ViewModelProvider(this)[TestViewModel::class.java]
        binding.mViewModel = viewModel

        setApi()
        setEventClick()

    }

    private fun setEventClick() {
        viewModel.onClick.observe(this, Observer {
            when (it) {
                "click" -> Toast.makeText(this, "ทดสอบคลิก", Toast.LENGTH_LONG).show()
                "click2" -> Toast.makeText(this, "22222222", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setApi() {

        viewModel.callService()
        viewModel.dataResult.observe(this, Observer {
            Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
        })
    }


}