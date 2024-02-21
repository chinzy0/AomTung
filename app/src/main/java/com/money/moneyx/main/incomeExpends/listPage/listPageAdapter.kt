package com.money.moneyx.main.incomeExpends.listPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.ListIncomeExpendsBinding
import com.money.moneyx.main.incomeExpends.summary.ReportALL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListPageAdapter(
    private val reportListSummary: ArrayList<ReportALL>,
    private val callback: (Triple<Int,String, ReportALL>) -> Unit
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
        holder.binding.textView17.text = reportListSummary[position].type_name
        holder.binding.textView19.text = reportListSummary[position].category_name
        holder.binding.textView24.text = reportListSummary[position].amount
        holder.binding.textView27.text = convertTimeStampToFormattedDateTime(reportListSummary[position].timestamp)

        holder.itemView.setOnClickListener{
            callback.invoke(Triple(reportListSummary[position].transaction_id,reportListSummary[position].type_data,reportListSummary[position]))

        }

        when (reportListSummary[position].type_name) {
            "รายรับไม่แน่นอน" -> {
                holder.binding.textView24.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
                holder.binding.textView17.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
                holder.binding.textView23.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
            }
            "รายรับแน่นอน" -> {
                holder.binding.textView24.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_certainText))
                holder.binding.textView23.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_certainText))
                holder.binding.textView17.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_certainText))
            }
            "รายจ่ายจำเป็น" -> {
                holder.binding.textView24.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.expends_letter2))
                holder.binding.textView23.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.expends_letter2))
                holder.binding.textView17.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.expends_letter2))
            }
            "รายจ่ายไม่จำเป็น" -> {
                holder.binding.textView24.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.expends_letter1))
                holder.binding.textView23.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.expends_letter1))
                holder.binding.textView17.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.expends_letter1))
            }
        }
    }
}

class ListPageViewAdapter(internal val binding: ListIncomeExpendsBinding):
    RecyclerView.ViewHolder(binding.root)
private fun convertTimeStampToFormattedDateTime(timeStamp: Int): String {
    val date = Date(timeStamp * 1000L)
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(date)
}