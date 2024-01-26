package com.money.moneyx.function

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.ListDailogBinding
import com.money.moneyx.main.addListPage.addIncome.GetAllTypeIncomeData
import com.money.moneyx.main.addListPage.addIncome.ListScheduleAutoData

class SelectTypeDialogAdapter(
    private val getAllTypeIncomeData: List<GetAllTypeIncomeData>,
    private val callback: (Pair<String,Int>) -> Unit) : RecyclerView.Adapter<SelectTypeViewAdapter>(){

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

        holder.binding.listMenuDialog.setOnClickListener {
            callback.invoke(Pair(getAllTypeIncomeData[position].type,getAllTypeIncomeData[position].id))
        }
    }

}
class SelectTypeViewAdapter (internal val binding: ListDailogBinding) :
    RecyclerView.ViewHolder(binding.root)

class SelectAutoSaveDialogAdapter(
    private val listScheduleAutoData: List<ListScheduleAutoData>,
    private val callback: (Pair<String,Int>) -> Unit) : RecyclerView.Adapter<SelectAutoSaveViewAdapter>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectAutoSaveViewAdapter {
        val menu : ListDailogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_dailog,
            parent,
            false
        )
        return SelectAutoSaveViewAdapter(menu)
    }

    override fun getItemCount()= listScheduleAutoData.size

    override fun onBindViewHolder(holder: SelectAutoSaveViewAdapter, position: Int) {
        holder.binding.listMenuDialog.text = listScheduleAutoData[position].frequency
        holder.binding.listMenuDialog.setOnClickListener {
            callback.invoke(Pair(listScheduleAutoData[position].frequency,listScheduleAutoData[position].id))
        }
    }


}
class SelectAutoSaveViewAdapter (internal val binding: ListDailogBinding) :
    RecyclerView.ViewHolder(binding.root)