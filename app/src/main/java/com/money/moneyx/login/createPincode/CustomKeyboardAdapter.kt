package com.money.moneyx.login.createPincode

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.CustomKeyboardBinding

class CustomKeyboardAdapter(
    private val customKeyboardModel: ArrayList<CustomKeyboardModel>, private val clickListener: (String) -> Unit) : RecyclerView.Adapter<CustomKeyboardViewAdapter>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomKeyboardViewAdapter {
        val custom : CustomKeyboardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.custom_keyboard,
            parent,
false
        )
        return CustomKeyboardViewAdapter(custom)
    }

    override fun getItemCount() = customKeyboardModel.size

    override fun onBindViewHolder(holder: CustomKeyboardViewAdapter, position: Int){
        holder.binding.textView7.text = customKeyboardModel[position].keyboardCustom


        when (position) {
            9 -> {
                holder.binding.body.visibility = View.GONE
            }
            11 -> {
                holder.binding.textView7.visibility = View.GONE
                holder.binding.imageView3.visibility = View.VISIBLE
            }
        }


        holder.itemView.setOnClickListener {
            var number = customKeyboardModel[position].keyboardCustom
            clickListener.invoke(number)
        }




    }

}




class CustomKeyboardViewAdapter (internal val binding: CustomKeyboardBinding) :
    RecyclerView.ViewHolder(binding.root)