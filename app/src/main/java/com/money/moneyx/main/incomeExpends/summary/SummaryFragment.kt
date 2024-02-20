package com.money.moneyx.main.incomeExpends.summary

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.NumberPicker
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.tabs.TabLayout
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.FragmentSummaryBinding
import com.money.moneyx.function.loadingScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SummaryFragment : Fragment() {
    private lateinit var binding: FragmentSummaryBinding
    private val fragment = ArrayList<Fragment>()
    private lateinit var viewModel: SummaryViewModel
    private var currentDate = ""
    private var unixTimeStart = 0L
    private var unixTimeEnd = 0L
    private var idMember = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_summary, container, false)
        viewModel = ViewModelProvider(this)[SummaryViewModel::class.java]
        binding.summaryViewModel = viewModel
        val preferences = Preference.getInstance(requireActivity())
        idMember = preferences.getInt("idmember", 0)
        setupTabs()
        updateUIForDay()

        loadingScreen(requireActivity())
        return binding.root
    }



    private fun setupTabs() {
        val tabTitles = listOf("วัน", "สัปดาห์", "เดือน", "ปี")
        fragment.clear()
        binding.tabLayoutList.addTab(binding.tabLayoutList.newTab().setText(tabTitles[0]))
        binding.tabLayoutList.addTab(binding.tabLayoutList.newTab().setText(tabTitles[1]))
        binding.tabLayoutList.addTab(binding.tabLayoutList.newTab().setText(tabTitles[2]))
        binding.tabLayoutList.addTab(binding.tabLayoutList.newTab().setText(tabTitles[3]))
        binding.tabLayoutList.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> updateUIForDay()
                    1 -> updateUIForWeek()
                    2 -> updateUIForMonth()
                    3 -> updateUIForYear()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun updateUIForDay() {
        binding.calendar.text = viewModel.date
        binding.calendar.textSize = 20f
        binding.calendar.setOnClickListener {
            openCalendarPicker(binding.calendar.text.toString())
        }
        currentDate = viewModel.date
        binding.back.setOnClickListener { goBack1day() }
        binding.forward.setOnClickListener { forward1day() }
        binding.calendar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int, ) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int, ) {}
            override fun afterTextChanged(editable: Editable?) {
                currentDate = editable.toString()
                unixTimeStart = convertDateStringToUnixTime(binding.calendar.text.toString())
                unixTimeEnd = convertDateEndStringToUnixTime(binding.calendar.text.toString())
                AVLoading.startAnimLoading()
                CoroutineScope(Dispatchers.Main).launch {
                    delay(300)
                    viewModel.reportSummary(
                        idmember = idMember,
                        datatype = "day",
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
                    viewModel.reportListGraph(
                        idmember = idMember,
                        datatype = "day",
                        end_timestamp = unixTimeEnd,
                        start_timestamp = unixTimeStart,
                    ){ graph ->
                        AVLoading.stopAnimLoading()
                        if (graph.success) {
                            graph(graph)
                        }
                    }
                }
            }
        })
        unixTimeStart = convertDateStringToUnixTime(binding.calendar.text.toString())
        unixTimeEnd = convertDateEndStringToUnixTime(binding.calendar.text.toString())
        viewModel.reportSummary(
                idmember = idMember,
                datatype = "day",
                end_timestamp = unixTimeEnd,
                start_timestamp = unixTimeStart,
            ) { model ->
                if (model.success) {
                    activity?.runOnUiThread{
                        binding.expendsSummary.text = model.data[0].total_expenses
                        binding.incomeSummary.text = model.data[0].total_income
                        binding.incomeTypeSummary.text = model.data[0].total_Income_Certain
                        binding.incomeTypeUncertainSummary.text = model.data[0].total_Income_Uncertain
                        binding.expendsTypeSummary.text = model.data[0].total_Expenses_Necessary
                        binding.expendsTypeUnSummary.text = model.data[0].total_Expenses_Unnecessary
                    }
                }
            }
        viewModel.reportListGraph(
            idmember = idMember,
            datatype = "day",
            end_timestamp = unixTimeEnd,
            start_timestamp = unixTimeStart,
        ){ graph ->
            AVLoading.stopAnimLoading()
            if (graph.success) {
                graph(graph)
            }
        }
    }
    private fun graph(graphData: ReportListGraph) {
        val barChart: BarChart = binding.barChart
        barChart.description.text = ""
        val dataOfList = graphData.data.dataOfList

        val incomeEntries = mutableListOf<BarEntry>()
        val expenseEntries = mutableListOf<BarEntry>()

        dataOfList.forEachIndexed { index, dataOf ->
            incomeEntries.add(BarEntry(index.toFloat(), dataOf.totalIncome.toFloat()))
            expenseEntries.add(BarEntry(index.toFloat(), dataOf.totalExpenses.toFloat()))
        }


        val incomeDataSet = BarDataSet(incomeEntries, "รายรับ")
        incomeDataSet.color = ContextCompat.getColor(requireContext(), R.color.income)

        val expenseDataSet = BarDataSet(expenseEntries, "รายจ่าย")
        expenseDataSet.color = ContextCompat.getColor(requireContext(), R.color.expends)


        val dataSets = mutableListOf<IBarDataSet>()
        dataSets.add(incomeDataSet)
        dataSets.add(expenseDataSet)

        // Creating the bar data
        val barData = BarData(dataSets)


        val groupSpace = 0.3f
        val barSpace = 0.05f
        val barWidth = 0.3f
        barData.barWidth = barWidth
        barChart.xAxis.axisMinimum = 0f
        barChart.xAxis.axisMaximum = graphData.data.dataOfList.size.toFloat()
        barData.groupBars(0f, groupSpace, barSpace)

        barChart.data = barData

        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Disable right Y-axis
        barChart.axisRight.isEnabled = false
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setCenterAxisLabels(true)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        val line  = ContextCompat.getColor(requireActivity(), R.color.line)
        xAxis.axisLineColor = line
        xAxis.axisLineWidth = 1.5f
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)
        barChart.invalidate()
    }






    private fun updateUIForWeek() {
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
        viewModel.reportSummary(
            idmember = idMember,
            datatype = "week",
            end_timestamp = unixTimeEnd,
            start_timestamp = unixTimeStart,
        ) { model ->
            if (model.success) {
                activity?.runOnUiThread{
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


    private fun updateUIForMonth() {
        binding.calendar.text = viewModel.monthAndYear
        binding.calendar.textSize = 20f
        binding.calendar.setOnClickListener {
            dropdownSummaryPage(binding.calendar.text.toString())
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
        viewModel.reportSummary(
            idmember = idMember,
            datatype = "month",
            end_timestamp = unixTimeEnd,
            start_timestamp = unixTimeStart,
        ) { model ->
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
    private fun updateUIForYear() {
        binding.calendar.text = viewModel.year
        binding.calendar.textSize = 20f
        binding.calendar.setOnClickListener {
            dropdownSelectYearSummaryPage(binding.calendar.text.toString())
        }
        binding.back.setOnClickListener { goBack1Year() }
        binding.forward.setOnClickListener { forward1Year() }
        binding.calendar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int, ) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int, ) {}
            override fun afterTextChanged(editable: Editable?) {
                currentDate = editable.toString()
                unixTimeStart = convertYearToUnixTime(binding.calendar.text.toString())
                unixTimeEnd = getLastDayOfYearAndConvertToUnixTime(binding.calendar.text.toString())
                AVLoading.startAnimLoading()
                CoroutineScope(Dispatchers.Main).launch {
                    delay(300)
                    viewModel.reportSummary(
                        idmember = idMember,
                        datatype = "year",
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
        unixTimeStart = convertYearToUnixTime(binding.calendar.text.toString())
        unixTimeEnd = getLastDayOfYearAndConvertToUnixTime(binding.calendar.text.toString())
        AVLoading.startAnimLoading()
        CoroutineScope(Dispatchers.Main).launch {
            delay(300)
            viewModel.reportSummary(
                idmember = idMember,
                datatype = "year",
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

    private fun forward1Year() {
        val currentDateString = binding.calendar.text.toString()
        val dateFormat = SimpleDateFormat("yyyy", Locale("th", "TH"))
        val currentDate = dateFormat.parse(currentDateString)
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.YEAR, +1)
        val formattedDate = dateFormat.format(calendar.time)
        binding.calendar.text = formattedDate
    }

    private fun goBack1Year() {
        val currentDateString = binding.calendar.text.toString()
        val dateFormat = SimpleDateFormat("yyyy", Locale("th", "TH"))
        val currentDate = dateFormat.parse(currentDateString)
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.YEAR, -1)
        val formattedDate = dateFormat.format(calendar.time)
        binding.calendar.text = formattedDate
    }

    private fun openCalendarPicker(initialDate: String) {
        val calendar = Calendar.getInstance()
        if (!initialDate.isNullOrBlank()) {
            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("th","TH"))
            calendar.time = dateFormat.parse(initialDate)
        }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            R.style.DialogThemeEx,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("d MMMM yyyy",  Locale("th","TH"))
                val formattedDate = dateFormat.format(selectedDate.time)
                binding.calendar.text = formattedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
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
            R.style.DialogThemeEx,
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




     private fun goBack1day() {
        val currentDateString = binding.calendar.text.toString()
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("th","TH"))
        val currentDate = dateFormat.parse(currentDateString)
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val formattedDate = dateFormat.format(calendar.time)
        binding.calendar.text = formattedDate

    }

     private fun forward1day() {
        val currentDateString = binding.calendar.text.toString()
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("th","TH"))
        val currentDate = dateFormat.parse(currentDateString)
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_MONTH, +1)
        val formattedDate = dateFormat.format(calendar.time)
        binding.calendar.text = formattedDate
    }

    private fun convertDateStringToUnixTime(dateString: String): Long {
        try {
            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("th","TH"))
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY, 7)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 1)
            return calendar.timeInMillis / 1000L
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }
    private fun convertYearToUnixTime(yearString: String): Long {
        try {
            val dateFormat = SimpleDateFormat("yyyy", Locale("th", "TH"))
            val date = dateFormat.parse(yearString)

            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
            calendar.set(Calendar.HOUR_OF_DAY, 0+7)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            return calendar.timeInMillis / 1000L
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }

    private fun getLastDayOfYearAndConvertToUnixTime(yearString: String): Long {
        try {
            val dateFormat = SimpleDateFormat("yyyy", Locale("th", "TH"))
            val date = dateFormat.parse(yearString)

            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            calendar.set(Calendar.HOUR_OF_DAY, 23+7)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)

            return calendar.timeInMillis / 1000L
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
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


    private fun convertDateEndStringToUnixTime(dateString: String): Long {
        return try {
            val dateFormat = SimpleDateFormat("d MMMM yyyy",  Locale("th","TH"))
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date
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
    private fun dropdownSummaryPage(monthAndYear: String, ) {
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

    private fun dropdownSelectYearSummaryPage(monthAndYear: String, ) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.select_year_dialog)
        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)

        val yearPicker = dialog.findViewById<NumberPicker>(R.id.yearPicker)
        val yearNow = Calendar.getInstance().get(Calendar.YEAR)
        yearPicker.minValue = yearNow - 100
        yearPicker.maxValue = yearNow + 100

        val dateFormat = SimpleDateFormat("yyyy", Locale("th", "TH"))
        val date = dateFormat.parse(monthAndYear)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val selectedYear = calendar.get(Calendar.YEAR)

        yearPicker.value = selectedYear

        val submit = dialog.findViewById<ConstraintLayout>(R.id.buttonSubmitMonthYear)

        submit.setOnClickListener {
            val selectedYear = yearPicker.value
            binding.calendar.text = selectedYear.toString()
            dialog.dismiss()
        }
    }






}

class MyXAxisValueFormatter(private val xAxisValues: List<Float>) : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val index = value.toInt()
        return if (index >= 0 && index < xAxisValues.size) {
            xAxisValues[index].toString()
        } else {
            ""
        }
    }
}
