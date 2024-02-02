package com.money.moneyx.main.addListPage.calculator

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.money.moneyx.databinding.ActivityCalculatorBinding
import com.money.moneyx.main.addListPage.ReportViewModel
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.math.BigDecimal
import java.math.RoundingMode


class CalculatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculatorBinding
    private lateinit var viewModel: ReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ReportViewModel::class.java]
        binding.reportViewModel = viewModel
        setEventClick()
        manageData()

        binding.buttonClear.setOnClickListener { onClearButtonClick() }
        binding.buttonDelete.setOnClickListener { onDeleteButtonClick() }
        binding.buttonDot.setOnClickListener { onDotButtonClick() }
        binding.buttonEqual.setOnClickListener { onEqualButtonClick() }
        binding.button1.setOnClickListener { onNumberButtonClick("1") }
        binding.button2.setOnClickListener { onNumberButtonClick("2") }
        binding.button3.setOnClickListener { onNumberButtonClick("3") }
        binding.button4.setOnClickListener { onNumberButtonClick("4") }
        binding.button5.setOnClickListener { onNumberButtonClick("5") }
        binding.button6.setOnClickListener { onNumberButtonClick("6") }
        binding.button7.setOnClickListener { onNumberButtonClick("7") }
        binding.button8.setOnClickListener { onNumberButtonClick("8") }
        binding.button9.setOnClickListener { onNumberButtonClick("9") }
        binding.buttonPlus.setOnClickListener { onOperationButtonClick("+") }
        binding.buttonNegative.setOnClickListener { onOperationButtonClick("-") }
        binding.buttonDivide.setOnClickListener { onOperationButtonClick("/") }
        binding.buttonX.setOnClickListener { onOperationButtonClick("*") }
        binding.button00.setOnClickListener { onNumberButtonClick("00") }
        binding.button0.setOnClickListener { onNumberButtonClick("0") }



    }

    private fun manageData(){
        val status =  intent.getStringExtra("status")
        if (status != "true"){
            val resultExpends = intent.getStringExtra("resultExpends")
            binding.textTv.setText(resultExpends)
        }else{
            val resultIncome = intent.getStringExtra("resultIncome")
            binding.textTv.setText(resultIncome)
        }
    }

    private fun setEventClick() {
        viewModel.onClick.observe(this, Observer {
            when (it) {
                "onBack" -> {
                    onBackPressed()
                }
                "btn" -> {
                    val status = intent.getStringExtra("status")
                    onEqualButtonClick()
                    val finalResult = evaluateExpression(binding.textTv.text.toString())
                    val resultToSend = finalResult.toDouble().coerceAtLeast(0.0).coerceAtMost(9999999999.99)

                    val formattedResult = String.format("%.2f", resultToSend)

                    intent.putExtra("number", formattedResult)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }


            }
        })
    }



    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
    private fun onClearButtonClick() {
        binding.textTv.text.clear()
    }

    private fun onDeleteButtonClick() {
        val length = binding.textTv.length()
        val currentText = binding.textTv.text
        if (length > 0) {
            val newText = currentText.substring(0, currentText.length - 1)
            binding.textTv.setText(newText)
        }
    }

    private fun onDotButtonClick() {
        val currentText = binding.textTv.text.toString()
        if (!currentText.contains(".")) {
            if (currentText.isEmpty() || currentText.endsWith("+") || currentText.endsWith("-") || currentText.endsWith("*") || currentText.endsWith("/")) {
                binding.textTv.append("0.")
            } else {
                binding.textTv.append(".")
            }
        }
    }


    private fun onEqualButtonClick() {
        try {
            val expression = binding.textTv.text.toString()

            if (expression.isNotEmpty()) {
                val result = evaluateExpression(expression, decimalPlaces = 2)
                binding.textTv.text.clear()
                binding.textTv.append(result)
            }
        } catch (e: Exception) {
            Log.e("CalculatorActivity", "Error in calculation: ${e.message}")
        }
    }


    private fun evaluateExpression(expression: String, decimalPlaces: Int = 2): String {
        try {
            val result = ExpressionBuilder(expression).build().evaluate()
            val roundedResult = BigDecimal(result).setScale(decimalPlaces, RoundingMode.HALF_UP).toDouble()
            val finalResult = if (roundedResult < 0) 0.0 else roundedResult

            // ใช้ String.format เพื่อรับจำนวนทศนิยมที่ต้องการแสดงผล
            return String.format("%.${decimalPlaces}f", finalResult)
        } catch (e: Exception) {
            Log.e("CalculatorActivity", "Error in calculation: ${e.message}")
            return "0"
        }
    }







    private fun onNumberButtonClick(number: String) {
        binding.textTv.append(number)
    }

    private fun onOperationButtonClick(operation: String) {
        val currentText = binding.textTv.text.toString()
        val lastCharIsOperator = currentText.isNotEmpty() && "+-*/".contains(currentText.last())
        if (lastCharIsOperator && !operation.isDigitsOnly()) {
            return
        }

        binding.textTv.append(operation)
    }

}



