package com.money.moneyx.main.addListPage.addIncome

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.money.moneyx.R
import com.money.moneyx.databinding.ActivityCategoryIncomeBinding

class CategoryIncomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCategoryIncomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView6.setOnClickListener {
            onBackPressed()
        }


    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}