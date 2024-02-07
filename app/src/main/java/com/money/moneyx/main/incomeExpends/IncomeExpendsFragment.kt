package com.money.moneyx.main.incomeExpends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.money.moneyx.R
import com.money.moneyx.data.ViewPagerAdapter
import com.money.moneyx.databinding.FragmentIncomeExpendsBinding
import com.money.moneyx.main.addListPage.addExpends.AddExpendsFragment
import com.money.moneyx.main.addListPage.addIncome.AddIncomeFragment
import com.money.moneyx.main.incomeExpends.listPage.ListPageFragment
import com.money.moneyx.main.incomeExpends.summary.SummaryFragment


class IncomeExpendsFragment : Fragment() {
    private lateinit var binding: FragmentIncomeExpendsBinding
    private val fragment = ArrayList<Fragment>()
    private lateinit var mPageAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_income_expends, container, false)

        tabLayout()
        changeTab()
        changeColorTab()


        return binding.root
    }
    private fun changeColorTab() {
        binding.tabLayoutIncomeExpends.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 1) {
                    binding.tabLayoutIncomeExpends.setSelectedTabIndicatorColor(resources.getColor(R.color.expends))
                    binding.tabLayoutIncomeExpends.setTabTextColors(
                        ContextCompat.getColor(requireContext(), R.color.unselected_tab_color),
                        ContextCompat.getColor(requireContext(), R.color.expends)
                    )
                } else {
                    binding.tabLayoutIncomeExpends.setSelectedTabIndicatorColor(resources.getColor(R.color.income))
                    binding.tabLayoutIncomeExpends.setTabTextColors(
                        ContextCompat.getColor(requireContext(), R.color.unselected_tab_color),
                        ContextCompat.getColor(requireContext(), R.color.income)
                    )
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun tabLayout() {
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragment.clear()
        fragment.add(ListPageFragment())
        fragment.add(SummaryFragment())
        mPageAdapter = ViewPagerAdapter(fragmentManager, lifecycle, fragment)
        binding.incomeExpendsPage.adapter = mPageAdapter
        binding.incomeExpendsPage.isUserInputEnabled = false
    }
    private fun changeTab() {
        val tabTitles = listOf("รายการ", "สรุปยอด")
        TabLayoutMediator(binding.tabLayoutIncomeExpends, binding.incomeExpendsPage)
        { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

}