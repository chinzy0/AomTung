package com.money.moneyx.main.addListPage.addIncome

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
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
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.Report
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


class AddIncomeFragment(private val editIncome: Report?) : Fragment() {
    private lateinit var binding: FragmentAddIncomeBinding
    private lateinit var viewModel: AddIncomeViewModel
    private var typeID = 0
    private var result = 0.0
    private var autoSaveID = 1
    private var categoryId = 0
    private var idMember = 0
    private var description = ""
    private var dateTimeSelected: Long = 0
    private var noteText = ""
    private var incomeID = 0
    private var edit = false


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


        setDateTime()
        editIncomeData()
        setEventClick()
        changeColorBtn()
        loadingScreen(requireActivity())





        return binding.root

    }

    private fun editIncomeData() {
        editIncome?.let { data ->
            result = editIncome.amount.toDouble()
            categoryId = editIncome.category_id
            typeID = editIncome.type_id
            description = data.description
            autoSaveID = editIncome.save_auto_id
            categoryId = editIncome.category_id
            description = editIncome.description
            incomeID = editIncome.transaction_id
            val localDateTime = unixTimestampToLocalDateTime(data.timestamp)
            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
            val formattedDate = localDateTime.format(dateFormat)
            val formattedTime = localDateTime.format(timeFormat)

            binding.textResult.setText(result.toString())
            binding.textTime2.text = data.type_name
            binding.textTime3.text = data.category_name
            binding.textTime44.text = data.description
            binding.textTime5.text = data.save_auto_name
            binding.textDate.text = formattedDate
            binding.textTime.text = formattedTime
        }
        if (editIncome != null) {
            edit = true
            if (description.isNotEmpty()) {
                binding.textTime4.visibility = View.GONE
                binding.textTime44.visibility = View.VISIBLE
                if (description.length > 15) {
                    val truncatedText = description.substring(0, 15) + "..."
                    binding.textTime44.text = truncatedText
                } else {
                    binding.textTime44.text = description
                }
                noteText = description
                description = description
            } else {
                binding.textTime44.text = ""
                binding.textTime4.visibility = View.VISIBLE
                binding.textTime44.visibility = View.GONE
                noteText = ""
            }
            binding.deleteButton.visibility = View.VISIBLE
        }else{
            binding.deleteButton.visibility = View.GONE
        }
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
                            formattedDate, binding.textTime.text.toString()
                        )
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
                    note(requireActivity(), noted = noteText, page = "income") { text ->
                        if (text.toString().isNotEmpty()) {
                            binding.textTime4.visibility = View.GONE
                            binding.textTime44.visibility = View.VISIBLE
                            if (text.toString().length > 15) {
                                val truncatedText = text.toString().substring(0, 15) + "..."
                                binding.textTime44.text = truncatedText
                            } else {
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
                    }else if(edit) {
                        AVLoading.startAnimLoading()
                        viewModel.updateIncome(
                            income_id = incomeID,
                            type_id = typeID,
                            category_id = categoryId,
                            description = description,
                            amount = result,
                            createdateTime = dateTimeSelected.toInt(),
                            auto_schedule = autoSaveID ){ updateIncome ->
                            AVLoading.stopAnimLoading()
                            if (updateIncome.data.is_Updated){
                                activity?.runOnUiThread { showSuccessDialog() }
                            }else{

                            }
                        }
                    } else {
                        AVLoading.startAnimLoading()
                        viewModel.createListIncome(
                            idmember = idMember,
                            idtype = typeID,
                            idcategory = categoryId,
                            description = description,
                            dateCreated = dateTimeSelected,
                            auto_schedule = autoSaveID,
                            amount = result
                        ) { model ->
                            AVLoading.stopAnimLoading()
                            if (model.success) {
                                activity?.runOnUiThread { showSuccessDialog() }
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
            private val decimalFormat = DecimalFormat("#.##")
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    AddListScreenActivity.textResult.value = s.toString()
                } else {
                    AddListScreenActivity.textResult.value = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    var resultValue = s.toString().toDouble()
                    resultValue =
                        BigDecimal(resultValue).setScale(2, RoundingMode.HALF_EVEN).toDouble()

                    if (resultValue > 0) {
                        val buttonColor = ContextCompat.getColor(requireContext(), R.color.income)
                        binding.buttonAddIncome.backgroundTintList =
                            ColorStateList.valueOf(buttonColor)
                        binding.buttonAddIncome.isEnabled = true
                        result = resultValue
                    } else {
                        val buttonColor =
                            ContextCompat.getColor(requireContext(), R.color.button_disable)
                        binding.buttonAddIncome.backgroundTintList =
                            ColorStateList.valueOf(buttonColor)
                        binding.buttonAddIncome.isEnabled = false
                    }
                } catch (e: NumberFormatException) {

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

    private fun showSuccessDialog() {

        val dialog = Dialog(requireActivity())
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.add_success_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val ok = dialog.findViewById<TextView>(R.id.addListSuccessButton)
        val text = dialog.findViewById<TextView>(R.id.textAlertAddlist)

        ok.setOnClickListener {
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun unixTimestampToLocalDateTime(unixTimestamp: Int): LocalDateTime {
        val instant = Instant.ofEpochSecond(unixTimestamp.toLong())
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }


}