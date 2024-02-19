package com.money.moneyx.main.incomeExpends.listPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.ListIncomeExpendsBinding
import com.money.moneyx.databinding.PastprogramBinding
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.Report
import com.money.moneyx.main.incomeExpends.summary.ReportListSummary
import com.money.moneyx.main.incomeExpends.summary.ReportListSummaryData

class ListPageAdapter(
    private val reportListSummary: ArrayList<ReportListSummaryData>,
    private val callback: (Triple<Int,String, Report>) -> Unit
): RecyclerView.Adapter<ListPageViewAdapter>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPageViewAdapter {
        val list : ListIncomeExpendsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_income_expends,
            parent,
            false
        )
        return ListPageViewAdapter(list)
    }

    override fun getItemCount() = reportListSummary.size

    override fun onBindViewHolder(holder: ListPageViewAdapter, position: Int) {
        TODO("Not yet implemented")
    }
}

class ListPageViewAdapter(internal val binding: ListIncomeExpendsBinding):
    RecyclerView.ViewHolder(binding.root)
