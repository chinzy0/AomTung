package com.money.moneyx.main.incomeExpends.listPage.listYear

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentListYearBinding

class ListYearFragment : Fragment() {
    private lateinit var binding: FragmentListYearBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list_year,
            container,
            false
        )
        return binding.root
    }

}