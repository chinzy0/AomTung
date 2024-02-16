package com.money.moneyx.main.incomeExpends.listPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayoutMediator
import com.money.moneyx.R
import com.money.moneyx.data.ViewPagerAdapter
import com.money.moneyx.databinding.FragmentListPageBinding
import com.money.moneyx.main.incomeExpends.listPage.listDay.ListDayFragment
import com.money.moneyx.main.incomeExpends.listPage.listMonth.ListMonthFragment
import com.money.moneyx.main.incomeExpends.listPage.listWeek.ListWeekFragment
import com.money.moneyx.main.incomeExpends.listPage.listYear.ListYearFragment

class ListPageFragment : Fragment() {
    private lateinit var binding: FragmentListPageBinding
    private val fragment = ArrayList<Fragment>()
    private lateinit var mPageAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_list_page, container, false)

        tabLayout()
        changeTab()

        return binding.root
    }
    private fun tabLayout() {
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragment.clear()
        fragment.add(ListDayFragment())
        fragment.add(ListWeekFragment())
        fragment.add(ListMonthFragment())
        fragment.add(ListYearFragment())
        mPageAdapter = ViewPagerAdapter(fragmentManager, lifecycle, fragment)
        binding.listTab.adapter = mPageAdapter
        binding.listTab.isUserInputEnabled = false
    }
    private fun changeTab() {
        val tabTitles = listOf("วัน","สัปดาห์","เดือน","ปี")
        TabLayoutMediator(binding.tabLayoutList, binding.listTab)
        { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

}