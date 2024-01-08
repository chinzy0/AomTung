package com.money.moneyx.main.homeScreen.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.money.moneyx.R
import com.money.moneyx.data.ViewPagerAdapter
import com.money.moneyx.databinding.FragmentHomeBinding
import com.money.moneyx.main.homeScreen.fragments.report.expendsReport.ExpendsReportFragment
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.IncomeReportFragment


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val fragment = ArrayList<Fragment>()
    private lateinit var  mPageAdapter: ViewPagerAdapter


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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragment.clear()
        fragment.add(IncomeReportFragment())
        fragment.add(ExpendsReportFragment())

        mPageAdapter = ViewPagerAdapter(fragmentManager, lifecycle, fragment)


        binding.report.adapter = mPageAdapter
        binding.report.isUserInputEnabled = false

        val tabTitles = listOf("รายรับ", "รายจ่าย")
        TabLayoutMediator(binding.tabLayout, binding.report) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
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

        return binding.root
    }


}