package com.money.moneyx.main.autoSave

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.ListAutosaveBinding
import com.money.moneyx.databinding.PastprogramBinding
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.Report
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AutoSaveAdapter(
    private val autoSaveModel: ArrayList<GetListAutoData>,
    private val callback: (Triple<Int,String,GetListAutoData>) -> Unit)
    : RecyclerView.Adapter<AutoSaveViewAdapter>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoSaveViewAdapter {
        val listAutoSave : ListAutosaveBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_autosave,
            parent,
            false
        )
        return AutoSaveViewAdapter(listAutoSave)
    }

    override fun getItemCount() = autoSaveModel.size

    override fun onBindViewHolder(holder: AutoSaveViewAdapter, position: Int) {
        holder.binding.autoSaveCat.text = autoSaveModel[position].category_name
        holder.binding.dateAutoSave.text = convertTimeStampToFormattedDateTime(autoSaveModel[position].timestamp)
        holder.binding.textAmount.text = autoSaveModel[position].amount
        holder.binding.textSym.text = autoSaveModel[position].currency_symbol
        holder.binding.textFrequency.text = autoSaveModel[position].save_auto_name
        holder.binding.autoSaveType.text = autoSaveModel[position].type_name

        holder.itemView.setOnClickListener{
            callback.invoke(Triple(autoSaveModel[position].transaction_id,autoSaveModel[position].dataType,autoSaveModel[position]))

        }

        when (autoSaveModel[position].type_name) {
            "รายรับไม่แน่นอน" -> {
                holder.binding.autoSaveType.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
                holder.binding.textSym.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
                holder.binding.textAmount.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_uncertainText))
            }
            "รายรับแน่นอน" -> {
                holder.binding.autoSaveType.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_certainText))
                holder.binding.textSym.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_certainText))
                holder.binding.textAmount.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.income_certainText))
            }
            "รายจ่ายจำเป็น" -> {
                holder.binding.autoSaveType.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.expends_letter2))
                holder.binding.textSym.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.expends_letter2))
                holder.binding.textAmount.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.expends_letter2))
            }
            "รายจ่ายไม่จำเป็น" -> {
                holder.binding.autoSaveType.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.expends_letter1))
                holder.binding.textSym.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.expends_letter1))
                holder.binding.textAmount.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.expends_letter1))
            }
        }
    }
}

class AutoSaveViewAdapter(internal val binding: ListAutosaveBinding):
    RecyclerView.ViewHolder(binding.root)
private fun convertTimeStampToFormattedDateTime(timeStamp: Int): String {
    val date = Date(timeStamp * 1000L)
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(date)
}