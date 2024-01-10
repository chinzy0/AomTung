package com.money.moneyx.main.addListPage.addIncome

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
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.money.moneyx.R
import com.money.moneyx.databinding.FragmentAddIncomeBinding
import com.money.moneyx.login.otpScreen.OtpScreenActivity
import com.money.moneyx.main.addListPage.ReportViewModel
import com.money.moneyx.main.addListPage.calculator.CalculatorActivity
import com.money.moneyx.main.homeScreen.HomeActivity
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.DropdownAdapter
import com.money.moneyx.main.homeScreen.fragments.report.incomeReport.DropdownModel


class AddIncomeFragment : Fragment() {
    private lateinit var binding: FragmentAddIncomeBinding
    private lateinit var addIncomeAdapter: AddIncomeAdapter
    private var addIncomeModel = ArrayList<AddIncomeModel>()
    private lateinit var viewModel: ReportViewModel

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

        addIncomeModel.add(AddIncomeModel("วันที่ เวลา",R.drawable.calendar,"22/12/2023,11:17"))
        addIncomeModel.add(AddIncomeModel("ประเภท",R.drawable.type,"เลือก"))
        addIncomeModel.add(AddIncomeModel("หมวดหมู่",R.drawable.category,"เลือก"))
        addIncomeModel.add(AddIncomeModel("โน้ต",R.drawable.note_,"เลือก"))
        addIncomeModel.add(AddIncomeModel("บันทึกอัตโนมัติ",R.drawable.autosave,"เลือก" ))

        addAdapter()
        setEventClick()



        return binding.root
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
                    showDateTimePicker()
                }
                "ประเภท" -> {
                    showTypeSelectionDialog()
                }
                "หมวดหมู่" -> {
                    selectCategory()
                }
                "โน้ต" -> {
                    showNote()
                }
                "บันทึกอัตโนมัติ" -> {

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

    private fun showDateTimePicker() {
        // Implement your date-time picker logic here
        // For instance, open a date-time picker dialog
        val datePickerDialog = DatePickerDialog(requireContext())
        datePickerDialog.setOnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Handle the selected date
            // You can also launch a time picker here if needed
        }
        datePickerDialog.show()
    }

    private fun showTypeSelectionDialog() {

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
        resultActivityAppointment.launch(intent)
    }






}