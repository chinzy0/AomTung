package com.money.moneyx.main.homeScreen.fragments.report.incomeReport

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentIncomeReportBinding
import com.money.moneyx.function.dropdownHomePage
import ir.mahozad.android.PieChart
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class IncomeReportFragment : Fragment() {
    private lateinit var binding: FragmentIncomeReportBinding
    private lateinit var incomeAdapter: IncomeReportAdapter
    private lateinit var viewModel: IncomeViewModel
    private var startDate = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_income_report, container, false)
        viewModel = ViewModelProvider(this)[IncomeViewModel::class.java]
        binding.incomeViewModel = viewModel

        adapter()
        chart()
        setMonth()
        setYear()
        setEventClick()
        setView()

        return binding.root
    }


    private fun chart() {
        val pieChart = binding.pieChartIncome

        val color1 = ContextCompat.getColor(requireContext(), R.color.income_chart_1)
        val color2 = ContextCompat.getColor(requireContext(), R.color.income_chart_2)

        pieChart.apply {
            slices = listOf(
                PieChart.Slice(0.7f, color2, legend = "รายรับแน่นอน"),
                PieChart.Slice(0.3f, color1, legend = "รายรับไม่แน่นอน"),

                )

        }
    }

    private fun adapter() {
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

    private fun setMonth() {
        binding.textMonth.text = viewModel.currentMonthIncome
    }

    private fun setYear() {
        binding.textYear.text = viewModel.currentYearIncome

    }

    private fun setEventClick() {
        viewModel.onClick.observe(requireActivity(), Observer {
            when (it) {
                "showDropdown" -> dropdownHomePage(requireActivity(), viewModel.onClickDialog)

            }
        })
    }

    private fun setView() {
        viewModel.onClickDialog.observe(requireActivity(), Observer {
            binding.textMonth.text = it.first
            binding.textYear.text = it.second

        })
    }


    private fun convertDateTimeToUnixTimestamp(formattedMonth: String, formattedYear: String): Long {
        val dateTimeString = "$formattedMonth $formattedYear"

        val dateFormat = SimpleDateFormat("01/MM/yyyy 00:00:00", Locale.getDefault())


        try {
            val date = dateFormat.parse(dateTimeString)
            return date?.time!! / 1000L
        } catch (e: Exception) {

            e.printStackTrace()
        }

        return 0L
    }

}