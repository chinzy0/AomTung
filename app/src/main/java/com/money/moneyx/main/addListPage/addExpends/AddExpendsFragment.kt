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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.money.moneyx.function.selectTypeExpends
import com.money.moneyx.function.showConfirmDeleteDialog
import com.money.moneyx.function.showTimePicker
import com.money.moneyx.main.addListPage.AddListScreenActivity
import com.money.moneyx.main.addListPage.calculator.CalculatorActivity
import com.money.moneyx.main.addListPage.category.CategoryExpendsActivity
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
import java.time.format.DateTimeFormatter
import java.util.Locale


class AddExpendsFragment(private val editExpends: Report?) : Fragment() {
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
    private var formatted = ""
    private var edit = false
    private lateinit var currentDate : LocalDate
    private lateinit var listDate: LocalDate
    private var formattedDate = ""
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

        currentDate = LocalDate.now()


        setDateTime()
        editIncomeData()
        setEventClick()
        changeColorBtn()
        loadingScreen(requireActivity())

        return binding.root
    }

    private fun editIncomeData() {
        editExpends?.let { data ->
            result = editExpends.amount.toDouble()
            categoryId = editExpends.category_id
            typeID = editExpends.type_id
            description = data.description
            autoSaveID = editExpends.save_auto_id
            categoryId = editExpends.category_id
            description = editExpends.description
            expendsID = editExpends.transaction_id

            val localDateTime = unixTimestampToLocalDateTime(data.timestamp)
            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            listDate = unixTimestampToLocalDate(data.timestamp)
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
            formattedDate = localDateTime.format(dateFormat)
            val formattedTime = localDateTime.format(timeFormat)

            binding.textTv.setText(result.toString())
            binding.textTime2.text = data.type_name
            binding.textTime3.text = data.category_name
            binding.textTime44.text = data.description
            binding.textTime5.text = data.save_auto_name
            binding.textDate.text = formattedDate
            binding.textTime.text = formattedTime
        }
        if (editExpends != null) {
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
            if (editExpends.save_auto_id != 1) {
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
                    dateTimeExpends(requireActivity()) { formattedDate ->
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
                        }
                    }
                }

                "expendsAutoSaveClick" -> {
                    autoSave(requireActivity(), HomeActivity.listScheduleAuto) { autoSaved ->
                        binding.textTime5.text = autoSaved.first
                        autoSaveID = autoSaved.second
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
                        if (typeID == 0 ){
                            binding.textTime2.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
                        }else{
                            binding.textTime2.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                        }
                        if (categoryId == 0){ binding.textTime3.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
                        }else{
                            binding.textTime3.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                        }
                        addListAlertDialog(requireActivity())
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
                                activity?.runOnUiThread { showSuccessDialog() }
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
                                activity?.runOnUiThread { showSuccessDialog() }
                                edit = false
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
                    resultValue = BigDecimal(resultValue).setScale(2, RoundingMode.HALF_EVEN).toDouble()

                    if (resultValue > 0) {
                        val buttonColor = ContextCompat.getColor(requireContext(), R.color.expends)
                        binding.buttonAddExpends.backgroundTintList =
                            ColorStateList.valueOf(buttonColor)
                        binding.buttonAddExpends.isEnabled = true
                        result = resultValue
                    } else {
                        val buttonColor =
                            ContextCompat.getColor(requireContext(), R.color.button_disable)
                        binding.buttonAddExpends.backgroundTintList =
                            ColorStateList.valueOf(buttonColor)
                        binding.buttonAddExpends.isEnabled = false
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


    private fun setDateTime() {
        binding.textTime.text = viewModel.time
        binding.textDate.text = viewModel.date
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
        val expends = R.color.expends
        val resolvedColor = ContextCompat.getColor(requireActivity(), expends)
        ok.backgroundTintList = ColorStateList.valueOf(resolvedColor)
        ok.setOnClickListener {
            val intent = Intent(requireActivity(), HomeActivity::class.java)
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