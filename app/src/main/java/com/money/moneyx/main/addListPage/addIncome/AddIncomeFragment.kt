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
import androidx.lifecycle.MutableLiveData
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
import com.money.moneyx.function.showConfirmDeleteDialog
import com.money.moneyx.function.showTimePicker
import com.money.moneyx.main.addListPage.AddListScreenActivity
import com.money.moneyx.main.addListPage.calculator.CalculatorActivity
import com.money.moneyx.main.addListPage.category.CategoryIncomeActivity
import com.money.moneyx.main.autoSave.GetListAutoData
import com.money.moneyx.main.homeScreen.HomeActivity
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.Report
import com.money.moneyx.main.incomeExpends.summary.ReportALL
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


class AddIncomeFragment(
    private val editIncome: Report?,
    private val editAutoSave: GetListAutoData?,
    private val incomeExpends: ReportALL?,
) : Fragment() {
    private lateinit var binding: FragmentAddIncomeBinding
    private lateinit var viewModel: AddIncomeViewModel
    private var typeID = 0
    private var result = 0.0
    private var autoSaveID = 0
    private var categoryId = 0
    private var idMember = 0
    private var description = ""
    private var dateTimeSelected: Long = 0
    private var noteText = ""
    private var incomeID = 0
    private lateinit var currentDate: LocalDate
    private lateinit var listDate: LocalDate
    private var formattedDate = ""
    private var formattedTime = ""
    private var autoSaveName = ""
    private var edit = false
    private val onClickDialog = MutableLiveData<String>()





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_income, container, false)
        viewModel = ViewModelProvider(this)[AddIncomeViewModel::class.java]
        binding.addIncomeViewModel = viewModel
        val preferences = Preference.getInstance(requireActivity())
        idMember = preferences.getInt("idmember", 0)
        Log.i("IdMember", idMember.toString())
        currentDate = LocalDate.now()

        AddListScreenActivity.change.value = ""


        setDateTime()
        editIncomeData()
        changeColorBtn()
        setEventClick()
        loadingScreen(requireActivity())





        return binding.root

    }


    private fun editIncomeData() {
        editAutoSave?.let { model ->
            val editAutoSaveAmount = editAutoSave?.amount?.replace(",", "")
            result = editAutoSaveAmount!!.toDouble()
            categoryId = model.category_id
            typeID = model.type_id
            description = model.description
            autoSaveID = model.save_auto_id
            description = model.description
            incomeID = model.transaction_id

            val localDateTime = unixTimestampToLocalDateTime(model.timestamp)
            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            listDate = unixTimestampToLocalDate(model.timestamp)
            Log.i("LocalDate", listDate.toString())
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
            formattedDate = localDateTime.format(dateFormat)
            formattedTime = localDateTime.format(timeFormat)

            binding.textResult.setText(editAutoSaveAmount)
            binding.textTime2.text = model.type_name
            binding.textTime3.text = model.category_name
            binding.textTime44.text = model.description
            binding.textTime5.text = model.save_auto_name
            binding.textDate.text = formattedDate
            binding.textTime.text = formattedTime
        }
        incomeExpends?.let { income ->
            val amount = income.amount.replace(",", "")
            result = amount.toDouble()
            categoryId = income.category_id
            typeID = income.type_id
            description = income.description
            autoSaveID = income.save_auto_id
            description = income.description
            incomeID = income.transaction_id

            val localDateTime = unixTimestampToLocalDateTime(income.timestamp)
            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            listDate = unixTimestampToLocalDate(income.timestamp)
            Log.i("LocalDate", listDate.toString())
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
            formattedDate = localDateTime.format(dateFormat)
            formattedTime = localDateTime.format(timeFormat)

            binding.textResult.setText(amount)
            binding.textTime2.text = income.type_name
            binding.textTime3.text = income.category_name
            binding.textTime44.text = income.description
            binding.textTime5.text = income.save_auto_name
            binding.textDate.text = formattedDate
            binding.textTime.text = formattedTime
        }
        editIncome?.let { data ->
            val amount = data.amount.replace(",", "")
            result = amount.toDouble()
            categoryId = data.category_id
            typeID = data.type_id
            description = data.description
            autoSaveID = data.save_auto_id
            description = data.description
            incomeID = data.transaction_id

            val localDateTime = unixTimestampToLocalDateTime(data.timestamp)
            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            listDate = unixTimestampToLocalDate(data.timestamp)
            Log.i("LocalDate", listDate.toString())
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
            formattedDate = localDateTime.format(dateFormat)
            formattedTime = localDateTime.format(timeFormat)

            binding.textResult.setText(amount)
            binding.textTime2.text = data.type_name
            binding.textTime3.text = data.category_name
            binding.textTime44.text = data.description
            binding.textTime5.text = data.save_auto_name
            binding.textDate.text = formattedDate
            binding.textTime.text = formattedTime
        }
        if (editIncome != null || editAutoSave != null || incomeExpends != null) {
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
                description = ""
            }
            if (editIncome?.save_auto_id != 1 || editAutoSave?.save_auto_id != 1 || incomeExpends?.save_auto_id != 1) {
                binding.autosaveButton.isEnabled = false
                val textColor = ContextCompat.getColor(requireActivity(), R.color.disable)
                binding.textTime5.setTextColor(textColor)
                binding.title5.setTextColor(textColor)
                binding.img55.visibility = View.VISIBLE
                binding.detail55.visibility = View.VISIBLE
                if (listDate.isBefore(currentDate)) {
                    binding.autosaveButton.isEnabled = false
                    val textColor = ContextCompat.getColor(requireActivity(), R.color.disable)
                    binding.textTime5.setTextColor(textColor)
                    binding.title5.setTextColor(textColor)
                    binding.img55.visibility = View.VISIBLE
                    binding.detail55.visibility = View.VISIBLE
                } else {
                    val textColor = ContextCompat.getColor(requireActivity(), R.color.black)
                    binding.textTime5.setTextColor(textColor)
                    binding.title5.setTextColor(textColor)
                    binding.autosaveButton.isEnabled = true
                    binding.img55.visibility = View.GONE
                    binding.detail55.visibility = View.GONE
                    binding.detail11.visibility = View.GONE
                    binding.img11.visibility = View.GONE
                    binding.textDate.isEnabled = true
                    binding.textTime.isEnabled = true
                    binding.textTime5.setTextColor(textColor)
                    binding.title5.setTextColor(textColor)
                    binding.textTime.setTextColor(textColor)
                    binding.textDate.setTextColor(textColor)
                    binding.title.setTextColor(textColor)
                }
            } else {
                if (listDate.isBefore(currentDate)) {
                    binding.autosaveButton.isEnabled = false
                    val textColor = ContextCompat.getColor(requireActivity(), R.color.disable)
                    binding.textTime5.setTextColor(textColor)
                    binding.title5.setTextColor(textColor)
                    binding.img55.visibility = View.VISIBLE
                    binding.detail55.visibility = View.VISIBLE
                    autoSaveID = 1
                } else {
                    val textColor = ContextCompat.getColor(requireActivity(), R.color.black)
                    binding.textTime5.setTextColor(textColor)
                    binding.title5.setTextColor(textColor)
                    binding.autosaveButton.isEnabled = true
                    binding.img55.visibility = View.GONE
                    binding.detail55.visibility = View.GONE
                }
            }
            binding.deleteButton.visibility = View.VISIBLE
        } else {
            binding.deleteButton.visibility = View.GONE
        }

    }


    private fun setEventClick() {
        onClickDialog.observe(requireActivity(), Observer {
            when (it) {
                "confirmDelete" -> {
                    viewModel.deleteIncome(income_id = incomeID) { del ->
                        if (del.success) {
                            val intent = Intent(requireActivity(), HomeActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        })

        viewModel.onClick.observe(requireActivity(), Observer {
            when (it) {
                "calculateClick" -> {
                    val intent = Intent(requireActivity(), CalculatorActivity::class.java)
                    intent.putExtra("status", "true")
                    intent.putExtra("resultIncome", binding.textResult.text.toString())
                    resultActivityAppointment.launch(intent)
                }

                "incomeDateClick" -> {
                    dateTime(requireActivity(), binding.textDate.text.toString()) { formatted ->
                        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        listDate = LocalDate.parse(formatted, dateFormat)
                        binding.textDate.text = formatted
                        if (edit){
                            val textColor = ContextCompat.getColor(requireActivity(), R.color.disable)
                            binding.textTime5.setTextColor(textColor)
                            binding.title5.setTextColor(textColor)
                            binding.img55.visibility = View.VISIBLE
                            binding.detail55.visibility = View.VISIBLE
                            if (formattedDate != binding.textDate.text) {
                                binding.autosaveButton.isEnabled = false
                                binding.textTime5.text = "ไม่มี"
                                val textColor = ContextCompat.getColor(requireActivity(), R.color.disable)
                                binding.textTime5.setTextColor(textColor)
                                binding.title5.setTextColor(textColor)
                                binding.img55.visibility = View.VISIBLE
                                binding.detail55.visibility = View.VISIBLE
                                autoSaveID = 1
                            }else{

                            }
                        }else{
                            if (listDate.isBefore(currentDate)) {
                                binding.autosaveButton.isEnabled = false
                                binding.textTime5.text = "ไม่มี"
                                val textColor = ContextCompat.getColor(requireActivity(), R.color.disable)
                                binding.textTime5.setTextColor(textColor)
                                binding.title5.setTextColor(textColor)
                                binding.img55.visibility = View.VISIBLE
                                binding.detail55.visibility = View.VISIBLE
                                autoSaveID = 1
                            } else {
                                val textColor = ContextCompat.getColor(requireActivity(), R.color.black)
                                binding.textTime5.setTextColor(textColor)
                                binding.title5.setTextColor(textColor)
                                binding.autosaveButton.isEnabled = true
                                binding.img55.visibility = View.GONE
                                binding.detail55.visibility = View.GONE
                            }
                            dateTimeSelected = convertDateTimeToUnixTimestamp(
                                formattedDate,
                                binding.textTime.text.toString()
                            )
                            updateData()
                        }

                    }
                }

                "incomeTimeClick" -> {
                    showTimePicker(
                        requireActivity(),
                        binding.textTime.text.toString()
                    ) { formatted ->
                        binding.textTime.text = formatted
                        dateTimeSelected = convertDateTimeToUnixTimestamp(
                            binding.textDate.text.toString(),
                            formatted
                        )
                        updateData()
                    }
                }

                "incomeTypeClick" -> {
                    selectType(requireActivity(), HomeActivity.getAllTypeIncomeData) { type ->
                        binding.textTime2.text = type.first
                        typeID = type.second
                        updateData()
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
                            description = ""
                        }
                        updateData()
                    }
                }

                "incomeAutoSaveClick" -> {
                    autoSave(requireActivity(), HomeActivity.listScheduleAuto) { autoSaved ->
                        binding.textTime5.text = autoSaved.first
                        autoSaveID = autoSaved.second
                        updateData()
                    }
                }

                "incomeDeleteClick" -> {
                    showConfirmDeleteDialog(requireActivity(), onClickDialog)
                }

                "incomeSaveClickButton" -> {
                    dateTimeSelected = convertDateTimeToUnixTimestamp(
                        binding.textDate.text.toString(),
                        binding.textTime.text.toString()
                    )
                    if (typeID == 0 || categoryId == 0) {
                        if (typeID == 0) {
                            binding.textTime2.setTextColor(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.red
                                )
                            )
                        } else {
                            binding.textTime2.setTextColor(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.black
                                )
                            )
                        }
                        if (categoryId == 0) {
                            binding.textTime3.setTextColor(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.red
                                )
                            )
                        } else {
                            binding.textTime3.setTextColor(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.black
                                )
                            )
                        }
                        addListAlertDialog(requireActivity(),"income")
                    } else if (edit) {
                        AVLoading.startAnimLoading()
                        viewModel.updateIncome(
                            income_id = incomeID,
                            type_id = typeID,
                            category_id = categoryId,
                            description = description,
                            amount = result,
                            createdateTime = dateTimeSelected.toInt(),
                            auto_schedule = autoSaveID
                        ) { updateIncome ->
                            AVLoading.stopAnimLoading()
                            if (updateIncome.data.is_Updated) {
                                activity?.runOnUiThread { showSuccessDialog() }
                            } else {
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

    private fun updateData() {
        if (edit && binding.textResult.text.toString().toDouble() > 0) {
            if (editIncome != null) {
                val amount = editIncome.amount.replace(",", "")
                if (binding.textDate.text != formattedDate || binding.textResult.text.toString()
                        .toDouble() != amount.toDouble()
                    || binding.textTime.text != formattedTime || typeID != editIncome.type_id || categoryId != editIncome.category_id
                    || description != editIncome.description || autoSaveID != editIncome.save_auto_id
                ) {
                    enableBtn()
                } else {
                    unableBtn()
                }
            } else if (editAutoSave != null) {
                val amount = editAutoSave.amount.replace(",", "")
                if (binding.textDate.text != formattedDate || binding.textResult.text.toString()
                        .toDouble() != amount.toDouble()
                    || binding.textTime.text != formattedTime || typeID != editAutoSave.type_id || categoryId != editAutoSave.category_id
                    || description != editAutoSave.description || autoSaveID != editAutoSave.save_auto_id
                ) {
                    enableBtn()
                } else {
                    unableBtn()
                }
            } else if (incomeExpends != null) {
                val amount = incomeExpends.amount.replace(",", "")
                if (binding.textDate.text != formattedDate || binding.textResult.text.toString()
                        .toDouble() != amount.toDouble()
                    || binding.textTime.text != formattedTime || typeID != incomeExpends.type_id || categoryId != incomeExpends.category_id
                    || description != incomeExpends.description || autoSaveID != incomeExpends.save_auto_id
                ) {
                    enableBtn()
                } else {
                    unableBtn()
                }
            }
        }
        checkHasChanged()
    }

    private fun checkHasChanged() {
        if (result > 0 || categoryId != 0 || typeID != 0 || description != "" || autoSaveID != 0 || binding.textTime.text != viewModel.time
            || binding.textDate.text != viewModel.date
        ) {
            AddListScreenActivity.change.value = "HasChanged"
        } else {
            AddListScreenActivity.change.value = ""
        }
    }


    private fun setDateTime() {
        binding.textTime.text = viewModel.time
        binding.textDate.text = viewModel.date

    }

    private fun enableBtn() {
        val buttonColor = ContextCompat.getColor(requireContext(), R.color.income)
        binding.buttonAddIncome.backgroundTintList = ColorStateList.valueOf(buttonColor)
        binding.buttonAddIncome.isEnabled = true
        AddListScreenActivity.change.value = "HasChanged"
    }

    private fun unableBtn() {
        val buttonColor = ContextCompat.getColor(requireContext(), R.color.button_disable)
        binding.buttonAddIncome.backgroundTintList = ColorStateList.valueOf(buttonColor)
        binding.buttonAddIncome.isEnabled = false
        AddListScreenActivity.change.value = ""
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
                    updateData()
                }
            }
        }


    private fun selectCategory() {
        val intent = Intent(requireActivity(), CategoryIncomeActivity::class.java)
        getCategory.launch(intent)
    }

    private fun changeColorBtn() {
        binding.textResult.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    AddListScreenActivity.textResult.value = s.toString()
                    result = 0.0
                } else {
                    AddListScreenActivity.textResult.value = s.toString()
                    try {
                        var resultValue = s.toString().toDouble()
                        if (resultValue > 0) {
                            val buttonColor =
                                ContextCompat.getColor(requireContext(), R.color.income)
                            binding.buttonAddIncome.backgroundTintList =
                                ColorStateList.valueOf(buttonColor)
                            binding.buttonAddIncome.isEnabled = true
                            if (s!!.length > 13) {
                                val truncatedText = s.toString().substring(0, 13)
                                binding.textResult.setText(truncatedText)
                                binding.textResult.setSelection(truncatedText.length)
                            }
                            val indexOfDot = s.indexOf('.')
                            if (indexOfDot != -1 && s.length - indexOfDot > 3) {
                                val truncatedDecimal = s.substring(0, indexOfDot + 3)
                                binding.textResult.setText(truncatedDecimal)
                                binding.textResult.setSelection(truncatedDecimal.length)
                            }
                            if (!s.contains('.')) {
                                if (s.length > 10) {
                                    val truncatedText = s.toString().substring(0, 10)
                                    binding.textResult.setText(truncatedText)
                                    binding.textResult.setSelection(truncatedText.length)
                                }
                            }
                            result = resultValue
                        } else {
                            val buttonColor =
                                ContextCompat.getColor(requireContext(), R.color.button_disable)
                            binding.buttonAddIncome.backgroundTintList =
                                ColorStateList.valueOf(buttonColor)
                            binding.buttonAddIncome.isEnabled = false
                        }
                        AddListScreenActivity.textResult.value = s.toString()
                    } catch (e: NumberFormatException) {

                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

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
        ok.setOnClickListener {
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            AddListScreenActivity.textResult.value = ""
            startActivity(intent)
        }
    }

    private fun unixTimestampToLocalDateTime(unixTimestamp: Int): LocalDateTime {
        val instant = Instant.ofEpochSecond(unixTimestamp.toLong())
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    private fun unixTimestampToLocalDate(unixTimestamp: Int): LocalDate {
        val instant = Instant.ofEpochSecond(unixTimestamp.toLong())
        return instant.atZone(ZoneId.systemDefault()).toLocalDate()
    }

}