package com.money.moneyx.main.autoSave

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.FragmentAutoSaveBinding
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.login.createPincode.CustomKeyboardModel
import com.money.moneyx.main.homeScreen.HomeActivity

class AutoSaveFragment : Fragment() {
    private lateinit var binding: FragmentAutoSaveBinding
    private lateinit var viewModel: AutoSaveViewModel
    private lateinit var autoSaveAdapter: AutoSaveAdapter
    private var idMember = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auto_save, container, false)
        viewModel = ViewModelProvider(this)[AutoSaveViewModel::class.java]
        binding.autoSaveViewModel = viewModel
        val preferences = Preference.getInstance(requireActivity())
        idMember = preferences.getInt("idmember", 0)


        adapterAutoSave()
        loadingScreen(requireActivity())
        return binding.root
    }
    private fun adapterAutoSave() {

        HomeActivity.listAutoSave?.data?.map { map ->
            viewModel.autoSaveModel.add(map)
        }

        autoSaveAdapter = AutoSaveAdapter(viewModel.autoSaveModel) { model ->

        }
        binding.RcvAutoSave.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = autoSaveAdapter
            autoSaveAdapter.notifyDataSetChanged()
        }
    }


}