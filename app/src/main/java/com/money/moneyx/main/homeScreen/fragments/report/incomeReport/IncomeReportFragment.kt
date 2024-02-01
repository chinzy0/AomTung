package com.money.moneyx.main.homeScreen.fragments.report.incomeReport

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentIncomeReportBinding
import com.money.moneyx.function.dropdownHomePage
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.main.homeScreen.HomeViewModel
import com.money.moneyx.main.homeScreen.fragments.home.HomeFragment
import ir.mahozad.android.PieChart
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.util.Locale


class IncomeReportFragment(
    private val reportMonthListIncome: List<ReportMonthIncome>?,

    ) :
    Fragment() {
    private lateinit var binding: FragmentIncomeReportBinding
    private lateinit var incomeAdapter: IncomeReportAdapter
    private lateinit var viewModel: IncomeViewModel
    private lateinit var viewModelHome: HomeViewModel
    private var startDateTime = ""
    private var incomeCertain = 0.0
    private var incomeUnCertain = 0.0
    private var incomeCertainGraph = 0.0
    private var incomeUnCertainGraph = 0.0
    private var unixTime = 0L
    private var homeFragment: HomeFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        homeFragment = requireActivity().supportFragmentManager.findFragmentByTag("HomeFragmentTag") as? HomeFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_income_report, container, false)
        viewModel = ViewModelProvider(this)[IncomeViewModel::class.java]
        viewModelHome = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.incomeViewModel = viewModel
        loadingScreen(requireActivity())

        setData()
        setMonthYear()
        chart()
        setEventClick()



        return binding.root
    }

    private fun setData() {
        viewModel.incomeModel.clear()
        reportMonthListIncome!!.map {
            val total = it.total_Income_Certain.toFloat() + it.total_Income_Uncertain.toFloat()
            incomeCertain = it.total_Income_Certain.toDouble()
            incomeUnCertain = it.total_Income_Uncertain.toDouble()
            incomeCertainGraph = (incomeCertain / total * 100) * 0.01
            incomeUnCertainGraph = (incomeUnCertain / total * 100) * 0.01
            viewModel.incomeModel.addAll(it.report_List)
        }
        adapter()
    }


    private fun chart() {
        val pieChart = binding.pieChartIncome
        val color1 = ContextCompat.getColor(requireContext(), R.color.income_chart_1)
        val color2 = ContextCompat.getColor(requireContext(), R.color.income_chart_2)
        pieChart.apply {
            slices = listOf(
                PieChart.Slice(incomeCertainGraph.toFloat(), color2, legend = "รายรับแน่นอน"),
                PieChart.Slice(incomeUnCertainGraph.toFloat(), color1, legend = "รายรับไม่แน่นอน"),
            )
        }
    }

    private fun adapter() {
        activity?.runOnUiThread {
            incomeAdapter = IncomeReportAdapter(viewModel.incomeModel) {
            }
            binding.RCVpastIncome.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = incomeAdapter
                incomeAdapter.notifyDataSetChanged()
            }

            if (viewModel.incomeModel.isEmpty()) {
                binding.RCVpastIncome.visibility = View.GONE
                binding.textView21.visibility = View.VISIBLE
            } else {
                binding.RCVpastIncome.visibility = View.VISIBLE
                binding.textView21.visibility = View.GONE
            }

        }
    }

    private fun setMonthYear() {
        binding.textMonth.text = viewModel.currentMonthIncome
        binding.textYear.text = viewModel.currentYearIncome
    }


    private fun setEventClick() {
        viewModel.onClick.observe(requireActivity(), Observer {
            when (it) {
                "showDropdown" -> dropdownHomePage(
                    requireActivity(),
                    viewModel.onClickDialog,
                    page = "income"
                )
            }
        })
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
            viewModel.incomeModel.clear()

            callYourApiFunction(newStart, newEnd)
        })
    }

    private fun callYourApiFunction(newStart: String, newEnd: String) {
        viewModel.startTimestamp.set(newStart)
        viewModel.endTimestamp.set(newEnd)
        AVLoading.startAnimLoading()
        viewModel.reportMonth(requireActivity()) { model ->
            AVLoading.stopAnimLoading()
            viewModelHome.reportMonth = model
            model.data.map { data ->
                activity?.runOnUiThread{
                    HomeFragment.summary.value =
                        Triple(data.totalBalance, data.totalIncome, data.totalExpenses)
                }
                data.report_month_list_income.map { reportMonthIncome ->
                    viewModel.incomeModel.addAll(reportMonthIncome.report_List)
                }
            }
            adapter()

        }

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


}
