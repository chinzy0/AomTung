package com.money.moneyx.main.homeScreen.fragments.report.incomeReport

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentIncomeReportBinding
import ir.mahozad.android.PieChart
import java.text.SimpleDateFormat
import java.util.Calendar


class IncomeReportFragment : Fragment() {
    private lateinit var binding: FragmentIncomeReportBinding
    private lateinit var incomeAdapter: IncomeReportAdapter
    private lateinit var dropdownAdapter: DropdownAdapter
    private var dropdownModel = ArrayList<DropdownModel>()
    private var incomeModel = ArrayList<IncomeReportModel>()
    private val currentMonth = getCurrentMonth()
    private val currentYear = getCurrentYear()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_income_report,container,false)

        adapter()
        chart()
        showDropdown()
        setMonth()
        setYear()



        return binding.root
    }
    private fun adapter(){
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
    }

    private fun chart(){
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

    private fun showDropdown() {
        binding.dropDownMonth.setOnClickListener {
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dropdown_month)
            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setGravity(Gravity.BOTTOM)

            val months = arrayOf("มกราคม" , "กุมภาพันธ์"  ,"มีนาคม"  ,"เมษายน" , "พฤษภาคม" , "มิถุนายน" , "กรกฎาคม", "สิงหาคม",
                "กันยายน", " ตุลาคม ", "พฤศจิกายน" ,"ธันวาคม")

            val monthPicker = dialog.findViewById<NumberPicker>(R.id.monthPicker)
            monthPicker.minValue = 0
            monthPicker.maxValue = months.size - 1
            monthPicker.displayedValues = months

            val yearPicker = dialog.findViewById<NumberPicker>(R.id.yearPicker)
            val yearNow = Calendar.getInstance().get(Calendar.YEAR)

            yearPicker.minValue = yearNow-100
            yearPicker.maxValue = yearNow+100
            yearPicker.displayedValues


            val calendar = Calendar.getInstance()
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)

            monthPicker.value = currentMonth
            yearPicker.value = currentYear

            val submit = dialog.findViewById<ConstraintLayout>(R.id.buttonSubmitMonthYear)
            submit.setOnClickListener {
                
                dialog.dismiss()
            }
        }
    }


    private fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMMM")
        return dateFormat.format(calendar.time)

    }
    private fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy")
        return dateFormat.format(calendar.time)

    }

    private fun setMonth(){
        binding.textMonth.text = currentMonth
    }
    private fun setYear(){
        binding.textYear.text = currentYear

    }







}