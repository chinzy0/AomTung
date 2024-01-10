package com.money.moneyx.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.ListProfileBinding

class ProfileAdapter(private val profileModel: ArrayList<ProfileModel>, private val callback: (String) -> Unit) : RecyclerView.Adapter<ProfileViewAdapter>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewAdapter {
        val menu : ListProfileBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_profile,
            parent,
            false
        )
        return ProfileViewAdapter(menu)
    }

    override fun getItemCount() = profileModel.size

    override fun onBindViewHolder(holder: ProfileViewAdapter, position: Int) {
        holder.binding.textMenu.text = profileModel[position].menu
        holder.binding.imageIcon.setImageResource(profileModel[position].icon)
        holder.itemView.setOnClickListener{
            callback.invoke(profileModel[position].menu,)
        }
    }

}

class ProfileViewAdapter (internal val binding:ListProfileBinding):
        RecyclerView.ViewHolder(binding.root)
