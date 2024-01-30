package com.money.moneyx.main.homeScreen.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.money.moneyx.R
import com.money.moneyx.data.ViewPagerAdapter
import com.money.moneyx.databinding.FragmentHomeBinding
import com.money.moneyx.main.homeScreen.HomeViewModel
import com.money.moneyx.main.homeScreen.fragments.report.expendsReport.ExpendsReportFragment
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.IncomeReportFragment
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.ReportMonth


class HomeFragment constructor(private val reportMonth: ReportMonth?) : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val fragment = ArrayList<Fragment>()
    private lateinit var mPageAdapter: ViewPagerAdapter
    private lateinit var viewModel: HomeViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.homeViewModel = viewModel



        reportMonth?.data?.map { map ->
            binding.textBalance.text = map.totalBalance
            binding.textIncomeTotal.text = map.totalIncome
            binding.textExpendsTotal.text = map.totalExpenses
        }



        fragmentSetup()
        swapTab()
        changeColor()


        return binding.root


    }

    private fun fragmentSetup() {
        reportMonth?.data?.map { map ->
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            fragment.clear()
            fragment.add(IncomeReportFragment(map.report_month_list_income))
            fragment.add(ExpendsReportFragment(map.report_month_list_Expenses))

            mPageAdapter = ViewPagerAdapter(fragmentManager, lifecycle, fragment)


            binding.report.adapter = mPageAdapter
            binding.report.isUserInputEnabled = false
        }

    }

    private fun swapTab() {
        val tabTitles = listOf("รายรับ", "รายจ่าย")
        TabLayoutMediator(binding.tabLayout, binding.report) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun changeColor() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 1) {
                    binding.tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.expends))
                } else {
                    binding.tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.income))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }


}