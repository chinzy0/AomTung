package com.money.moneyx.function

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.size
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.money.moneyx.R
import com.money.moneyx.main.addListPage.addExpends.GetAllTypeExpenses
import com.money.moneyx.main.addListPage.addIncome.GetAllTypeIncome
import com.money.moneyx.main.addListPage.addIncome.ListScheduleAuto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//private lateinit var selectTypeDialogAdapter: SelectTypeDialogAdapter
fun showExitDialog(mContext: Activity, onClickDialog: MutableLiveData<String>) {
    val dialog = Dialog(mContext)
    dialog.setCanceledOnTouchOutside(false)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.exit_dialog)
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


    dialog.show()
    val cancel = dialog.findViewById<ConstraintLayout>(R.id.cancle_exit_button)
    cancel.setOnClickListener {
        dialog.dismiss()
    }
    val exit = dialog.findViewById<ConstraintLayout>(R.id.confirm_exit_button)
    exit.setOnClickListener {
        onClickDialog.value = "confirm"
    }
}

fun wrongOtpDialog(mContext: Activity, message: String) {
    val dialog = Dialog(mContext)
    dialog.setCanceledOnTouchOutside(false)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.alert_otp)
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()
    val textMessage = dialog.findViewById<TextView>(R.id.textWrongOTP)
    textMessage.text = message
    val ok = dialog.findViewById<TextView>(R.id.okDialog)
    ok.setOnClickListener {
        dialog.dismiss()
    }
}
fun addListAlertDialog(mContext: Activity) {
    val dialog = Dialog(mContext)
    dialog.setCanceledOnTouchOutside(false)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.addlist_alert)
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()
    val ok = dialog.findViewById<TextView>(R.id.addlistOkButton)
    ok.setOnClickListener {
        dialog.dismiss()
    }
}



fun dropdownHomePage(
    mContext: Activity,
    onClickDialog: MutableLiveData<Pair<String, String>>,
) {
    val dialog = Dialog(mContext)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.dropdown_month)
    dialog.show()
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.setGravity(Gravity.BOTTOM)

    val months = arrayOf(
        "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม",
        "กันยายน", " ตุลาคม ", "พฤศจิกายน", "ธันวาคม"
    )

    val monthPicker = dialog.findViewById<NumberPicker>(R.id.monthPicker)
    monthPicker.minValue = 0
    monthPicker.maxValue = months.size - 1
    monthPicker.displayedValues = months



    val yearPicker = dialog.findViewById<NumberPicker>(R.id.yearPicker)
    val yearNow = Calendar.getInstance().get(Calendar.YEAR)
    yearPicker.minValue = yearNow - 100
    yearPicker.maxValue = yearNow + 100
    yearPicker.displayedValues


    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)
    monthPicker.value = currentMonth
    yearPicker.value = currentYear


    val submit = dialog.findViewById<ConstraintLayout>(R.id.buttonSubmitMonthYear)
    submit.setOnClickListener {
        val selectedMonthYear = Calendar.getInstance()
        selectedMonthYear.set(Calendar.MONTH, monthPicker.value)
        selectedMonthYear.set(Calendar.MONTH, monthPicker.value)
        val dateFormat = SimpleDateFormat(" MMMM-yyyy", Locale.getDefault())
        val monthFormat = SimpleDateFormat(" MM", Locale.getDefault())
        val formattedMonthYear = dateFormat.format(selectedMonthYear.time)
        val formattedMonth = monthFormat.format(selectedMonthYear.time)
        Log.i("formattedMonth", formattedMonth)
        onClickDialog.value = Pair(months[monthPicker.value], yearPicker.value.toString())

        dialog.dismiss()
    }
}

fun note(mContext: Activity, noted: String, noteText: (Any) -> Unit) {
    //โน๊ต
    val dialog = Dialog(mContext)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.addlist_note)
    dialog.show()
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.setGravity(Gravity.BOTTOM)
    dialog.setCanceledOnTouchOutside(false)
    val note = dialog.findViewById<EditText>(R.id.textNote)
    val counter = dialog.findViewById<TextView>(R.id.textCount)
    val button = dialog.findViewById<Button>(R.id.buttonSave)
    note.setText(noted)
    note.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val remainingCharacters = (50 - s?.length!!)
            counter?.text = remainingCharacters.toString()
        }
        override fun afterTextChanged(s: Editable?) {}
    })
    button.setOnClickListener {
        noteText(note.text.toString())
        //ปิดคีย์บอร์ด
        val inputMethodManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val windowToken = dialog.currentFocus?.windowToken ?: button.windowToken
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        dialog.dismiss()
    }
}

fun dateTime(mContext: Activity, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        mContext,
        R.style.DialogTheme,
        { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            Log.i("formattedDate", formattedDate)
            onDateSelected(formattedDate)
        },
        year,
        month,
        dayOfMonth
    )
    datePickerDialog.show()
}
fun dateTimeExpends(mContext: Activity, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        mContext,
        R.style.DialogThemeEx,
        { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            Log.i("formattedDate", formattedDate)
            onDateSelected(formattedDate)
        },
        year,
        month,
        dayOfMonth
    )
    datePickerDialog.show()
}


