package com.money.moneyx.main.addListPage.addExpends

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentAddExpendsBinding
import com.money.moneyx.function.autoSave
import com.money.moneyx.function.dateTime
import com.money.moneyx.function.note
import com.money.moneyx.function.selectType
import com.money.moneyx.function.showTimePicker
import com.money.moneyx.main.addListPage.addIncome.AddIncomeAdapter
import com.money.moneyx.main.addListPage.calculator.CalculatorActivity
import com.money.moneyx.main.addListPage.category.CategoryIncomeActivity


class AddExpendsFragment : Fragment() {
    private lateinit var binding: FragmentAddExpendsBinding
    private lateinit var addIncomeAdapter: AddIncomeAdapter
    private lateinit var viewModel: AddExpendsViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_expends,container,false)
        viewModel = ViewModelProvider(this)[AddExpendsViewModel::class.java]
        binding.addExpendsViewModel = viewModel

        viewModel.setDataAdapter("")
        addAdapter()
        setEventClick()
        changeColorBtn()

        return binding.root
    }

    private fun setEventClick() {
        viewModel.onClick.observe(requireActivity(), Observer {
            when (it) {
                "calculateClick" -> {
                    val intent = Intent(requireActivity(), CalculatorActivity::class.java)
                    intent.putExtra("resultExpends",binding.textTv.text.toString())
                    resultActivityAppointment.launch(intent)
                }
            }
        })
    }

    private fun addAdapter(){
        addIncomeAdapter = AddIncomeAdapter(viewModel.addIncomeModel) {
            when(it.first) {
                "วันที่ เวลา" -> {
                    when (it.second) {
                        "date" -> {
                            dateTime(requireActivity()) { formattedDate ->
                                Log.i("Income Date", formattedDate)
                            }
                        }
                        "time" -> {
                            dateTime(requireActivity()) { formattedDate ->

                            }
                        }
                        else -> {
                        }
                    }

                }
                "ประเภท" -> {
//                    selectType(requireActivity())
                }
                "หมวดหมู่" -> {
//                    selectCategory()
                }
                "โน้ต" -> {
//                    note(requireActivity())
                }
                "บันทึกอัตโนมัติ" -> {
//                    autoSave(requireActivity())
                }
            }
        }
        binding.RCVincome.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = addIncomeAdapter
            addIncomeAdapter.notifyDataSetChanged()
        }
    }

    private val resultActivityAppointment = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val x = data.getStringExtra("number").toString()
                    binding.textTv.setText(x)
                }
            }
        }

    private val getCategory =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val categorySelected = data.getStringExtra("Category").toString()
                    Log.i("categorySelected",categorySelected)
                    viewModel.addIncomeModel.clear()
                    viewModel.setDataAdapter(categorySelected)
                    addAdapter()
                }
            }
        }


    private fun selectCategory(){
        val intent = Intent(requireActivity(), CategoryIncomeActivity::class.java)
        getCategory.launch(intent)
    }
    private fun changeColorBtn(){
        binding.textTv.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()){
                    val buttonColor = ContextCompat.getColor(requireContext(), R.color.expends)
                    binding.buttonAddIncome.backgroundTintList = ColorStateList.valueOf(buttonColor)
                    binding.buttonAddIncome.isEnabled = true
                }else{
                    val buttonColor = ContextCompat.getColor(requireContext(), R.color.button_disable)
                    binding.buttonAddIncome.backgroundTintList = ColorStateList.valueOf(buttonColor)
                    binding.buttonAddIncome.isEnabled = false
                }
            }

        })
    }




}