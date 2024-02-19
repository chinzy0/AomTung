package com.money.moneyx.main.incomeExpends.listPage.listYear

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.NumberPicker
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.FragmentListYearBinding
import com.money.moneyx.main.incomeExpends.summary.SummaryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ListYearFragment : Fragment() {
    private lateinit var binding: FragmentListYearBinding
    private lateinit var viewModel: SummaryViewModel
    private var idMember = 0
    private var currentDate = ""
    private var unixTimeStart = 0L
    private var unixTimeEnd = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_year, container, false)
        viewModel = ViewModelProvider(this)[SummaryViewModel::class.java]
        binding.summaryViewModel = viewModel
        val preferences = Preference.getInstance(requireActivity())
        idMember = preferences.getInt("idmember", 0)

        callAPI()

        return binding.root
    }

    private fun callAPI() {
        binding.calendar.text = viewModel.year
        binding.calendar.textSize = 20f
        binding.calendar.setOnClickListener {
            dropdownSelectYearSummaryPage(binding.calendar.text.toString())
        }
        binding.back.setOnClickListener { goBack1Year() }
        binding.forward.setOnClickListener { forward1Year() }
        binding.calendar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {}
            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int,
            ) {}
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
    private fun dropdownSelectYearSummaryPage(monthAndYear: String) {
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
        calendar.time = date!!
        val selectedYear = calendar.get(Calendar.YEAR)

        yearPicker.value = selectedYear

        val submit = dialog.findViewById<ConstraintLayout>(R.id.buttonSubmitMonthYear)

        submit.setOnClickListener {
            val selectedYear = yearPicker.value
            binding.calendar.text = selectedYear.toString()
            dialog.dismiss()
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


}