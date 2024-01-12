package com.money.moneyx.main.addListPage.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.ListCategoryBinding

class CategoryAdapter(private val categoryModel: ArrayList<CategoryModel>, private val callback: (String) -> Unit) : RecyclerView.Adapter<CategoryViewAdapter>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewAdapter {
        val list : ListCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_category,
            parent,
            false
        )
        return CategoryViewAdapter(list)
    }

    override fun getItemCount() = categoryModel.size

    override fun onBindViewHolder(holder: CategoryViewAdapter, position: Int) {
       holder.binding.imageCategory.setImageResource(categoryModel[position].catImage)
        holder.binding.textCategory.text = categoryModel[position].catText
        holder.itemView.setOnClickListener{
            callback.invoke(categoryModel[position].catText)
        }
    }
}

class CategoryViewAdapter (internal  val binding: ListCategoryBinding):
    RecyclerView.ViewHolder(binding.root)
