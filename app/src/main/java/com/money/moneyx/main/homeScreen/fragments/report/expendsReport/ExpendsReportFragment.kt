package com.money.moneyx.main.homeScreen.fragments.report.expendsReport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentExpendsReportBinding
import ir.mahozad.android.PieChart


class ExpendsReportFragment : Fragment() {
    private lateinit var binding: FragmentExpendsReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_expends_report,container,false)
        val pieChart = binding.pieChartExpends

        val color1 = ContextCompat.getColor(requireContext(), R.color.expends_chart_1)
        val color2 = ContextCompat.getColor(requireContext(), R.color.expends_chart_2)

        pieChart.apply {
            slices = listOf(
                PieChart.Slice(0.5f, color2, legend = "รายจ่ายจำเป็น"),
                PieChart.Slice(0.5f, color1, legend = "รายจ่ายไม่จำเป็น"),


                )

        }


        return binding.root
    }



}