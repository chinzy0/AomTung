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
import com.money.moneyx.main.homeScreen.HomeActivity
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.Report
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone


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
    private lateinit var currentDate : LocalDate
    private lateinit var listDate:  LocalDate
    private var formattedDate = ""
    private var edit = false
    private val onClickDialog = MutableLiveData<String>()


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
        Log.i("IdMember", idMember.toString())

        currentDate = LocalDate.now()



        setDateTime()
        editIncomeData()
        setEventClick()
        changeColorBtn()
        loadingScreen(requireActivity())

        Log.i("adskljadladad",result.toString())




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
            listDate = unixTimestampToLocalDate(data.timestamp)
            Log.i("LocalDate",listDate.toString())
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
            formattedDate = localDateTime.format(dateFormat)
            val formattedTime = localDateTime.format(timeFormat)

            binding.textResult.setText(editIncome.amount)
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
            if (editIncome.save_auto_id != 1) {
                binding.autosaveButton.isEnabled = false
                binding.textDate.isEnabled = false
                binding.textTime.isEnabled = false
                val textColor = ContextCompat.getColor(requireActivity(), R.color.disable)
                binding.textTime5.setTextColor(textColor)
                binding.title5.setTextColor(textColor)
                binding.textTime.setTextColor(textColor)
                binding.textDate.setTextColor(textColor)
                binding.title.setTextColor(textColor)
                binding.img55.visibility = View.VISIBLE
                binding.img11.visibility = View.VISIBLE
                binding.detail55.visibility = View.VISIBLE
                binding.detail11.visibility = View.VISIBLE
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
                    dateTime(requireActivity()) { formattedDate ->
                        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        listDate = LocalDate.parse(formattedDate, dateFormat)
                        binding.textDate.text = formattedDate
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
                        addListAlertDialog(requireActivity())
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
            val decimalFormat = DecimalFormat("#,###,###,###.##")
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
                    if (s.toString().isNotEmpty()) {
                        var resultValue = s.toString().replace(",", "").toDouble()
                        resultValue = BigDecimal(resultValue).setScale(2, RoundingMode.HALF_EVEN).toDouble()

                        val decimalFormat = DecimalFormat("#,###,##0.00")
                        val formattedResult = decimalFormat.format(resultValue)

                        if (s!!.length <= 15) {  // Assuming maximum length including decimals is 15
                            binding.textResult.removeTextChangedListener(this)
                            binding.textResult.setText(formattedResult)
                            binding.textResult.setSelection(binding.textResult.text.length-3)
                            binding.textResult.addTextChangedListener(this)

                            val buttonColor = if (resultValue > 0) R.color.income else R.color.button_disable
                            binding.buttonAddIncome.backgroundTintList =
                                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), buttonColor))
                            binding.buttonAddIncome.isEnabled = resultValue > 0

                            result = resultValue
                        } else {
                            binding.textResult.removeTextChangedListener(this)
                            binding.textResult.setText(s.subSequence(0, 15))
                            binding.textResult.setSelection(binding.textResult.text.length)
                            binding.textResult.addTextChangedListener(this)
                        }
                    }
                } catch (e: NumberFormatException) {
                    // Handle the exception if needed
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