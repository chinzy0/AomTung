package com.money.moneyx.main.homeScreen.fragments.report.incomeReport

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.PastprogramBinding

class IncomeReportAdapter(
    private val incomeReportModel:  List<ReportMonthIncome>,
    function: () -> Unit
): RecyclerView.Adapter<IncomeViewAdapter>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewAdapter {
        val listPast : PastprogramBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.pastprogram,
            parent,
            false
        )
        return IncomeViewAdapter(listPast)
    }

    override fun getItemCount() = incomeReportModel.size

    override fun onBindViewHolder(holder: IncomeViewAdapter, position: Int) {
        holder.binding.textIncomeType.text = incomeReportModel[position].report_List[position].type_data
        holder.binding.textCategory.text = incomeReportModel[position].report_List[position].type_data
        holder.binding.textDate.text = incomeReportModel[position].report_List[position].type_data
        holder.binding.textMoney.text = incomeReportModel[position].report_List[position].type_data


    }

}

class IncomeViewAdapter (internal val binding: PastprogramBinding):
        RecyclerView.ViewHolder(binding.root)

