package com.money.moneyx.main.incomeExpends.listPage.listDay

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.FragmentListDayBinding
import com.money.moneyx.main.addListPage.AddListScreenActivity
import com.money.moneyx.main.autoSave.AutoSaveAdapter
import com.money.moneyx.main.incomeExpends.listPage.ListPageAdapter
import com.money.moneyx.main.incomeExpends.summary.ReportALL
import com.money.moneyx.main.incomeExpends.summary.SummaryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ListDayFragment : Fragment() {
    private lateinit var binding : FragmentListDayBinding
    private lateinit var viewModel: SummaryViewModel
    private lateinit var listPageAdapter: ListPageAdapter
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_day, container, false)
        viewModel = ViewModelProvider(this)[SummaryViewModel::class.java]
        binding.summaryViewModel = viewModel
        val preferences = Preference.getInstance(requireActivity())
        idMember = preferences.getInt("idmember", 0)

        callAPI()


        return binding.root
    }

    private fun adapter() {
        listPageAdapter = ListPageAdapter(viewModel.listDay) { model ->


        }
        binding.RCVday.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listPageAdapter
            listPageAdapter.notifyDataSetChanged()
        }
        if (viewModel.listDay.isEmpty()) {
            binding.RCVday.visibility = View.GONE
        } else {
            binding.RCVday.visibility = View.VISIBLE
        }
    }

    private fun callAPI() {
        binding.calendar.text = viewModel.date
        binding.calendar2.text = viewModel.date
        binding.calendar.textSize = 20f
        binding.calendar.setOnClickListener {
            openCalendarPicker(binding.calendar.text.toString())
        }
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
                    viewModel.reportSummary(idmember = idMember, datatype = "day", end_timestamp = unixTimeEnd, start_timestamp = unixTimeStart,) { model ->
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
                    viewModel.reportListSummary(
                        idmember = idMember,
                        datatype = "day",
                        end_timestamp = unixTimeEnd,
                        start_timestamp = unixTimeStart,
                    ){ listDay ->
                        viewModel.listDay.clear()
                        listDay.data.map {
                            it.report_List_ALL.map { map ->
                                viewModel.listDay.add(map)
                                activity?.runOnUiThread {
                                    adapter()
                                }
                            }
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
        viewModel.reportListSummary(
            idmember = idMember,
            datatype = "day",
            end_timestamp = unixTimeEnd,
            start_timestamp = unixTimeStart,
        ){ listDay ->
            viewModel.listDay.clear()
           listDay.data.map {
               it.report_List_ALL.map { map ->
                   viewModel.listDay.add(map)
                   activity?.runOnUiThread {
                       adapter()
                   }
               }
           }
        }


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
            R.style.DialogTheme,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("d MMMM yyyy",  Locale("th","TH"))
                val formattedDate = dateFormat.format(selectedDate.time)
                binding.calendar.text = formattedDate
                binding.calendar2.text = formattedDate
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
        binding.calendar2.text = formattedDate

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
        binding.calendar2.text = formattedDate
    }

    private fun convertDateStringToUnixTime(dateString: String): Long {
        return try {
            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("th","TH"))
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY, 7)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 1)
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


}