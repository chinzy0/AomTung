package com.money.moneyx.main.addListPage.addExpends

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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


class AddExpendsFragment : Fragment() {
    private lateinit var binding: FragmentAddExpendsBinding
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_expends,container,false)
        viewModel = ViewModelProvider(this)[ReportViewModel::class.java]
        binding.reportViewModel = viewModel

        addIncomeModel.add(AddIncomeModel("วันที่ เวลา",R.drawable.calendar,"22/12/2023,11:17"))
        addIncomeModel.add(AddIncomeModel("ประเภท",R.drawable.type,"เลือก"))
        addIncomeModel.add(AddIncomeModel("หมวดหมู่",R.drawable.category,"เลือก"))
        addIncomeModel.add(AddIncomeModel("โน้ต",R.drawable.note_,"เลือก"))
        addIncomeModel.add(
            AddIncomeModel("บันทึกอัตโนมัติ",R.drawable.autosave,"เลือก" +
                "")
        )

        addIncomeAdapter = AddIncomeAdapter(addIncomeModel) {


        }
        binding.RCVincome.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = addIncomeAdapter
            addIncomeAdapter.notifyDataSetChanged()
        }
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

}