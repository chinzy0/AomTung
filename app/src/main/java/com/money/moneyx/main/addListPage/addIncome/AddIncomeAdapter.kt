package com.money.moneyx.main.addListPage.addIncome

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.ListInformationBinding

class AddIncomeAdapter(
    private val addIncomeModel: ArrayList<AddIncomeModel>, private val callback: (String) -> Unit) : RecyclerView.Adapter<AddIncomeViewAdapter>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddIncomeViewAdapter {
       val list : ListInformationBinding = DataBindingUtil.inflate(
           LayoutInflater.from(parent.context),
           R.layout.list_information,
           parent,
           false
       )
        return AddIncomeViewAdapter(list)
    }

    override fun getItemCount() = addIncomeModel.size

    override fun onBindViewHolder(holder: AddIncomeViewAdapter, position: Int) {
        holder.binding.title.text = addIncomeModel[position].title
        holder.binding.textView18.text = addIncomeModel[position].detail
        holder.binding.img.setImageResource(addIncomeModel[position].img)
        if (addIncomeModel.size-1 == position){
            holder.binding.underline.visibility = View.GONE
        }
        holder.itemView.setOnClickListener{
            callback.invoke(addIncomeModel[position].title,)
        }
    }

}

class AddIncomeViewAdapter (internal  val binding: ListInformationBinding):
        RecyclerView.ViewHolder(binding.root)
