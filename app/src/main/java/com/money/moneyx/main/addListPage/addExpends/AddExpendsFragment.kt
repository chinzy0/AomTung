package com.money.moneyx.main.addListPage.addExpends

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
import com.money.moneyx.databinding.FragmentAddExpendsBinding
import com.money.moneyx.function.addListAlertDialog
import com.money.moneyx.function.autoSave
import com.money.moneyx.function.dateTimeExpends
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.function.note
import com.money.moneyx.function.selectTypeExpends
import com.money.moneyx.function.showConfirmDeleteDialog
import com.money.moneyx.function.showTimePicker
import com.money.moneyx.main.addListPage.AddListScreenActivity
import com.money.moneyx.main.addListPage.calculator.CalculatorActivity
import com.money.moneyx.main.addListPage.category.CategoryExpendsActivity
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


class AddExpendsFragment(
    private val editExpends: Report?,
    private val editAutoSaveExpends: GetListAutoData?,
    private val incomeExpends: ReportALL?,
) : Fragment() {
    private lateinit var binding: FragmentAddExpendsBinding
    private lateinit var viewModel: AddExpendsViewModel
    private var typeID = 0
    private var autoSaveID = 1
    private var categoryId = 0
    private var idMember = 0
    private var description = ""
    private var dateTimeSelected: Long = 0
    private var expendsID = 0
    private var noteText = ""
    private var result = 0.0
    private var edit = false
    private lateinit var currentDate: LocalDate
    private lateinit var listDate: LocalDate
    private var formattedDate = ""
    private var formattedTime = ""
    private var autoSaveName = ""
    private var position = ""
    private val onClickDialog = MutableLiveData<String>()


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
        AddListScreenActivity.change.value = ""

        currentDate = LocalDate.now()


        setDateTime()
        editIncomeData()
        setEventClick()
        changeColorBtn()
        loadingScreen(requireActivity())

        return binding.root
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

    private fun editIncomeData() {
        editAutoSaveExpends?.let { model ->
            val editAutoSaveExpendsAmount = editAutoSaveExpends?.amount?.replace(",", "")
            result = editAutoSaveExpendsAmount!!.toDouble()
            categoryId = model.category_id
            typeID = model.type_id
            description = model.description
            autoSaveID = model.save_auto_id
            description = model.description
            expendsID = model.transaction_id

            val localDateTime = unixTimestampToLocalDateTime(model.timestamp)
            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            listDate = unixTimestampToLocalDate(model.timestamp)
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
            formattedDate = localDateTime.format(dateFormat)
            formattedTime = localDateTime.format(timeFormat)

            binding.textTv.setText(editAutoSaveExpendsAmount)
            binding.textTime2.text = model.type_name
            binding.textTime3.text = model.category_name
            binding.textTime44.text = model.description
            binding.textTime5.text = model.save_auto_name
            autoSaveName = model.save_auto_name
            binding.textDate.text = formattedDate
            binding.textTime.text = formattedTime
        }
        incomeExpends?.let { expends ->
            val amount = expends.amount.replace(",", "")
            result = amount.toDouble()
            categoryId = expends.category_id
            typeID = expends.type_id
            description = expends.description
            autoSaveID = expends.save_auto_id
            description = expends.description
            expendsID = expends.transaction_id

            val localDateTime = unixTimestampToLocalDateTime(expends.timestamp)
            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            listDate = unixTimestampToLocalDate(expends.timestamp)
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
            formattedDate = localDateTime.format(dateFormat)
            formattedTime = localDateTime.format(timeFormat)

            binding.textTv.setText(amount)
            binding.textTime2.text = expends.type_name
            binding.textTime3.text = expends.category_name
            binding.textTime44.text = expends.description
            binding.textTime5.text = expends.save_auto_name
            autoSaveName = expends.save_auto_name
            binding.textDate.text = formattedDate
            binding.textTime.text = formattedTime
        }


        editExpends?.let { data ->
            val amount = editExpends.amount.replace(",", "")
            result = amount.toDouble()
            categoryId = data.category_id
            typeID = data.type_id
            description = data.description
            autoSaveID = data.save_auto_id
            description = data.description
            expendsID = data.transaction_id

            val localDateTime = unixTimestampToLocalDateTime(data.timestamp)
            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            listDate = unixTimestampToLocalDate(data.timestamp)
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
            formattedDate = localDateTime.format(dateFormat)
            formattedTime = localDateTime.format(timeFormat)

            binding.textTv.setText(amount)
            binding.textTime2.text = data.type_name
            binding.textTime3.text = data.category_name
            binding.textTime44.text = data.description
            binding.textTime5.text = data.save_auto_name
            autoSaveName = data.save_auto_name
            binding.textDate.text = formattedDate
            binding.textTime.text = formattedTime
        }
        if (editExpends != null || editAutoSaveExpends != null || incomeExpends != null) {
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
            if (editExpends?.save_auto_id != 1 || editAutoSaveExpends?.save_auto_id != 1 || incomeExpends?.save_auto_id != 1) {
                if (listDate.isBefore(currentDate)){
                    binding.autosaveButton.isEnabled = false
                    val textColor = ContextCompat.getColor(requireActivity(), R.color.disable)
                    binding.textTime5.setTextColor(textColor)
                    binding.title5.setTextColor(textColor)
                    binding.img55.visibility = View.VISIBLE
                    binding.detail55.visibility = View.VISIBLE
                }else{
                    val textColor = ContextCompat.getColor(requireActivity(), R.color.black)
                    binding.textTime5.setTextColor(textColor)
                    binding.title5.setTextColor(textColor)
                    binding.img55.visibility = View.GONE
                    binding.detail55.visibility = View.GONE
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

        if (editExpends == null && editAutoSaveExpends == null){
            position = "incomeExpends"
        }else if (editExpends == null && incomeExpends == null){
            position = "editAutoSave"
        }else if (editAutoSaveExpends == null && incomeExpends == null){
            position = "editIncome"
        }
    }


    private fun setEventClick() {
        onClickDialog.observe(requireActivity(), Observer {
            when (it) {
                "confirmDelete" -> {
                    viewModel.deleteExpenses(expendsID) { del ->
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
                "expendsCalculateClick" -> {
                    val intent = Intent(requireActivity(), CalculatorActivity::class.java)
                    intent.putExtra("resultExpends", binding.textTv.text.toString())
                    resultActivityAppointment.launch(intent)
                }

                "expendsDateClick" -> {
                    dateTimeExpends(
                        requireActivity(),
                        binding.textDate.text.toString()
                    ) { formatted ->
                        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        listDate = LocalDate.parse(formatted, dateFormat)
                        binding.textDate.text = formatted
                        if (edit){
                            if (formattedDate != binding.textDate.text) {
                                binding.autosaveButton.isEnabled = false
                                binding.textTime5.text = autoSaveName
                                val textColor = ContextCompat.getColor(requireActivity(), R.color.disable)
                                binding.textTime5.setTextColor(textColor)
                                binding.title5.setTextColor(textColor)
                                binding.img55.visibility = View.VISIBLE
                                binding.detail55.visibility = View.VISIBLE
                                if (editExpends == null && editAutoSaveExpends == null){
                                    autoSaveID = incomeExpends!!.save_auto_id
                                }else if (editExpends == null && incomeExpends == null){
                                    autoSaveID = editAutoSaveExpends!!.save_auto_id
                                }else if (editAutoSaveExpends == null && incomeExpends == null){
                                    autoSaveID = editExpends!!.save_auto_id
                                }
                                if (listDate.isBefore(currentDate)) {
                                    binding.textTime5.text = "ไม่มี"
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
                            }else{
                                binding.textTime5.text = autoSaveName
                                binding.autosaveButton.isEnabled = true
                                val textColor = ContextCompat.getColor(requireActivity(), R.color.black)
                                binding.textTime5.setTextColor(textColor)
                                binding.title5.setTextColor(textColor)
                                binding.img55.visibility = View.GONE
                                binding.detail55.visibility = View.GONE
                                if (editExpends == null && editAutoSaveExpends == null){
                                    autoSaveID = incomeExpends!!.save_auto_id
                                }else if (editExpends == null && incomeExpends == null){
                                    autoSaveID = editAutoSaveExpends!!.save_auto_id
                                }else if (editAutoSaveExpends == null && incomeExpends == null){
                                    autoSaveID = editExpends!!.save_auto_id
                                }
                            }
                        }else{
                            if (listDate.isBefore(currentDate)) {
                                binding.textTime5.text = "ไม่มี"
                                binding.autosaveButton.isEnabled = false
                                val textColor = ContextCompat.getColor(requireActivity(), R.color.disable)
                                binding.textTime5.setTextColor(textColor)
                                binding.title5.setTextColor(textColor)
                                binding.img55.visibility = View. GONE
                                binding.detail55.visibility = View.GONE
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

                        }
                        updateData()
                    }
                }

                "expendsTimeClick" -> {
                    showTimePicker(
                        requireActivity(),
                        binding.textTime.text.toString()
                    ) { formattedTime ->
                        binding.textTime.text = formattedTime
                        dateTimeSelected = convertDateTimeToUnixTimestamp(
                            binding.textDate.text.toString(),
                            formattedTime
                        )
                        updateData()
                    }
                }

                "expendsTypeClick" -> {
                    selectTypeExpends(requireActivity(), HomeActivity.getAllTypeExpenses) { type ->
                        binding.textTime2.text = type.first
                        typeID = type.second
                        updateData()
                    }
                }

                "expendsCategoryClick" -> {
                    selectCategory()
                }

                "expendsNoteClick" -> {
                    note(requireActivity(), noted = noteText, page = "expends") { text ->
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

                "expendsAutoSaveClick" -> {
                    autoSave(requireActivity(), HomeActivity.listScheduleAuto) { autoSaved ->
                        binding.textTime5.text = autoSaved.first
                        autoSaveID = autoSaved.second
                        updateData()
                    }
                }

                "expendsDelClick" -> {
                    showConfirmDeleteDialog(requireActivity(), onClickDialog)
                }

                "expendsSaveClick" -> {
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
                                ContextCompat.getColor(binding.root.context, R.color.red)
                            )
                        } else {
                            binding.textTime3.setTextColor(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.black
                                )
                            )
                        }
                        addListAlertDialog(requireActivity(), "expends")
                    } else if (edit) {
                        AVLoading.startAnimLoading()
                        viewModel.updateExpenses(
                            expends_id = expendsID,
                            type_id = typeID,
                            category_id = categoryId,
                            description = description,
                            amount = result,
                            createdateTime = dateTimeSelected.toInt(),
                            auto_schedule = autoSaveID
                        ) { updateExpends ->
                            AVLoading.stopAnimLoading()
                            if (updateExpends.data.is_Updated) {
                                activity?.runOnUiThread { showSuccessDialog(position) }
                            } else {
                            }
                        }
                    } else {
                        AVLoading.startAnimLoading()
                        viewModel.createListExpenses(
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
                                activity?.runOnUiThread { showSuccessDialog(position) }
                                edit = false
                            } else {

                            }
                        }
                    }
                }
            }
        })
    }

    private fun updateData() {
        if (edit && binding.textTv.text.toString().toDouble() > 0) {
            if (editExpends != null) {
                val amount = editExpends.amount.replace(",", "")
                if (binding.textDate.text != formattedDate || binding.textTv.text.toString()
                        .toDouble() != amount.toDouble()
                    || binding.textTime.text != formattedTime || typeID != editExpends.type_id || categoryId != editExpends.category_id
                    || description != editExpends.description || autoSaveID != editExpends.save_auto_id
                ) {
                    enableBtn()
                } else {
                    unableBtn()
                }
            } else if (editAutoSaveExpends != null) {
                val amount = editAutoSaveExpends.amount.replace(",", "")
                if (binding.textDate.text != formattedDate || binding.textTv.text.toString()
                        .toDouble() != amount.toDouble()
                    || binding.textTime.text != formattedTime || typeID != editAutoSaveExpends.type_id || categoryId != editAutoSaveExpends.category_id
                    || description != editAutoSaveExpends.description || autoSaveID != editAutoSaveExpends.save_auto_id
                ) {
                    enableBtn()
                } else {
                    unableBtn()
                }
            } else if (incomeExpends != null) {
                val amount = incomeExpends.amount.replace(",", "")
                if (binding.textDate.text != formattedDate || binding.textTv.text.toString()
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
                    updateData()
                }
            }
        }


    private fun selectCategory() {
        val intent = Intent(requireActivity(), CategoryExpendsActivity::class.java)
        getCategory.launch(intent)
    }

    private fun enableBtn() {
        val buttonColor = ContextCompat.getColor(requireContext(), R.color.expends)
        binding.buttonAddExpends.backgroundTintList = ColorStateList.valueOf(buttonColor)
        binding.buttonAddExpends.isEnabled = true
        AddListScreenActivity.change.value = "HasChanged"
    }

    private fun unableBtn() {
        val buttonColor = ContextCompat.getColor(requireContext(), R.color.button_disable)
        binding.buttonAddExpends.backgroundTintList = ColorStateList.valueOf(buttonColor)
        binding.buttonAddExpends.isEnabled = false
        AddListScreenActivity.change.value = ""
    }

    private fun changeColorBtn() {
        binding.textTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    AddListScreenActivity.textResult.value = s.toString()
                } else {
                    AddListScreenActivity.textResult.value = s.toString()
                    try {
                        var resultValue = s.toString().toDouble()
                        if (resultValue > 0) {
                            val buttonColor =
                                ContextCompat.getColor(requireContext(), R.color.expends)
                            binding.buttonAddExpends.backgroundTintList =
                                ColorStateList.valueOf(buttonColor)
                            binding.buttonAddExpends.isEnabled = true
                            if (s!!.length > 13) {
                                val truncatedText = s.toString().substring(0, 13)
                                binding.textTv.setText(truncatedText)
                                binding.textTv.setSelection(truncatedText.length)
                            }
                            val indexOfDot = s.indexOf('.')
                            if (indexOfDot != -1 && s.length - indexOfDot > 3) {
                                val truncatedDecimal = s.substring(0, indexOfDot + 3)
                                binding.textTv.setText(truncatedDecimal)
                                binding.textTv.setSelection(truncatedDecimal.length)
                            }
                            if (!s.contains('.')) {
                                if (s.length > 10) {
                                    val truncatedText = s.toString().substring(0, 10)
                                    binding.textTv.setText(truncatedText)
                                    binding.textTv.setSelection(truncatedText.length)
                                }
                            }
                            result = resultValue
                        } else {
                            val buttonColor =
                                ContextCompat.getColor(requireContext(), R.color.button_disable)
                            binding.buttonAddExpends.backgroundTintList =
                                ColorStateList.valueOf(buttonColor)
                            binding.buttonAddExpends.isEnabled = false
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


    private fun setDateTime() {
        binding.textTime.text = viewModel.time
        binding.textDate.text = viewModel.date
    }

    private fun showSuccessDialog(position: String) {
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
        val expends = R.color.expends
        val resolvedColor = ContextCompat.getColor(requireActivity(), expends)
        ok.backgroundTintList = ColorStateList.valueOf(resolvedColor)
        ok.setOnClickListener {
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            intent.putExtra("positionClick", position)
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