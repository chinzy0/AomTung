package com.money.moneyx.main.incomeExpends.listPage.listMonth

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.NumberPicker
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.FragmentListMonthBinding
import com.money.moneyx.main.incomeExpends.summary.SummaryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ListMonthFragment : Fragment() {
    private lateinit var binding: FragmentListMonthBinding
    private lateinit var viewModel: SummaryViewModel
    private var idMember = 0
    private var currentDate = ""
    private var unixTimeStart = 0L
    private var unixTimeEnd = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_month, container, false)
        viewModel = ViewModelProvider(this)[SummaryViewModel::class.java]
        binding.summaryViewModel = viewModel
        val preferences = Preference.getInstance(requireActivity())
        idMember = preferences.getInt("idmember", 0)

        callAPI()
        return binding.root
    }

    private fun callAPI() {
        binding.calendar.text = viewModel.monthAndYear
        binding.calendar.textSize = 20f
        binding.calendar.setOnClickListener {
            dropdownListPage(binding.calendar.text.toString())
        }
        currentDate = viewModel.monthAndYear
        binding.back.setOnClickListener { goBack1Month() }
        binding.forward.setOnClickListener { forward1Month() }
        binding.calendar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                currentDate = editable.toString()
                unixTimeStart = convertMonthStringToUnixTime(binding.calendar.text.toString())
                unixTimeEnd = convertEndMonthStringToUnixTime(binding.calendar.text.toString())
                AVLoading.startAnimLoading()
                CoroutineScope(Dispatchers.Main).launch {
                    delay(300)
                    viewModel.reportSummary(
                        idmember = idMember,
                        datatype = "month",
                        end_timestamp = unixTimeEnd,
                        start_timestamp = unixTimeStart,
                    ) { model ->
                        AVLoading.stopAnimLoading()
                        if (model.success) {
                            activity?.runOnUiThread {
                                binding.expendsSummary.text = model.data[0].total_expenses
                                binding.incomeSummary.text = model.data[0].total_income
                                binding.incomeTypeSummary.text = model.data[0].total_Income_Certain
                                binding.incomeTypeUncertainSummary.text = model.data[0].total_Income_Uncertain
                                binding.expendsTypeSummary.text = model.data[0].total_Expenses_Necessary
                                binding.expendsTypeUnSummary.text = model.data[0].total_Expenses_Unnecessary
                            }
                        }
                    }
                }
            }
        })
        unixTimeStart = convertMonthStringToUnixTime(binding.calendar.text.toString())
        unixTimeEnd = convertEndMonthStringToUnixTime(binding.calendar.text.toString())
        CoroutineScope(Dispatchers.Main).launch {
            delay(300)
            viewModel.reportSummary(
                idmember = idMember,
                datatype = "month",
                end_timestamp = unixTimeEnd,
                start_timestamp = unixTimeStart,
            ) { model ->
                AVLoading.stopAnimLoading()
                if (model.success) {
                    activity?.runOnUiThread {
                        binding.expendsSummary.text = model.data[0].total_expenses
                        binding.incomeSummary.text = model.data[0].total_income
                        binding.incomeTypeSummary.text = model.data[0].total_Income_Certain
                        binding.incomeTypeUncertainSummary.text = model.data[0].total_Income_Uncertain
                        binding.expendsTypeSummary.text = model.data[0].total_Expenses_Necessary
                        binding.expendsTypeUnSummary.text = model.data[0].total_Expenses_Unnecessary
                    }
                }
            }
        }
    }

    private fun convertMonthStringToUnixTime(monthString: String): Long {
        return try {
            val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("th", "TH"))
            val date = dateFormat.parse(monthString)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 7)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.timeInMillis / 1000L
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }
    private fun convertEndMonthStringToUnixTime(monthString: String): Long {
        return try {
            val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("th", "TH"))
            val date = dateFormat.parse(monthString)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            calendar.set(Calendar.HOUR_OF_DAY, 23+7)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.timeInMillis / 1000L
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }
    private fun dropdownListPage(monthAndYear: String, ) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dropdown_month)
        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)

        val months = arrayOf(
            "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม",
            "กันยายน", " ตุลาคม ", "พฤศจิกายน", "ธันวาคม"
        )

        val monthPicker = dialog.findViewById<NumberPicker>(R.id.monthPicker)
        monthPicker.minValue = 0
        monthPicker.maxValue = months.size - 1
        monthPicker.displayedValues = months

        val yearPicker = dialog.findViewById<NumberPicker>(R.id.yearPicker)
        val yearNow = Calendar.getInstance().get(Calendar.YEAR)
        yearPicker.minValue = yearNow - 100
        yearPicker.maxValue = yearNow + 100

        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("th", "TH"))
        val date = dateFormat.parse(monthAndYear)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val selectedMonthIndex = calendar.get(Calendar.MONTH)
        val selectedYear = calendar.get(Calendar.YEAR)

        monthPicker.value = selectedMonthIndex
        yearPicker.value = selectedYear

        val submit = dialog.findViewById<ConstraintLayout>(R.id.buttonSubmitMonthYear)
        val income = R.color.income
        val resolvedColor = ContextCompat.getColor(requireActivity(), income)
        submit.backgroundTintList = ColorStateList.valueOf(resolvedColor)
        submit.setOnClickListener {
            val selectedMonth = monthPicker.value
            val selectedYear = yearPicker.value
            val selectedDateString = getFormattedDateString(selectedMonth, selectedYear)
            binding.calendar.text = selectedDateString
            dialog.dismiss()
        }
    }
    private fun getFormattedDateString(selectedMonth: Int, selectedYear: Int): String {
        val months = arrayOf(
            "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน",
            "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"
        )

        return "${months[selectedMonth]} $selectedYear"
    }

    private fun forward1Month() {
        val currentDateString = binding.calendar.text.toString()
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("th", "TH"))
        val currentDate = dateFormat.parse(currentDateString)
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.MONTH, +1)
        val formattedDate = dateFormat.format(calendar.time)
        binding.calendar.text = formattedDate
    }


    private fun goBack1Month() {
        val currentDateString = binding.calendar.text.toString()
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("th", "TH"))
        val currentDate = dateFormat.parse(currentDateString)
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.MONTH, -1)
        val formattedDate = dateFormat.format(calendar.time)
        binding.calendar.text = formattedDate
    }
}