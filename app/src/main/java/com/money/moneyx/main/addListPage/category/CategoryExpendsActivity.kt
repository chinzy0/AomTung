package com.money.moneyx.main.addListPage.category

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.money.moneyx.R
import com.money.moneyx.databinding.ActivityCategoryExpendsBinding

class CategoryExpendsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCategoryExpendsBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private var categoryModel = ArrayList<CategoryModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryExpendsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView6.setOnClickListener {
            onBackPressed()
        }
        mockUp()
        addAdapter()



    }
    private fun mockUp(){
        categoryModel.add(CategoryModel(R.drawable.shopping,"ชอปปิ้ง"))
        categoryModel.add(CategoryModel(R.drawable.food,"อาหาร & เครื่องดื่ม"))
        categoryModel.add(CategoryModel(R.drawable.travel,"ค่าเดินทาง"))
        categoryModel.add(CategoryModel(R.drawable.bill,"ค่าชำระบิล"))
        categoryModel.add(CategoryModel(R.drawable.phone,"ค่าโทรศัพท์"))
        categoryModel.add(CategoryModel(R.drawable.pet,"สัตว์เลี้ยง"))
        categoryModel.add(CategoryModel(R.drawable.health,"สุขภาพ"))
        categoryModel.add(CategoryModel(R.drawable.beauty,"ความงาม"))
        categoryModel.add(CategoryModel(R.drawable.education,"การศึกษา"))
        categoryModel.add(CategoryModel(R.drawable.insurance,"ค่าประกัน"))
        categoryModel.add(CategoryModel(R.drawable.investment,"การลงทุน"))
        categoryModel.add(CategoryModel(R.drawable.entertainment,"บันเทิง"))
        categoryModel.add(CategoryModel(R.drawable.tax,"ภาษี & ค่าธรรมเนียม"))
        categoryModel.add(CategoryModel(R.drawable.house_item,"ของใช้ในบ้าน"))
        categoryModel.add(CategoryModel(R.drawable.other_ex,"อื่นๆ"))

    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun addAdapter(){
        categoryAdapter = CategoryAdapter(categoryModel) {
            when(it) {
                "ชอปปิ้ง" -> {
                    putCategory(it)
                }
                "อาหาร & เครื่องดื่ม" -> {
                    putCategory(it)
                }
                "ค่าเดินทาง" -> {
                    putCategory(it)
                }
                "ค่าชำระบิล" -> {
                    putCategory(it)
                }
                "ค่าโทรศัพท์" -> {
                    putCategory(it)
                }
                "สัตว์เลี้ยง" -> {
                    putCategory(it)
                }
                "สุขภาพ" -> {
                    putCategory(it)
                }
                "ความงาม" -> {
                    putCategory(it)
                }
                "การศึกษา" -> {
                    putCategory(it)
                }
                "ค่าประกัน" -> {
                    putCategory(it)
                }
                "การลงทุน" -> {
                    putCategory(it)
                }
                "บันเทิง" -> {
                    putCategory(it)
                }
                "ภาษี & ค่าธรรมเนียม" -> {
                    putCategory(it)
                }
                "ของใช้ในบ้าน" -> {
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