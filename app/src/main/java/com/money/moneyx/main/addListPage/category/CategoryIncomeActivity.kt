package com.money.moneyx.main.addListPage.category

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.money.moneyx.R
import com.money.moneyx.databinding.ActivityCategoryIncomeBinding
import com.money.moneyx.main.homeScreen.HomeActivity

class CategoryIncomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCategoryIncomeBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView6.setOnClickListener {
            onBackPressed()
        }
        addAdapter()


    }


    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun addAdapter(){
        val myList: List<Pair<Int, String>> = listOf(
            Pair(R.drawable.salary_cat, "salary_cat"),
            Pair(R.drawable.extraincome_cat, "extraincome_cat"),
            Pair(R.drawable.business_cat, "business_cat"),
            Pair(R.drawable.transactions_cat, "transactions_cat"),
            Pair(R.drawable.rent_cat, "rent_cat"),
            Pair(R.drawable.family_cat, "family_cat"),
            Pair(R.drawable.snack_cat, "snack_cat"),
            Pair(R.drawable.bonus_cat, "bonus_cat"),
            Pair(R.drawable.asset_cat, "asset_cat"),
            Pair(R.drawable.gift_cat, "gift_cat"),
            Pair(R.drawable.interest_cat, "interest_cat"),
            Pair(R.drawable.other_cat, "orther_cat"),

            )

        categoryAdapter = CategoryAdapter(HomeActivity.getAllCategoryincome!!.data, myList) {
            when(it.first) {
                "เงินเดือน" -> {
                    putCategory(it.first,it.second)
                }
                "รายได้พิเศษ" -> {
                    putCategory(it.first, it.second)
                }
                "รายได้จากธุรกิจ" -> {
                    putCategory(it.first, it.second)

                }
                "การทำธุรกรรม" -> {
                    putCategory(it.first, it.second)

                }
                "ค่าเช่า" -> {
                    putCategory(it.first, it.second)

                }
                "ครอบครัวช่วยเหลือ" -> {
                    putCategory(it.first, it.second)

                }
                "ค่าขนม" -> {
                    putCategory(it.first, it.second)

                }
                "ค่าล่วงเวลา&โบนัส" -> {
                    putCategory(it.first, it.second)
                }
                "สินทรัพย์" -> {
                    putCategory(it.first, it.second)

                }
                "ของขวัญ" -> {
                    putCategory(it.first, it.second)

                }
                "ดอกเบี้ย/เงินปันผล" -> {
                    putCategory(it.first, it.second)

                }
                "อื่นๆ" -> {
                    putCategory(it.first, it.second)

                }
            }
        }

        binding.RcvCategory.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoryAdapter
            categoryAdapter.notifyDataSetChanged()
        }
    }
    private fun putCategory(selectedCategory: String, categoryId: Int) {
        intent.putExtra("Category", selectedCategory)
        intent.putExtra("Category_Id", categoryId)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }




}