package com.money.moneyx.main.addListPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.money.moneyx.R
import com.money.moneyx.data.ViewPagerAdapter
import com.money.moneyx.databinding.ActivityAddListScreenBinding
import com.money.moneyx.main.addListPage.addExpends.AddExpendsFragment
import com.money.moneyx.main.addListPage.addIncome.AddIncomeFragment

class AddListScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddListScreenBinding
    private val fragment = ArrayList<Fragment>()
    private lateinit var  mPageAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddListScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        tabLayout()
        changeTab()
        changeColorTab()
        onBack()


    }
    private fun changeTab(){
        val tabTitles = listOf("รายรับ", "รายจ่าย")
        TabLayoutMediator(binding.tabLayoutListAdd, binding.ListAddPage) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

    }

    private fun changeColorTab(){
        binding.tabLayoutListAdd.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 1) {
                    binding.tabLayoutListAdd.setSelectedTabIndicatorColor(resources.getColor(R.color.expends))
                } else {
                    binding.tabLayoutListAdd.setSelectedTabIndicatorColor(resources.getColor(R.color.income))
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
    private fun tabLayout(){
        val fragmentManager: FragmentManager = supportFragmentManager
        fragment.clear()
        fragment.add(AddIncomeFragment())
        fragment.add(AddExpendsFragment())
        mPageAdapter = ViewPagerAdapter(fragmentManager, lifecycle, fragment)
        binding.ListAddPage.adapter = mPageAdapter
        binding.ListAddPage.isUserInputEnabled = false
    }

    private fun onBack(){
        binding.appbarAddListPage.imageView6.setOnClickListener{
            onBackPressed()
        }
    }
}