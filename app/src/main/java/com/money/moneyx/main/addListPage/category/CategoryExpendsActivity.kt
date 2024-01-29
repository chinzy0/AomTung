package com.money.moneyx.main.addListPage.category

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.money.moneyx.R
import com.money.moneyx.databinding.ActivityCategoryExpendsBinding
import com.money.moneyx.main.addListPage.addIncome.GetAllCategoryincomeData
import com.money.moneyx.main.homeScreen.HomeActivity

class CategoryExpendsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCategoryExpendsBinding
    private lateinit var categoryExpendsAdapter: CategoryExpendsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryExpendsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView6.setOnClickListener {
            onBackPressed()
        }

        addAdapter()



    }


    private fun addAdapter(){
        val myListExpends: List<Pair<Int, String>> = listOf(
            Pair(R.drawable.shopping, "shopping"),
            Pair(R.drawable.food, "food"),
            Pair(R.drawable.travel, "travel"),
            Pair(R.drawable.bill, "bill"),
            Pair(R.drawable.phone, "phone"),
            Pair(R.drawable.pet, "pet"),
            Pair(R.drawable.health, "health"),
            Pair(R.drawable.beauty, "beauty"),
            Pair(R.drawable.education, "education"),
            Pair(R.drawable.insurance, "insurance"),
            Pair(R.drawable.investment, "investment"),
            Pair(R.drawable.entertainment, "entertainment"),
            Pair(R.drawable.tax, "tax"),
            Pair(R.drawable.house_item, "house_item"),
            Pair(R.drawable.other_ex, "other_ex"),
            )

        categoryExpendsAdapter = CategoryExpendsAdapter(HomeActivity.getAllCategoryExpenses!!.data, myListExpends) {
            when(it.first) {
                "ชอปปิ้ง" -> {
                    putCategory(it.first,it.second)
                }
                "อาหาร&เครื่องดื่ม" -> {
                    putCategory(it.first, it.second)
                }
                "ค่าเดินทาง" -> {
                    putCategory(it.first, it.second)

                }
                "ค่าชำระบิล" -> {
                    putCategory(it.first, it.second)

                }
                "ค่าเช่า" -> {
                    putCategory(it.first, it.second)

                }
                "ค่าโทรศัพท์" -> {
                    putCategory(it.first, it.second)

                }
                "สัตว์เลี้ยง" -> {
                    putCategory(it.first, it.second)

                }
                "สุขภาพ" -> {
                    putCategory(it.first, it.second)
                }
                "ความงาม" -> {
                    putCategory(it.first, it.second)

                }
                "การศึกษา" -> {
                    putCategory(it.first, it.second)

                }
                "ค่าประกัน" -> {
                    putCategory(it.first, it.second)

                }
                "การลงทุน" -> {
                    putCategory(it.first, it.second)

                }
                "บันเทิง" -> {
                    putCategory(it.first, it.second)

                }
                "ภาษี&ค่าธรรมเนียม" -> {
                    putCategory(it.first, it.second)

                }
                "ของใช้ในบ้าน" -> {
                    putCategory(it.first, it.second)

                }
                "อื่นๆ" -> {
                    putCategory(it.first, it.second)

                }
            }
        }

        binding.RcvCategory.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoryExpendsAdapter
            categoryExpendsAdapter.notifyDataSetChanged()
        }
    }
    private fun putCategory(selectedCategory: String, categoryId: Int) {
        intent.putExtra("Category", selectedCategory)
        intent.putExtra("Category_Id", categoryId)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


}