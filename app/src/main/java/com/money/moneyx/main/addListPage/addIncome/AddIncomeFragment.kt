package com.money.moneyx.main.addListPage.addIncome

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentAddIncomeBinding
import com.money.moneyx.function.autoSave
import com.money.moneyx.function.dateTime
import com.money.moneyx.function.note
import com.money.moneyx.function.selectType
import com.money.moneyx.function.showTimePicker
import com.money.moneyx.main.addListPage.calculator.CalculatorActivity
import com.money.moneyx.main.addListPage.category.CategoryIncomeActivity
import com.money.moneyx.main.homeScreen.HomeActivity


class AddIncomeFragment : Fragment() {
    private lateinit var binding: FragmentAddIncomeBinding
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

        setEventClick()
        changeColorBtn()
        setDateTime()


        return binding.root

    }


    private fun setEventClick() {
        viewModel.onClick.observe(requireActivity(), Observer {
            when (it) {
                "calculateClick" -> {
                    val intent = Intent(requireActivity(), CalculatorActivity::class.java)
                    intent.putExtra("status", "true")
                    intent.putExtra("resultIncome",binding.textResult.text.toString())
                    resultActivityAppointment.launch(intent)
                }
                "incomeDateClick" -> {
                    dateTime(requireActivity()) { formattedDate ->
                        binding.textDate.text = formattedDate
                    }
                }
                "incomeTimeClick" -> {
                    showTimePicker(requireActivity()) { formattedTime ->
                        binding.textTime.text = formattedTime
                    }
                }
                "incomeTypeClick" -> {
                    selectType(requireActivity(), HomeActivity.getAllTypeIncomeData){

                    }
                }
                "incomeCategoryClick" -> {
                    selectCategory()
                }
                "incomeNoteClick" -> {
                    note(requireActivity()){ noted ->
                        binding.textTime4.text = noted
                    }
                }
                "incomeAutoSaveClick" -> {
                    autoSave(requireActivity()){ autoSaved ->
                        binding.textTime5.text = autoSaved
                    }
                }
                "incomeSaveClick" -> {
                }
            }
        })
    }
    private fun setDateTime(){
        binding.textTime.text = viewModel.time
        binding.textDate.text = viewModel.date

    }




    private val resultActivityAppointment =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val x = data.getStringExtra("number").toString()
                    binding.textResult.setText(x)
                }
            }
        }
    private val getCategory =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val categorySelected = data.getStringExtra("Category").toString()
                    Log.i("categorySelected",categorySelected)
                    binding.textTime3.text = categorySelected
                }
            }
        }


    private fun selectCategory(){
        val intent = Intent(requireActivity(), CategoryIncomeActivity::class.java)
        getCategory.launch(intent)
    }

    private fun changeColorBtn(){
        binding.textResult.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()){
                    val buttonColor = ContextCompat.getColor(requireContext(), R.color.income)
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