package com.money.moneyx.main.homeScreen.fragments.report.expendsReport

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentExpendsReportBinding
import com.money.moneyx.function.ApiReport
import com.money.moneyx.function.dropdownHomePage
import com.money.moneyx.main.addListPage.AddListScreenActivity
import com.money.moneyx.main.homeScreen.HomeActivity
import com.money.moneyx.main.homeScreen.HomeViewModel
import com.money.moneyx.main.homeScreen.fragments.home.HomeFragment
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.ExpendsReportAdapter
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.ReportMonthExpenses
import ir.mahozad.android.PieChart
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.util.Locale


class ExpendsReportFragment(
    private val reportMonthListExpenses: List<ReportMonthExpenses>?,
) : Fragment() {
    private lateinit var binding: FragmentExpendsReportBinding
    private lateinit var expendsReportAdapter: ExpendsReportAdapter
    private lateinit var viewModel: ExpendsViewModel
    private lateinit var viewModelHome: HomeViewModel
    private var unixTime = 0L
    private var expensesNecessary = 0.0
    private var expensesUnNecessary = 0.0
    private var expensesNecessaryGraph = 0.0
    private var expensesUnNecessaryGraph = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_expends_report, container, false)
        viewModel = ViewModelProvider(this)[ExpendsViewModel::class.java]
        viewModelHome = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.expendsViewModel = viewModel

        setData()
        setMonthYear()
        chart()
        setEventClick()



        return binding.root
    }

    private fun setData() {
        viewModel.expendsModel.clear()
        reportMonthListExpenses!!.map {
            val total =
                it.total_Expenses_Necessary.toFloat() + it.total_Expenses_Unnecessary.toFloat()
            expensesNecessary = it.total_Expenses_Necessary.toDouble()
            expensesUnNecessary = it.total_Expenses_Unnecessary.toDouble()
            expensesUnNecessaryGraph = (expensesUnNecessary / total * 100) * 0.01
            expensesNecessaryGraph = (expensesNecessary / total * 100) * 0.01
            viewModel.expendsModel.addAll(it.report_List)
        }
        adapter()
    }

    private fun chart() {
        val pieChart = binding.pieChartExpends
        val color1 = ContextCompat.getColor(requireContext(), R.color.expends_chart_1)
        val color2 = ContextCompat.getColor(requireContext(), R.color.expends_chart_2)

        pieChart.apply {
            slices = listOf(
                PieChart.Slice(expensesNecessaryGraph.toFloat(), color2, legend = "รายจ่ายจำเป็น"),
                PieChart.Slice(
                    expensesUnNecessaryGraph.toFloat(),
                    color1,
                    legend = "รายจ่ายไม่จำเป็น"
                )
            )

        }
    }

    private fun setEventClick() {
        viewModel.onClick.observe(requireActivity(), Observer {
            when (it) {
                "showDropdown" -> dropdownHomePage(
                    requireActivity(),
                    viewModel.onClickDialog,
                    "expends",
                )
            }
        })

    }

    private fun adapter() {
        activity?.runOnUiThread {
            expendsReportAdapter = ExpendsReportAdapter(viewModel.expendsModel) { model ->
                when(model.first){
                    model.first -> {
                        var intent = Intent(requireActivity(), AddListScreenActivity:: class.java)
                        intent.putExtra("edit", "editExpense")
                        intent.putExtra("modelExpenseEdit", model.third)
                        startActivity(intent)
                    }
                }
            }
            binding.RCVpastIncome.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = expendsReportAdapter
                expendsReportAdapter.notifyDataSetChanged()
            }

            if (viewModel.expendsModel.isEmpty()) {
                binding.RCVpastIncome.visibility = View.GONE
                binding.textView21.visibility = View.VISIBLE
            } else {
                binding.RCVpastIncome.visibility = View.VISIBLE
                binding.textView21.visibility = View.GONE
            }

        }

    }

    private fun setMonthYear() {
        binding.textMonth.text = viewModel.currentMonthExpends
        binding.textYear.text = viewModel.currentYearExpends
    }

    private fun setView() {
        viewModel.onClickDialog.observe(requireActivity(), Observer {
            binding.textMonth.text = it.first
            binding.textYear.text = it.second
            unixTime = convertDateTimeToUnixTimestamp(it.third, it.second)
            val localDateTime = unixTimestampToLocalDateTime(unixTime)
            val lastDayOfMonth = YearMonth.from(localDateTime).atEndOfMonth()
            val newStart = convertDateTimeToUnixTimestamp(it.third, it.second).toString()
            val newEnd = convertEndDateTimeToUnixTimestamp(lastDayOfMonth.toString()).toString()
            viewModel.expendsModel.clear()
            callYourApiFunction(newStart, newEnd)
        })
    }


    private fun unixTimestampToLocalDateTime(unixTimestamp: Long): LocalDateTime {
        val instant = Instant.ofEpochSecond(unixTimestamp)
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    private fun convertDateTimeToUnixTimestamp(
        formattedMonth: String,
        formattedYear: String,
    ): Long {
        val dateTimeString = "01/$formattedMonth/$formattedYear 23:59:00"
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        try {
            val date = dateFormat.parse(dateTimeString)
            return date?.time!! / 1000L
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0L
    }

    private fun convertEndDateTimeToUnixTimestamp(formattedMonthYear: String): Long {
        val dateTimeString = "$formattedMonthYear 23:59:00"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        try {
            val date = dateFormat.parse(dateTimeString)
            return date?.time!! / 1000L
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0L
    }

    private fun callYourApiFunction(newStart: String, newEnd: String) {
        ApiReport.startDateTime = newStart
        ApiReport.endDateTime = newEnd
        viewModel.startTimestampEx.set(newStart)
        viewModel.endTimestampEx.set(newEnd)
        AVLoading.startAnimLoading()
        viewModel.reportMonth(requireActivity()) { model ->
            AVLoading.stopAnimLoading()
            viewModelHome.reportMonth = model
            model.data.map { data ->
                activity?.runOnUiThread {
                    HomeFragment.summary.value =
                        Triple(data.totalBalance, data.totalIncome, data.totalExpenses)
                }
                data.report_month_list_Expenses.map { reportMonthExpends ->
                    val total =
                        reportMonthExpends.total_Expenses_Necessary.toFloat() + reportMonthExpends.total_Expenses_Unnecessary.toFloat()
                    expensesNecessary = reportMonthExpends.total_Expenses_Necessary.toDouble()
                    expensesUnNecessary = reportMonthExpends.total_Expenses_Unnecessary.toDouble()
                    expensesNecessaryGraph = (expensesNecessary / total * 100) * 0.01
                    expensesUnNecessaryGraph = (expensesUnNecessary / total * 100) * 0.01
                    chart()
                    viewModel.expendsModel.addAll(reportMonthExpends.report_List)
                }
            }
            adapter()

        }

    }


}