package com.money.moneyx.main.addListPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.money.moneyx.R
import com.money.moneyx.data.ViewPagerAdapter
import com.money.moneyx.databinding.ActivityAddListScreenBinding
import com.money.moneyx.function.showConfirmOnBack
import com.money.moneyx.main.addListPage.addExpends.AddExpendsFragment
import com.money.moneyx.main.addListPage.addIncome.AddIncomeFragment
import com.money.moneyx.main.homeScreen.fragments.report.expendsReport.ExpendsReportFragment
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.Report
import java.text.FieldPosition

class AddListScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddListScreenBinding
    private val fragment = ArrayList<Fragment>()
    private lateinit var mPageAdapter: ViewPagerAdapter
    private val onClickDialog = MutableLiveData<String>()
    private var edit = ""
    private var editIncome: Report? = null
    private var editExpends: Report? = null

    companion object {
        val textResult = MutableLiveData<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddListScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        checkAddress()
        tabLayout()
        changeTab()
        changeColorTab()
        onBack()
        setEventClick()

        Log.i("asdkalmdksadasd", edit)
    }

    private fun checkAddress() {
        edit = intent.getStringExtra("edit").toString()
        editIncome = intent.getParcelableExtra("modelIncomeEdit")
        editExpends = intent.getParcelableExtra("modelExpenseEdit")
        if (edit == "null") {
            binding.appbarAddListPage.textView9.text = "เพิ่มรายการ"
        } else {
            binding.appbarAddListPage.textView9.text = "แก้ไข"
        }
    }

    private fun changeTab() {
        val tabTitles = listOf("รายรับ", "รายจ่าย")
        TabLayoutMediator(binding.tabLayoutListAdd, binding.ListAddPage)
        { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

    }

    private fun changeColorTab() {
        binding.tabLayoutListAdd.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 1) {
                    binding.tabLayoutListAdd.setSelectedTabIndicatorColor(resources.getColor(R.color.expends))
                } else {
                    binding.tabLayoutListAdd.setSelectedTabIndicatorColor(resources.getColor(R.color.income))
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun tabLayout() {
        val fragmentManager: FragmentManager = supportFragmentManager
        fragment.clear()
        fragment.add(AddIncomeFragment(editIncome))
        fragment.add(AddExpendsFragment(editExpends))
        mPageAdapter = ViewPagerAdapter(fragmentManager, lifecycle, fragment)
        binding.ListAddPage.adapter = mPageAdapter
        binding.ListAddPage.isUserInputEnabled = false

        if (edit == "editExpense" ) {
            binding.ListAddPage.currentItem = 1
            binding.tabLayoutListAdd.setSelectedTabIndicatorColor(resources.getColor(R.color.expends))
        }else{
            binding.ListAddPage.currentItem = 0
            binding.tabLayoutListAdd.setSelectedTabIndicatorColor(resources.getColor(R.color.income))
        }
    }

    private fun onBack() {
        binding.appbarAddListPage.imageView6.setOnClickListener {
            if (textResult.value != null && textResult.value!!.isNotEmpty()) {
                showConfirmOnBack(this, onClickDialog)
            } else {
                onBackPressed()
            }
        }
    }

    private fun setEventClick() {
        onClickDialog.observe(this, Observer {
            when (it) {
                "confirm" -> {
                    textResult.value = ""
                    onBackPressed()
                }
            }
        })

    }


}