package com.money.moneyx.main.addListPage.category

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.money.moneyx.R
import com.money.moneyx.databinding.ActivityCategoryIncomeBinding
import com.money.moneyx.login.otpScreen.OtpScreenActivity
import com.money.moneyx.main.addListPage.addIncome.AddIncomeFragment
import kotlin.math.log

class CategoryIncomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCategoryIncomeBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private var categoryModel = ArrayList<CategoryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView6.setOnClickListener {
            onBackPressed()
        }

        mockUp()
        addAdapter()



    }
    private fun mockUp(){
        categoryModel.add(CategoryModel(R.drawable.salary_cat,"เงินเดือน"))
        categoryModel.add(CategoryModel(R.drawable.extraincome_cat,"รายได้พิเศษ"))
        categoryModel.add(CategoryModel(R.drawable.business_cat,"รายได้จากธุรกิจ"))
        categoryModel.add(CategoryModel(R.drawable.transactions_cat,"การทำธุรกรรม"))
        categoryModel.add(CategoryModel(R.drawable.rent_cat,"ค่าเช่า"))
        categoryModel.add(CategoryModel(R.drawable.family_cat,"ครอบครัวช่วยเหลือ"))
        categoryModel.add(CategoryModel(R.drawable.snack_cat,"ค่าขนม"))
        categoryModel.add(CategoryModel(R.drawable.bonus_cat,"ค่าล่วงเวลา & โบนัส"))
        categoryModel.add(CategoryModel(R.drawable.asset_cat,"สินทรัพย์"))
        categoryModel.add(CategoryModel(R.drawable.gift_cat,"ของขวัญ"))
        categoryModel.add(CategoryModel(R.drawable.interest_cat,"ดอกเบี้ย/เงินปันผล"))
        categoryModel.add(CategoryModel(R.drawable.other_cat,"อื่นๆ"))
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun addAdapter(){
        categoryAdapter = CategoryAdapter(categoryModel) {
            when(it) {
                "เงินเดือน" -> {
                    putCategory(it)
                }
                "รายได้พิเศษ" -> {
                    putCategory(it)
                }
                "รายได้จากธุรกิจ" -> {
                    putCategory(it)
                }
                "การทำธุรกรรม" -> {
                    putCategory(it)
                }
                "ค่าเช่า" -> {
                    putCategory(it)
                }
                "ครอบครัวช่วยเหลือ" -> {
                    putCategory(it)
                }
                "ค่าขนม" -> {
                    putCategory(it)
                }
                "ค่าล่วงเวลา & โบนัส" -> {
                    putCategory(it)
                }
                "สินทรัพย์" -> {
                    putCategory(it)
                }
                "ของขวัญ" -> {
                    putCategory(it)
                }
                "ดอกเบี้ย/เงินปันผล" -> {
                    putCategory(it)
                }
                "อื่นๆ" -> {
                    putCategory(it)
                }
            }
        }


        binding.RcvCategory.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoryAdapter
            categoryAdapter.notifyDataSetChanged()
        }
    }
    private fun putCategory(selectedCategory: String) {
        intent.putExtra("Category", selectedCategory)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }




}