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
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.FragmentAddIncomeBinding
import com.money.moneyx.function.addListAlertDialog
import com.money.moneyx.function.autoSave
import com.money.moneyx.function.dateTime
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.function.note
import com.money.moneyx.function.selectType
import com.money.moneyx.function.showTimePicker
import com.money.moneyx.main.addListPage.AddListScreenActivity
import com.money.moneyx.main.addListPage.calculator.CalculatorActivity
import com.money.moneyx.main.addListPage.category.CategoryIncomeActivity
import com.money.moneyx.main.homeScreen.HomeActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class AddIncomeFragment : Fragment() {
    private lateinit var binding: FragmentAddIncomeBinding
    private lateinit var viewModel: AddIncomeViewModel
    private var typeID = 0
    private var autoSaveID = 1
    private var categoryId = 0
    private var idMember = 0
    private var description = ""
    private var dateTimeSelected: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_income, container, false)
        viewModel = ViewModelProvider(this)[AddIncomeViewModel::class.java]
        binding.addIncomeViewModel = viewModel
        val preferences = Preference.getInstance(requireActivity())
        idMember = preferences.getInt("idmember", 0)
        Log.i("idMember", idMember.toString())

        setEventClick()
        changeColorBtn()
        setDateTime()
        loadingScreen(requireActivity())


        return binding.root

    }


    private fun setEventClick() {

        viewModel.onClick.observe(requireActivity(), Observer {
            when (it) {
                "calculateClick" -> {
                    val intent = Intent(requireActivity(), CalculatorActivity::class.java)
                    intent.putExtra("status", "true")
                    intent.putExtra("resultIncome", binding.textResult.text.toString())
                    resultActivityAppointment.launch(intent)
                }

                "incomeDateClick" -> {
                    dateTime(requireActivity()) { formattedDate ->
                        binding.textDate.text = formattedDate
                        dateTimeSelected = convertDateTimeToUnixTimestamp(
                            formattedDate, binding.textTime.text.toString())
                    }
                }

                "incomeTimeClick" -> {
                    showTimePicker(requireActivity()) { formattedTime ->
                        binding.textTime.text = formattedTime
                        dateTimeSelected = convertDateTimeToUnixTimestamp(
                            binding.textDate.text.toString(),
                            formattedTime
                        )

                    }
                }

                "incomeTypeClick" -> {
                    selectType(requireActivity(), HomeActivity.getAllTypeIncomeData) { type ->
                        binding.textTime2.text = type.first
                        typeID = type.second
                    }
                }

                "incomeCategoryClick" -> {
                    selectCategory()
                }

                "incomeNoteClick" -> {
                    note(requireActivity(), noted = binding.textTime44.text.toString()) { text ->
                        if (text.toString().isNotEmpty()) {
                            binding.textTime4.visibility = View.GONE
                            binding.textTime44.visibility = View.VISIBLE
                            binding.textTime44.text = text.toString()
                            description = text.toString()
                        } else {
                            binding.textTime44.text = ""
                            binding.textTime4.visibility = View.VISIBLE
                            binding.textTime44.visibility = View.GONE
                        }
                    }
                }

                "incomeAutoSaveClick" -> {
                    autoSave(requireActivity(), HomeActivity.listScheduleAuto) { autoSaved ->
                        binding.textTime5.text = autoSaved.first
                        autoSaveID = autoSaved.second
                    }
                }

                "incomeSaveClickButton" -> {
                    dateTimeSelected = convertDateTimeToUnixTimestamp(
                        binding.textDate.text.toString(),
                        binding.textTime.text.toString()
                    )
                    if (typeID == 0 || categoryId == 0) {
                        addListAlertDialog(requireActivity())
                    } else {
                        AVLoading.startAnimLoading()
                        viewModel.createListIncome(
                            idmember = idMember,
                            idtype = typeID,
                            idcategory = categoryId,
                            description = description,
                            dateCreated = dateTimeSelected,
                            auto_schedule = autoSaveID,
                            amount = binding.textResult.text.toString().toDouble()
                        ) { model ->
                            AVLoading.stopAnimLoading()
                            if (model.success) {
                                val intent = Intent(requireActivity(), HomeActivity::class.java)
                                startActivity(intent)
                            } else {

                            }

                        }
                    }
                }
            }
        })
    }


    private fun setDateTime() {
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
                    val categoryID = data.getIntExtra("Category_Id", 0)
                    categoryId = categoryID
                    binding.textTime3.text = categorySelected
                }
            }
        }


    private fun selectCategory() {
        val intent = Intent(requireActivity(), CategoryIncomeActivity::class.java)
        getCategory.launch(intent)
    }

    private fun changeColorBtn() {
        binding.textResult.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val resultValue = s.toString().toDoubleOrNull() ?: 0.0
                if (resultValue > 0) {
                    val buttonColor = ContextCompat.getColor(requireContext(), R.color.income)
                    binding.buttonAddIncome.backgroundTintList = ColorStateList.valueOf(buttonColor)
                    binding.buttonAddIncome.isEnabled = true
                } else {
                    val buttonColor =
                        ContextCompat.getColor(requireContext(), R.color.button_disable)
                    binding.buttonAddIncome.backgroundTintList = ColorStateList.valueOf(buttonColor)
                    binding.buttonAddIncome.isEnabled = false
                }
            }

        })
    }


    private fun convertDateTimeToUnixTimestamp(formattedDate: String, formattedTime: String): Long {
        val dateTimeString = "$formattedDate $formattedTime:00"

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())


        try {
            val date = dateFormat.parse(dateTimeString)
            return date?.time!! / 1000L
        } catch (e: Exception) {

            e.printStackTrace()
        }

        return 0L
    }


}