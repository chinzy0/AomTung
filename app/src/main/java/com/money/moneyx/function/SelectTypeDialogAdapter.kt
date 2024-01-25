package com.money.moneyx.function

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.ListDailogBinding
import com.money.moneyx.main.addListPage.addIncome.GetAllTypeIncomeData

class SelectTypeDialogAdapter(
    private val getAllTypeIncomeData: List<GetAllTypeIncomeData>,
    function: () -> Unit
) : RecyclerView.Adapter<SelectTypeViewAdapter>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectTypeViewAdapter {
      val menu : ListDailogBinding = DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.list_dailog,
          parent,
          false
      )
        return SelectTypeViewAdapter(menu)
    }
    override fun getItemCount() = getAllTypeIncomeData.size

    override fun onBindViewHolder(holder: SelectTypeViewAdapter, position: Int) {
        holder.binding.listMenuDialog.text = getAllTypeIncomeData[position].type
    }

}

class SelectTypeViewAdapter (internal val binding: ListDailogBinding) :
    RecyclerView.ViewHolder(binding.root)
