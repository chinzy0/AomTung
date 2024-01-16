package com.money.moneyx.main.addListPage.addIncome

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentAddIncomeBinding
import com.money.moneyx.function.autoSave
import com.money.moneyx.function.dateTime
import com.money.moneyx.function.note
import com.money.moneyx.function.selectType
import com.money.moneyx.function.showTimePicker
import com.money.moneyx.main.addListPage.category.CategoryIncomeActivity
import com.money.moneyx.main.addListPage.calculator.CalculatorActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale



class AddIncomeFragment : Fragment() {
    private lateinit var binding: FragmentAddIncomeBinding
    private lateinit var addIncomeAdapter: AddIncomeAdapter
    private lateinit var viewModel: AddIncomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_income,container,false)
        viewModel = ViewModelProvider(this)[AddIncomeViewModel::class.java]
        binding.addIncomeViewModel = viewModel

        viewModel.setDataAdapter("")
        addAdapter()
        setEventClick()

        return binding.root
    }


    private fun setEventClick() {
        viewModel.onClick.observe(requireActivity(), Observer {
            when (it) {
                "calculateClick" -> {
                    val intent = Intent(requireActivity(), CalculatorActivity::class.java)
                    intent.putExtra("status", "true")
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
                            dateTime(requireActivity())
                        }
                        "time" -> {
                            showTimePicker(requireActivity())
                        }
                        else -> {
                        }
                    }

                }
                "ประเภท" -> {
                    selectType(requireActivity())
                }
                "หมวดหมู่" -> {
                    selectCategory()
                }
                "โน้ต" -> {
                    note(requireActivity())
                }
                "บันทึกอัตโนมัติ" -> {
                    autoSave(requireActivity())
                }
            }
        }
        binding.RCVincome.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = addIncomeAdapter
            addIncomeAdapter.notifyDataSetChanged()
        }
    }

    private val resultActivityAppointment =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val x = data.getIntExtra("number",0).toString()
                    binding.textView14.setText(x)
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




}