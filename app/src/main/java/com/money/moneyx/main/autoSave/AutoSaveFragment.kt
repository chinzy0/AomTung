package com.money.moneyx.main.autoSave

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.FragmentAutoSaveBinding
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.function.showExitDialog
import com.money.moneyx.main.addListPage.AddListScreenActivity
import com.money.moneyx.main.homeScreen.HomeActivity

class AutoSaveFragment : Fragment() {
    private lateinit var binding: FragmentAutoSaveBinding
    private lateinit var viewModel: AutoSaveViewModel
    private lateinit var autoSaveAdapter: AutoSaveAdapter

    private var idMember = 0


    override fun onStart() {
        super.onStart()
        activity?.onBackPressedDispatcher?.addCallback {

        }
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

        dropdownAutoSave()
        loadingScreen(requireActivity())
        return binding.root
    }

    private fun dropdownAutoSave() {
        val spinnerData = listOf("ทั้งหมด", "ทุกวัน", "ทุกสัปดาห์", "ทุกเดือน", "ทุก3เดือน")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerData
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dropdown.setOnClickListener {
            binding.spinnerAutoSave.performClick()
        }
        binding.spinnerAutoSave.adapter = adapter

        binding.spinnerAutoSave.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                adapterAutoSave(spinnerData[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    private fun adapterAutoSave(s: String?= null) {
        viewModel.autoSaveModel.clear()
        HomeActivity.listAutoSave?.data?.map { map ->
                when (s) {
                    map.save_auto_name -> { viewModel.autoSaveModel.add(map) }
                    else -> {
                        if (s == "ทั้งหมด") {
                            viewModel.autoSaveModel.add(map)
                        } else {

                        }
                    }
                }

        }
        autoSaveAdapter = AutoSaveAdapter(viewModel.autoSaveModel) { model ->
            when(model.first){
                model.first -> {
                    var intent = Intent(requireActivity(), AddListScreenActivity:: class.java)
                    if (model.second == "income"){
                        intent.putExtra("edit", "IncomeAutoSave")
                    }else{
                        intent.putExtra("edit", "ExpensesAutoSave")
                    }
                    intent.putExtra("AutoSave", model.third)
                    startActivity(intent)
                }
            }
        }
        binding.RcvAutoSave.apply {
            adapter = autoSaveAdapter
            layoutManager = LinearLayoutManager(context)
            autoSaveAdapter.notifyDataSetChanged()
        }
    }
}