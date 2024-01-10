package com.money.moneyx.main.homeScreen.fragments.report.incomeReport

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.ListDropdownBinding

class DropdownAdapter(private val dropdownModel: ArrayList<DropdownModel>, function: () -> Unit):RecyclerView.Adapter<ViewAdapterListMonth>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAdapterListMonth {
        val month : ListDropdownBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_dropdown,
            parent,
            false
        )
        return ViewAdapterListMonth(month)
    }

    override fun getItemCount() = dropdownModel.size

    override fun onBindViewHolder(holder: ViewAdapterListMonth, position: Int) {
        holder.binding.textView31.text = dropdownModel[position].month
        holder.binding.textView31.text = dropdownModel[position].month
    }


}
class ViewAdapterListMonth(internal val binding: ListDropdownBinding) :
    RecyclerView.ViewHolder(binding.root)