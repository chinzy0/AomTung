package com.money.moneyx.main.addListPage.addIncome

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentAddIncomeBinding
import com.money.moneyx.main.addListPage.ReportViewModel
import com.money.moneyx.main.addListPage.category.CategoryIncomeActivity
import com.money.moneyx.main.addListPage.calculator.CalculatorActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale



class AddIncomeFragment : Fragment() {
    private lateinit var binding: FragmentAddIncomeBinding
    private lateinit var addIncomeAdapter: AddIncomeAdapter
    private var addIncomeModel = ArrayList<AddIncomeModel>()
    private lateinit var viewModel: ReportViewModel
    private val date = getCurrentDateTime()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_income,container,false)
        viewModel = ViewModelProvider(this)[ReportViewModel::class.java]
        binding.reportViewModel = viewModel

        setDataAdapter()
        addAdapter()
        setEventClick()




        return binding.root
    }

    private fun setDataAdapter() {
        addIncomeModel.add(AddIncomeModel("วันที่ เวลา",R.drawable.calendar,date))
        addIncomeModel.add(AddIncomeModel("ประเภท",R.drawable.type,"เลือก"))
        addIncomeModel.add(AddIncomeModel("หมวดหมู่",R.drawable.category,"เลือก"))
        addIncomeModel.add(AddIncomeModel("โน้ต",R.drawable.note_,"เลือก"))
        addIncomeModel.add(AddIncomeModel("บันทึกอัตโนมัติ",R.drawable.autosave_icon,"เลือก" ))
    }

    private fun setEventClick() {
        viewModel.onClick.observe(requireActivity(), Observer {
            when (it) {
                "calculateClick" -> {
                    val intent = Intent(requireActivity(), CalculatorActivity::class.java)
                    intent.putExtra("status", "true")
                    resultActivityAppointment.launch(intent)
                }
            }
        })
    }

    private fun addAdapter(){
        addIncomeAdapter = AddIncomeAdapter(addIncomeModel) {
            Log.i("asdsadasd",it)
            when(it) {
                "วันที่ เวลา" -> {
                    dateTime()
                }
                "ประเภท" -> {
                    selectType()
                }
                "หมวดหมู่" -> {
                    selectCategory()
                }
                "โน้ต" -> {
                    note()
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

    private val resultActivityAppointment =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val x = data.getIntExtra("number",0).toString()


                    binding.textView14.setText(x)


                }
            }
        }

    private val getCategory =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val categorySelected = data.getStringExtra("Category").toString()
                    Log.i("categorySelected",categorySelected)
                    addIncomeModel.clear()
                    addIncomeModel.add(AddIncomeModel("วันที่ เวลา",R.drawable.calendar,date))
                    addIncomeModel.add(AddIncomeModel("ประเภท",R.drawable.type,"เลือก"))
                    addIncomeModel.add(AddIncomeModel("หมวดหมู่",R.drawable.category,categorySelected))
                    addIncomeModel.add(AddIncomeModel("โน้ต",R.drawable.note_,"เลือก"))
                    addIncomeModel.add(AddIncomeModel("บันทึกอัตโนมัติ",R.drawable.autosave_icon,"เลือก" ))
                    addAdapter()



                }
            }
        }

    private fun note(){
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


        note.addTextChangedListener(object : TextWatcher{
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

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            showTimePicker()
        }, year, month, dayOfMonth)
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val currentTime = getCurrentDateTime()

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
        builder.setTitle("Select Time")
        builder.setPositiveButton("OK") { dialog, _ ->
            val selectedHour = numberPickerHour.value
            val selectedMinute = numberPickerMinute.value

            val selectedTime = Calendar.getInstance()
            selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour)
            selectedTime.set(Calendar.MINUTE, selectedMinute)
            val dateFormat = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault())
            val formattedDateTime = dateFormat.format(selectedTime.time)
            Log.i("formattedDateTime", formattedDateTime)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }





    private fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm")
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
        val intent = Intent(requireActivity(), CategoryIncomeActivity::class.java)
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