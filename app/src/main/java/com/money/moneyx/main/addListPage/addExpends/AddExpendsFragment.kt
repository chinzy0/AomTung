package com.money.moneyx.main.addListPage.addExpends

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentAddExpendsBinding
import com.money.moneyx.main.addListPage.ReportViewModel
import com.money.moneyx.main.addListPage.addIncome.AddIncomeAdapter
import com.money.moneyx.main.addListPage.addIncome.AddIncomeModel
import com.money.moneyx.main.addListPage.calculator.CalculatorActivity
import com.money.moneyx.main.addListPage.category.CategoryExpendsActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddExpendsFragment : Fragment() {
    private lateinit var binding: FragmentAddExpendsBinding
    private lateinit var addIncomeAdapter: AddIncomeAdapter
    private var addIncomeModel = ArrayList<AddIncomeModel>()
    private lateinit var viewModel: ReportViewModel
    private val date = getCurrentDate()
    private val time = getCurrentTime()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_expends,container,false)
        viewModel = ViewModelProvider(this)[ReportViewModel::class.java]
        binding.reportViewModel = viewModel

        setDataAdapter()
        addAdapter()
        setEventClick()




        return binding.root
    }

    private fun setEventClick() {
        viewModel.onClick.observe(requireActivity(), Observer {
            when (it) {
                "calculateClick" -> {
                    val intent = Intent(requireActivity(), CalculatorActivity::class.java)

                    resultActivityAppointment.launch(intent)
                }
            }
        })
    }

    private val resultActivityAppointment =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val x = data.getIntExtra("number",0).toString()
                    binding.textView14.setText(x)

                }
            }
        }
    private fun setDataAdapter() {
        addIncomeModel.add(AddIncomeModel("วันที่ เวลา", R.drawable.calendar, date, time))
        addIncomeModel.add(AddIncomeModel("ประเภท", R.drawable.type, "เลือก", time))
        addIncomeModel.add(AddIncomeModel("หมวดหมู่", R.drawable.category, "เลือก", time))
        addIncomeModel.add(AddIncomeModel("โน้ต", R.drawable.note_, "เลือก", time))
        addIncomeModel.add(AddIncomeModel(
            "บันทึกอัตโนมัติ",
            R.drawable.autosave_icon,
            "เลือก",
            time
        ))
    }
    private fun addAdapter(){
        addIncomeAdapter = AddIncomeAdapter(addIncomeModel) {
            Log.i("asdsadasd",it.first)
            when(it.first) {
                "วันที่ เวลา" -> {

                    if(it.second == "date"){
                        dateTime()
                    }else if (it.second == "time"){
                        showTimePicker()
                    }else{
                        dateTime()
                    }
                }
                "ประเภท" -> {
                    selectType()
                }
                "หมวดหมู่" -> {
                    selectCategory()
                }
                "โน้ต" -> {
                    showNote()
                }
                "บันทึกอัตโนมัติ" -> {
                    autoSave()
                }
            }
        }
        binding.RCVincome.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = addIncomeAdapter
            addIncomeAdapter.notifyDataSetChanged()
        }
    }

    private val getCategory =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val categorySelected = data.getStringExtra("Category").toString()
                    Log.i("categorySelected",categorySelected)
                    addIncomeModel.clear()
                    addIncomeModel.add(AddIncomeModel(
                        "วันที่ เวลา",
                        R.drawable.calendar,
                        date,
                        time
                    ))
                    addIncomeModel.add(AddIncomeModel("ประเภท", R.drawable.type, "เลือก", time))
                    addIncomeModel.add(AddIncomeModel(
                        "หมวดหมู่",
                        R.drawable.category,
                        categorySelected,
                        time
                    ))
                    addIncomeModel.add(AddIncomeModel("โน้ต", R.drawable.note_, "เลือก", time))
                    addIncomeModel.add(AddIncomeModel(
                        "บันทึกอัตโนมัติ",
                        R.drawable.autosave_icon,
                        "เลือก",
                        time
                    ))
                    addAdapter()



                }
            }
        }
    private fun showNote(){
        //โน๊ต
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.addlist_note)
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)

        var note = dialog.findViewById<EditText>(R.id.textNote)
        var counter = dialog.findViewById<TextView>(R.id.textCount)
        var button = dialog.findViewById<Button>(R.id.buttonSave)
        val colorButtonEnable = ContextCompat.getColor(requireActivity(), R.color.button)
        val colorButtonDisable = ContextCompat.getColor(requireActivity(), R.color.button_disable)


        note.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val remainingCharacters = (50 - s?.length!!) ?: 0
                counter?.text = remainingCharacters.toString()
                Log.i("asdasda",s.toString())
                if (s.isNotEmpty()){
                    button.isEnabled = true
                    button.backgroundTintList = ColorStateList.valueOf(colorButtonEnable)
                }else{
                    button.isEnabled = false
                    button.backgroundTintList = ColorStateList.valueOf(colorButtonDisable)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        button.setOnClickListener {
            dialog.dismiss()
        }


    }
    private fun dateTime() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), R.style.DialogThemeEx, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            showTimePicker()
        }, year, month, dayOfMonth)
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val currentTime = getCurrentTime()

        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.time_picker_dialog, null)
        val numberPickerHour: NumberPicker = view.findViewById(R.id.hourPicker)
        val numberPickerMinute: NumberPicker = view.findViewById(R.id.minutePicker)

        numberPickerHour.minValue = 0
        numberPickerHour.maxValue = 23
        numberPickerMinute.minValue = 0
        numberPickerMinute.maxValue = 59


        numberPickerHour.value = calendar.get(Calendar.HOUR_OF_DAY)
        numberPickerMinute.value = calendar.get(Calendar.MINUTE)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        builder.setPositiveButton("OK") { dialog, _ ->
            val selectedHour = numberPickerHour.value
            val selectedMinute = numberPickerMinute.value

            val selectedTime = Calendar.getInstance()
            selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour)
            selectedTime.set(Calendar.MINUTE, selectedMinute)
            val dateFormat = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault())
            val formattedDateTime = dateFormat.format(selectedTime.time)
            Log.i("formattedDateTimeEx", formattedDateTime)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()

        }

        builder.show()
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(calendar.time)
    }
    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm")
        return dateFormat.format(calendar.time)
    }
    private fun selectType() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.select_type_dialog)
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)

        var necessaryExpenses = dialog.findViewById<TextView>(R.id.necessary_expenses)
        var unnecessaryExpenses = dialog.findViewById<TextView>(R.id.Unnecessary_expenses)

        necessaryExpenses.setOnClickListener {
            dialog.dismiss()
        }

        unnecessaryExpenses.setOnClickListener {
            dialog.dismiss()
        }


    }
    private fun selectCategory(){
        val intent = Intent(requireActivity(), CategoryExpendsActivity::class.java)
        getCategory.launch(intent)
    }
    private fun autoSave(){
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.autosave_dailog)
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)

        var none = dialog.findViewById<TextView>(R.id.none)
        var everyday = dialog.findViewById<TextView>(R.id.everyday)
        var every_week = dialog.findViewById<TextView>(R.id.every_week)
        var every_month = dialog.findViewById<TextView>(R.id.every_month)
        var every_3month = dialog.findViewById<TextView>(R.id.every_3month)


        none.setOnClickListener {
            dialog.dismiss()
        }

        everyday.setOnClickListener {
            dialog.dismiss()
        }
        every_week.setOnClickListener {
            dialog.dismiss()
        }
        every_month.setOnClickListener {
            dialog.dismiss()
        }
        every_3month.setOnClickListener {
            dialog.dismiss()
        }

    }





}