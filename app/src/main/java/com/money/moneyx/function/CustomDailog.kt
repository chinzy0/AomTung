package com.money.moneyx.function

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData

import com.money.moneyx.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    var cancel = dialog.findViewById<ConstraintLayout>(R.id.cancle_exit_button)
    cancel.setOnClickListener {
        dialog.dismiss()
    }
    var exit = dialog.findViewById<ConstraintLayout>(R.id.confirm_exit_button)
    exit.setOnClickListener {
        onClickDialog.value = "confirm"

    }
}

fun wrongOtpDialog(mContext: Activity) {
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
    var ok = dialog.findViewById<TextView>(R.id.okDialog)
    ok.setOnClickListener {
        dialog.dismiss()
    }
}



fun dropdownHomePage(mContext: Activity, onClickDialog: MutableLiveData<Pair<String, String>>) {
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
        selectedMonthYear.set(Calendar.YEAR, yearPicker.value)
        val dateFormat = SimpleDateFormat(" MMMM-yyyy", Locale.getDefault())
        val formattedMonthYear = dateFormat.format(selectedMonthYear.time)
        Log.i("dropdownHomePage", formattedMonthYear)
        onClickDialog.value = Pair(months[monthPicker.value], yearPicker.value.toString())

        dialog.dismiss()
    }
}

fun note(mContext: Activity) {
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

    var note = dialog.findViewById<EditText>(R.id.textNote)
    var counter = dialog.findViewById<TextView>(R.id.textCount)
    var button = dialog.findViewById<Button>(R.id.buttonSave)
    val colorButtonEnable = ContextCompat.getColor(mContext, R.color.button)
    val colorButtonDisable = ContextCompat.getColor(mContext, R.color.button_disable)


    note.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val remainingCharacters = (50 - s?.length!!) ?: 0
            counter?.text = remainingCharacters.toString()
            Log.i("asdasda", s.toString())
            if (s.isNotEmpty()) {
                button.isEnabled = true
                button.backgroundTintList = ColorStateList.valueOf(colorButtonEnable)
            } else {
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

fun dateTime(mContext: Activity) {
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
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            Log.i("formattedDate", formattedDate)


        },
        year,
        month,
        dayOfMonth
    )
    datePickerDialog.show()
}

fun showTimePicker(mContext: Activity) {
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

        val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
        val formattedTime = timeFormat.format(selectedTime.time)
        Log.i("formattedTime", formattedTime)


    }


    builder.setNegativeButton("ยกเลิก") { dialog, _ ->
        dialog.dismiss()
    }

    builder.show()
}

fun selectType(mContext: Activity) {
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

    var necessaryExpenses = dialog.findViewById<TextView>(R.id.necessary_expenses)
    var unnecessaryExpenses = dialog.findViewById<TextView>(R.id.Unnecessary_expenses)

    necessaryExpenses.setOnClickListener {
        dialog.dismiss()
    }

    unnecessaryExpenses.setOnClickListener {
        dialog.dismiss()
    }
}
 fun autoSave(mContext: Activity){
    val dialog = Dialog(mContext)
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