package com.money.moneyx.main.addListPage.addIncome

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.money.moneyx.R
import java.text.SimpleDateFormat
import java.util.Calendar

class AddIncomeViewModel : ViewModel() {

    val onClick = MutableLiveData<String>()
    val text = ObservableField("text001")
    var addIncomeModel = ArrayList<AddIncomeModel>()
    val date = getCurrentDate()
    val time = getCurrentTime()

    fun saveClick() {
        onClick.value = "addIncome"
    }

    fun calculateClick() {
        onClick.value = "calculateClick"
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

    fun setDataAdapter(categorySelected: String) {

        addIncomeModel.add(AddIncomeModel("วันที่ เวลา", R.drawable.calendar, date, time))
        addIncomeModel.add(AddIncomeModel("ประเภท", R.drawable.type, "เลือก", time))

        if (categorySelected == "") {
            addIncomeModel.add(AddIncomeModel("หมวดหมู่", R.drawable.category, "เลือก", time))
        } else {
            addIncomeModel.add(
                AddIncomeModel(
                    "หมวดหมู่",
                    R.drawable.category,
                    categorySelected,
                    time
                )
            )
        }

        addIncomeModel.add(AddIncomeModel("โน้ต", R.drawable.note_, "เลือก", time))
        addIncomeModel.add(
            AddIncomeModel(
                "บันทึกอัตโนมัติ",
                R.drawable.autosave_icon,
                "เลือก",
                time
            )
        )

    }
}