fun showTimePicker(mContext: Activity,onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val inflater = LayoutInflater.from(mContext)
    val view = inflater.inflate(R.layout.time_picker_dialog, null)

    view.findViewById<NumberPicker>(R.id.hourPicker).apply {
        minValue = 0
        maxValue = 23
        value = calendar.get(Calendar.HOUR_OF_DAY)
    }

    view.findViewById<NumberPicker>(R.id.minutePicker).apply {
        minValue = 0
        maxValue = 59
        value = calendar.get(Calendar.MINUTE)
    }

    val builder = AlertDialog.Builder(mContext)
    builder.setView(view)

    builder.setPositiveButton("ยืนยัน") { dialog, _ ->
        val selectedHour = view.findViewById<NumberPicker>(R.id.hourPicker).value
        val selectedMinute = view.findViewById<NumberPicker>(R.id.minutePicker).value

        val selectedTime = Calendar.getInstance()
        selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour)
        selectedTime.set(Calendar.MINUTE, selectedMinute)

        val timeFormat = SimpleDateFormat("HH:mm")
        val formattedTime = timeFormat.format(selectedTime.time)
        Log.i("formattedTime", formattedTime)
        onTimeSelected(formattedTime)

    }


    builder.setNegativeButton("ยกเลิก") { dialog, _ ->
        dialog.dismiss()
    }

    builder.show()
}

fun selectType(
    mContext: Activity, modelData: GetAllTypeIncome?,
    onTypeSelected: (Pair<String,Int>) -> Unit) {
    val dialog = Dialog(mContext)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.select_type_dialog)
    dialog.show()

    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.setGravity(Gravity.BOTTOM)


    val selectTypeDialogAdapter = SelectTypeDialogAdapter(modelData!!.data){
       model ->
        when(model.first) {
            "รายรับแน่นอน" -> {
                onTypeSelected(Pair(model.first,model.second))
                dialog.dismiss()
            }
            "รายรับไม่แน่นอน" -> {
                onTypeSelected(Pair(model.first,model.second,))
                dialog.dismiss()
            }
        }

    }
    val selectDialogRCV = dialog.findViewById<RecyclerView>(R.id.RcvListMenuSelect) ?: RecyclerView(mContext)
    selectDialogRCV.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = selectTypeDialogAdapter
        selectTypeDialogAdapter.notifyDataSetChanged()
    }
}

fun selectTypeExpends(
    mContext: Activity, modelData: GetAllTypeExpenses?,
    onTypeSelected: (Pair<String,Int>) -> Unit) {
    val dialog = Dialog(mContext)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.select_type_dialog)
    dialog.show()

    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.setGravity(Gravity.BOTTOM)


    val selectTypeDialogAdapter = SelectTypeExpendsDialogAdapter(modelData!!.data){
            model ->
        when(model.first) {
            "รายจ่ายจำเป็น" -> {
                onTypeSelected(Pair(model.first,model.second))
                dialog.dismiss()
            }
            "รายจ่ายไม่จำเป็น" -> {
                onTypeSelected(Pair(model.first,model.second,))
                dialog.dismiss()
            }
        }

    }
    val selectDialogRCV = dialog.findViewById<RecyclerView>(R.id.RcvListMenuSelect) ?: RecyclerView(mContext)
    selectDialogRCV.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = selectTypeDialogAdapter
        selectTypeDialogAdapter.notifyDataSetChanged()
    }
}

fun autoSave(
    mContext: Activity,
    modelData: ListScheduleAuto?,
    onAutoSaveSelected: (Pair<String,Int>) -> Unit
) {
    val dialog = Dialog(mContext)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.select_type_dialog)
    dialog.show()
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.setGravity(Gravity.BOTTOM)

    val selectAutoSaveDialogAdapter = SelectAutoSaveDialogAdapter(modelData!!.data){ model ->
        when(model.first) {
            "ไม่มี" -> {
                onAutoSaveSelected(Pair(model.first,model.second))
                dialog.dismiss()
            }
            "ทุกวัน" -> {
                onAutoSaveSelected(Pair(model.first,model.second))
                dialog.dismiss()
            }
            "ทุกสัปดาห์" -> {
                onAutoSaveSelected(Pair(model.first,model.second))
                dialog.dismiss()
            }
            "ทุกเดือน" -> {
                onAutoSaveSelected(Pair(model.first,model.second))
                dialog.dismiss()
            }
            "ทุก3เดือน" -> {
                onAutoSaveSelected(Pair(model.first,model.second))
                dialog.dismiss()
            }
        }
    }
    val selectDialogRCV = dialog.findViewById<RecyclerView>(R.id.RcvListMenuSelect) ?: RecyclerView(mContext)
    selectDialogRCV.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = selectAutoSaveDialogAdapter
        selectAutoSaveDialogAdapter.notifyDataSetChanged()
    }
}

fun dialogOtp(mContext: Activity, data: String, callback: (String) -> Unit) {

    val dialog = Dialog(mContext)
    dialog.setCanceledOnTouchOutside(false)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.send_otp)
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val otp = dialog.findViewById<TextView>(R.id.textOTP)
    otp.text = data
    val ok = dialog.findViewById<TextView>(R.id.okOtp)
    ok.setOnClickListener {
        callback.invoke(data)
        dialog.dismiss()
    }
    dialog.show()
}