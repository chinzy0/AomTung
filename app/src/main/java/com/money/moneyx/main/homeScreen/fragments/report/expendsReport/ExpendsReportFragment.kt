package com.money.moneyx.main.homeScreen.fragments.report.expendsReport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentExpendsReportBinding
import com.money.moneyx.function.dropdownHomePage
import ir.mahozad.android.PieChart


class ExpendsReportFragment : Fragment() {
    private lateinit var binding: FragmentExpendsReportBinding
    private lateinit var viewModel: ExpendsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_expends_report,container,false)
        viewModel = ViewModelProvider(this)[ExpendsViewModel::class.java]
        binding.expendsViewModel = viewModel

        showPieChart()
        setView()
        setMonth()
        setYear()
        setEventClick()



        return binding.root
    }

    private fun setView() {
        viewModel.onClickDialog.observe(requireActivity(), Observer {
            binding.textMonth.text = it.first
            binding.textYear.text = it.second
        })
    }

    private fun showPieChart(){
        val pieChart = binding.pieChartExpends
        val color1 = ContextCompat.getColor(requireContext(), R.color.expends_chart_1)
        val color2 = ContextCompat.getColor(requireContext(), R.color.expends_chart_2)

        pieChart.apply {
            slices = listOf(
                PieChart.Slice(0.5f, color2, legend = "รายจ่ายจำเป็น"),
                PieChart.Slice(0.5f, color1, legend = "รายจ่ายไม่จำเป็น"),


                )

        }
    }

    private fun setEventClick() {
        viewModel.onClick.observe(requireActivity(), Observer {
            when (it) {
                "showDropdown" -> dropdownHomePage(
                    requireActivity(),
                    viewModel.onClickDialog,
                )
            }
        })

    }


    private fun setMonth() {
        binding.textMonth.text = viewModel.currentMonthExpends
    }

    private fun setYear() {
        binding.textYear.text = viewModel.currentYearExpends

    }






}