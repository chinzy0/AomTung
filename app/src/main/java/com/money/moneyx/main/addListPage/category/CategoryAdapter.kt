package com.money.moneyx.main.addListPage.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.ListCategoryBinding
import com.money.moneyx.main.addListPage.addExpends.GetAllCategoryExpenses
import com.money.moneyx.main.addListPage.addExpends.GetAllCategoryExpensesData
import com.money.moneyx.main.addListPage.addIncome.GetAllCategoryincomeData

class CategoryAdapter(
    private val getAllCategoryincome: List<GetAllCategoryincomeData>,
    private val myList: List<Pair<Int, String>>,
    private val callback: (Pair<String, Int>) -> Unit,
) :
    RecyclerView.Adapter<CategoryViewAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewAdapter {
        val list: ListCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_category,
            parent,
            false
        )
        return CategoryViewAdapter(list)
    }

    override fun getItemCount() = getAllCategoryincome.size

    override fun onBindViewHolder(holder: CategoryViewAdapter, position: Int) {
        holder.binding.textCategory.text = getAllCategoryincome[position].category
        val x = "R.drawable.${getAllCategoryincome[position].image}"
        if (getAllCategoryincome[position].image == myList[position].second){
            holder.binding.imageCategory.setImageResource(myList[position].first)
        }

        holder.itemView.setOnClickListener {
            callback.invoke(
                Pair(
                    getAllCategoryincome[position].category,
                    getAllCategoryincome[position].id
                )
            )

        }
    }
}

class CategoryViewAdapter(internal val binding: ListCategoryBinding) :
    RecyclerView.ViewHolder(binding.root)
//---------------------------------------------------------------------------------------------------//
class CategoryExpendsAdapter(
    private val getAllCategoryExpends: List<GetAllCategoryExpensesData>,
    private val myListExpends: List<Pair<Int, String>>,
    private val callback: (Pair<String, Int>) -> Unit,
) :
    RecyclerView.Adapter<CategoryExpendsViewAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryExpendsViewAdapter {
        val list: ListCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_category,
            parent,
            false
        )
        return CategoryExpendsViewAdapter(list)
    }

    override fun onBindViewHolder(holder: CategoryExpendsViewAdapter, position: Int) {
        holder.binding.textCategory.text = getAllCategoryExpends[position].category
        val x = "R.drawable.${getAllCategoryExpends[position].image}"
        if (getAllCategoryExpends[position].image == myListExpends[position].second){
            holder.binding.imageCategory.setImageResource(myListExpends[position].first)
        }

        holder.itemView.setOnClickListener {
            callback.invoke(
                Pair(
                    getAllCategoryExpends[position].category,
                    getAllCategoryExpends[position].id
                )
            )

        }
    }
    override fun getItemCount() = getAllCategoryExpends.size
}

class CategoryExpendsViewAdapter(internal val binding: ListCategoryBinding) :
    RecyclerView.ViewHolder(binding.root)