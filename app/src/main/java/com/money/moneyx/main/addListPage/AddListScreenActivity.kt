package com.money.moneyx.main.addListPage

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.money.moneyx.R
import com.money.moneyx.data.ViewPagerAdapter
import com.money.moneyx.databinding.ActivityAddListScreenBinding
import com.money.moneyx.databinding.FragmentAddIncomeBinding
import com.money.moneyx.function.dialogOtp
import com.money.moneyx.function.showConfirmOnBack
import com.money.moneyx.function.showExitDialog
import com.money.moneyx.function.wrongOtpDialog
import com.money.moneyx.login.loginScreen.LoginActivity
import com.money.moneyx.main.addListPage.addExpends.AddExpendsFragment
import com.money.moneyx.main.addListPage.addIncome.AddIncomeFragment

class AddListScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddListScreenBinding
    private val fragment = ArrayList<Fragment>()
    private lateinit var  mPageAdapter: ViewPagerAdapter
    private val onClickDialog = MutableLiveData<String>()
    companion object {
        val textResult = MutableLiveData<String>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddListScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        tabLayout()
        changeTab()
        changeColorTab()
        onBack()
        setEventClick()

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

    private fun onBack() {
        binding.appbarAddListPage.imageView6.setOnClickListener {
            if (textResult.value != null && textResult.value!!.isNotEmpty()) {
                showConfirmOnBack(this,onClickDialog)
            } else {
                onBackPressed()
            }
        }
    }

    private fun setEventClick() {
        onClickDialog.observe(this, Observer {
            when (it) {
                "confirm" -> {
                    onBackPressed()
                }
            }
        })

    }



}