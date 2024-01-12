package com.money.moneyx.main.autoSave

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentAutoSaveBinding
import com.money.moneyx.databinding.FragmentIncomeExpendsBinding

class AutoSaveFragment : Fragment() {
    private lateinit var binding: FragmentAutoSaveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_auto_save,
            container,
            false
        )
        return binding.root
    }


}