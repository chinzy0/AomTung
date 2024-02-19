package com.money.moneyx.main.incomeExpends.listPage.listWeek

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.FragmentListWeekBinding
import com.money.moneyx.main.incomeExpends.summary.SummaryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ListWeekFragment : Fragment() {
    private lateinit var binding: FragmentListWeekBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_week, container, false)
        viewModel = ViewModelProvider(this)[SummaryViewModel::class.java]
        binding.summaryViewModel = viewModel
        val preferences = Preference.getInstance(requireActivity())
        idMember = preferences.getInt("idmember", 0)

        callAPI()
        return binding.root
    }

    private fun callAPI() {
        binding.calendar.text = viewModel.week.first+" - "+viewModel.week.second
        binding.calendar.textSize = 16f
        binding.calendar.setOnClickListener {
            openCalendarPickerWeek(binding.calendar.text.toString())
        }
        binding.back.setOnClickListener { onBack1Week() }
        binding.forward.setOnClickListener { forward1Week() }
        binding.calendar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int, ) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int, ) {}
            override fun afterTextChanged(editable: Editable?) {
                currentDate = editable.toString()
                unixTimeStart = convertDateWeekStringToUnixTime(binding.calendar.text.toString())
                unixTimeEnd = convertDateEndWeekStringToUnixTime(binding.calendar.text.toString())
                AVLoading.startAnimLoading()
                CoroutineScope(Dispatchers.Main).launch {
                    delay(300)
                    viewModel.reportSummary(
                        idmember = idMember,
                        datatype = "week",
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
        unixTimeStart = convertDateWeekStringToUnixTime(binding.calendar.text.toString())
        unixTimeEnd = convertDateEndWeekStringToUnixTime(binding.calendar.text.toString())
        AVLoading.startAnimLoading()
        CoroutineScope(Dispatchers.Main).launch {
            delay(300)
            viewModel.reportSummary(
                idmember = idMember,
                datatype = "week",
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
    private fun convertDateWeekStringToUnixTime(weekRangeString: String): Long {
        return try {
            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("th", "TH"))
            val dates = weekRangeString.split(" - ")
            val startDate = dateFormat.parse(dates[0])
            val calendar = Calendar.getInstance()
            calendar.time = startDate
            calendar.set(Calendar.HOUR_OF_DAY, 0 + 7)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.timeInMillis / 1000L
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }
    private fun convertDateEndWeekStringToUnixTime(weekRangeString: String): Long {
        return try {
            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("th", "TH"))
            val dates = weekRangeString.split(" - ")
            val endDate = dateFormat.parse(dates[1])
            val calendar = Calendar.getInstance()
            calendar.time = endDate
            calendar.set(Calendar.HOUR_OF_DAY, 23 + 7)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.timeInMillis / 1000L
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }
    private fun openCalendarPickerWeek(initialDate: String) {
        val calendar = Calendar.getInstance()

        if (!initialDate.isNullOrBlank()) {
            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("th", "TH"))
            calendar.time = dateFormat.parse(initialDate)
        }

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val startOfWeek = calendar.time

        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val endOfWeek = calendar.time

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            R.style.DialogTheme,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val selectedDateFormatted = SimpleDateFormat("d MMMM yyyy", Locale("th", "TH")).format(selectedDate.time)
                calendar.time = selectedDate.time
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                val startOfWeek = calendar.time
                calendar.add(Calendar.DAY_OF_WEEK, 6)
                val endOfWeek = calendar.time
                val startOfWeekFormatted = SimpleDateFormat("d MMMM yyyy", Locale("th", "TH")).format(startOfWeek)
                val endOfWeekFormatted = SimpleDateFormat("d MMMM yyyy", Locale("th", "TH")).format(endOfWeek)
                val weekRangeText = "$startOfWeekFormatted - $endOfWeekFormatted"
                binding.calendar.text = weekRangeText
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun forward1Week() {
        val currentDateString = binding.calendar.text.toString()
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("th", "TH"))
        val currentDate = dateFormat.parse(currentDateString)
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        val formattedStartDate = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val formattedEndDate = dateFormat.format(calendar.time)
        binding.calendar.text = "$formattedStartDate - $formattedEndDate"
    }

    private fun onBack1Week() {
        val currentDateString = binding.calendar.text.toString()
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("th", "TH"))
        val currentDate = dateFormat.parse(currentDateString)
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val formattedStartDate = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val formattedEndDate = dateFormat.format(calendar.time)
        binding.calendar.text = "$formattedStartDate - $formattedEndDate"
    }

}