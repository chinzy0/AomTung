package com.money.moneyx.main.homeScreen.fragments.report.incomeReport

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.PastprogramBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IncomeReportAdapter(
    private val incomeReportModel: ArrayList<Report>,
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
        holder.binding.textIncomeType.text = incomeReportModel[position].type_name
        holder.binding.textCategory.text = incomeReportModel[position].category_name
        holder.binding.textDate.text = convertTimeStampToFormattedDateTime(incomeReportModel[position].timestamp)
        holder.binding.textMoney.text = incomeReportModel[position].amount
        holder.binding.textPlus.text = incomeReportModel[position].symbol_math
        if (incomeReportModel[position].type_name == "รายรับไม่แน่นอน") {
            holder.binding.textIncomeType.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
            holder.binding.textSymbol.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
            holder.binding.textMoney.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
            holder.binding.textPlus.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
        }

    }

}
private fun convertTimeStampToFormattedDateTime(timeStamp: Int): String {
    val date = Date(timeStamp * 1000L)
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(date)
}
class IncomeViewAdapter (internal val binding: PastprogramBinding):
    RecyclerView.ViewHolder(binding.root)

class ExpendsReportAdapter(
    private val expendsReportAdapter: ArrayList<Report>,
    function: () -> Unit
): RecyclerView.Adapter<expendsReportViewAdapter>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): expendsReportViewAdapter {
        val listPast : PastprogramBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.pastprogram,
            parent,
            false
        )
        return expendsReportViewAdapter(listPast)
    }

    override fun getItemCount()=expendsReportAdapter.size

    override fun onBindViewHolder(holder: expendsReportViewAdapter, position: Int) {
        holder.binding.textIncomeType.text = expendsReportAdapter[position].type_name
        holder.binding.textCategory.text = expendsReportAdapter[position].category_name
        holder.binding.textDate.text = convertTimeStampToFormattedDateTime(expendsReportAdapter[position].timestamp)
        holder.binding.textMoney.text = expendsReportAdapter[position].amount
        holder.binding.textPlus.text = expendsReportAdapter[position].symbol_math
        if (expendsReportAdapter[position].type_name == "รายรับไม่แน่นอน") {
            holder.binding.textIncomeType.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
            holder.binding.textSymbol.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
            holder.binding.textMoney.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
            holder.binding.textPlus.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
        }
    }


}

class expendsReportViewAdapter  (internal val binding: PastprogramBinding):
    RecyclerView.ViewHolder(binding.root)




