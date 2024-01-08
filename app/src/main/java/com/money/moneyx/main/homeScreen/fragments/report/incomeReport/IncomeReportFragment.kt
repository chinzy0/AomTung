package com.money.moneyx.main.homeScreen.fragments.report.incomeReport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentIncomeReportBinding
import ir.mahozad.android.PieChart


class IncomeReportFragment : Fragment() {
    private lateinit var binding: FragmentIncomeReportBinding
    private lateinit var incomeAdapter: IncomeReportAdapter
    private var incomeModel = ArrayList<IncomeReportModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_income_report,container,false)

        incomeModel.add(IncomeReportModel("รายรับแน่นอน","เงินเดือน","+1,000.00","22/12/2023 11:17"))
        incomeModel.add(IncomeReportModel("รายรับไม่แน่นอน","ของขวัญ","+500.00","22/12/2023 11:17"))
        incomeModel.add(IncomeReportModel("รายรับไม่แน่นอน","ของขวัญ","+500.00","22/12/2023 11:17"))

        incomeAdapter = IncomeReportAdapter(incomeModel){

        }
        binding.RCVpastIncome.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = incomeAdapter
            incomeAdapter.notifyDataSetChanged()
        }

        if (incomeModel!=null){
            binding.RCVpastIncome.visibility = View.VISIBLE
            binding.textView21.visibility = View.GONE
        }else{
            binding.RCVpastIncome.visibility = View.GONE
            binding.textView21.visibility = View.VISIBLE
        }


        val pieChart = binding.pieChartIncome

        val color1 = ContextCompat.getColor(requireContext(), R.color.income_chart_1)
        val color2 = ContextCompat.getColor(requireContext(), R.color.income_chart_2)

        pieChart.apply {
            slices = listOf(
                PieChart.Slice(0.7f, color2, legend = "รายรับแน่นอน"),
                PieChart.Slice(0.3f, color1, legend = "รายรับไม่แน่นอน"),


            )

        }





        return binding.root
    }

}