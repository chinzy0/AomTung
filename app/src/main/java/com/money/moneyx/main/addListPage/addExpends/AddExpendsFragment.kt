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
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.FragmentAddExpendsBinding
import com.money.moneyx.function.addListAlertDialog
import com.money.moneyx.function.autoSave
import com.money.moneyx.function.dateTime
import com.money.moneyx.function.dateTimeExpends
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.function.note
import com.money.moneyx.function.selectType
import com.money.moneyx.function.selectTypeExpends
import com.money.moneyx.function.showTimePicker
import com.money.moneyx.main.addListPage.addIncome.AddIncomeAdapter
import com.money.moneyx.main.addListPage.calculator.CalculatorActivity
import com.money.moneyx.main.addListPage.category.CategoryExpendsActivity
import com.money.moneyx.main.addListPage.category.CategoryIncomeActivity
import com.money.moneyx.main.homeScreen.HomeActivity
import java.text.SimpleDateFormat
import java.util.Locale


class AddExpendsFragment : Fragment() {
    private lateinit var binding: FragmentAddExpendsBinding
    private lateinit var viewModel: AddExpendsViewModel
    private var typeID = 0
    private var autoSaveID = 1
    private var categoryId = 0
    private var idMember = 0
    private var description = ""
    private var dateTimeSelected: Long = 0
    private var noteText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_expends, container, false)
        viewModel = ViewModelProvider(this)[AddExpendsViewModel::class.java]
        binding.addExpendsViewModel = viewModel
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
                "expendsCalculateClick" -> {
                    val intent = Intent(requireActivity(), CalculatorActivity::class.java)
                    intent.putExtra("resultExpends", binding.textTv.text.toString())
                    resultActivityAppointment.launch(intent)
                }

                "expendsDateClick" -> {
                    dateTimeExpends(requireActivity()) { formattedDate ->
                        binding.textDate.text = formattedDate
                        dateTimeSelected = convertDateTimeToUnixTimestamp(
                            formattedDate,
                            binding.textTime.text.toString()
                        )
                    }
                }

                "expendsTimeClick" -> {
                    showTimePicker(requireActivity()) { formattedTime ->
                        binding.textTime.text = formattedTime
                        dateTimeSelected = convertDateTimeToUnixTimestamp(
                            binding.textDate.text.toString(),
                            formattedTime
                        )

                    }
                }

                "expendsTypeClick" -> {
                    selectTypeExpends(requireActivity(), HomeActivity.getAllTypeExpenses) { type ->
                        binding.textTime2.text = type.first
                        typeID = type.second
                    }
                }

                "expendsCategoryClick" -> {
                    selectCategory()
                }

                "expendsNoteClick" -> {
                    note(requireActivity(), noted = binding.textTime44.text.toString(), page = "expends") { text ->
                        if (text.toString().isNotEmpty()) {
                            binding.textTime4.visibility = View.GONE
                            binding.textTime44.visibility = View.VISIBLE
                            if (text.toString().length > 15){
                                val truncatedText = text.toString().substring(0, 15)+ "..."
                                binding.textTime44.text = truncatedText
                            }else{
                                binding.textTime44.text = text.toString()
                            }
                            noteText = text.toString()
                            description = text.toString()
                        } else {
                            binding.textTime44.text = ""
                            binding.textTime4.visibility = View.VISIBLE
                            binding.textTime44.visibility = View.GONE
                            noteText = ""
                        }
                    }
                }

                "expendsAutoSaveClick" -> {
                    autoSave(requireActivity(), HomeActivity.listScheduleAuto) { autoSaved ->
                        binding.textTime5.text = autoSaved.first
                        autoSaveID = autoSaved.second
                    }
                }

                "expendsSaveClick" -> {
                    dateTimeSelected = convertDateTimeToUnixTimestamp(
                        binding.textDate.text.toString(),
                        binding.textTime.text.toString()
                    )
                    if (typeID == 0 || categoryId == 0) {
                        addListAlertDialog(requireActivity())
                    } else {
                        AVLoading.startAnimLoading()
                        viewModel.createListExpenses(
                            idmember = idMember,
                            idtype = typeID,
                            idcategory = categoryId,
                            description = description,
                            dateCreated = dateTimeSelected,
                            auto_schedule = autoSaveID,
                            amount = binding.textTv.text.toString().toDouble()
                        ) { model ->
                            if (model.success) {
                                AVLoading.stopAnimLoading()
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


    private val resultActivityAppointment =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
                    val categoryID = data.getIntExtra("Category_Id", 0)
                    categoryId = categoryID
                    binding.textTime3.text = categorySelected
                }
            }
        }


    private fun selectCategory() {
        val intent = Intent(requireActivity(), CategoryExpendsActivity::class.java)
        getCategory.launch(intent)
    }

    private fun changeColorBtn() {
        binding.textTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val resultValue = s.toString().toDoubleOrNull() ?: 0.0
                if (resultValue > 0) {
                    val buttonColor = ContextCompat.getColor(requireContext(), R.color.button)
                    binding.buttonAddExpends.backgroundTintList =
                        ColorStateList.valueOf(buttonColor)
                    binding.buttonAddExpends.isEnabled = true
                } else {
                    val buttonColor =
                        ContextCompat.getColor(requireContext(), R.color.button_disable)
                    binding.buttonAddExpends.backgroundTintList =
                        ColorStateList.valueOf(buttonColor)
                    binding.buttonAddExpends.isEnabled = false
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

    private fun setDateTime() {
        binding.textTime.text = viewModel.time
        binding.textDate.text = viewModel.date

    }


}