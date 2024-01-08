package com.money.moneyx.login.forgotPassword

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivityResetPinCodeBinding
import com.money.moneyx.login.createPincode.CustomKeyboardAdapter
import com.money.moneyx.login.createPincode.CustomKeyboardModel
import com.money.moneyx.login.loginScreen.LoginViewModel

class ResetPinCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPinCodeBinding
    private lateinit var viewModel : LoginViewModel
    private lateinit var keyboardAdapter: CustomKeyboardAdapter
    private var listKeyboard = ArrayList<CustomKeyboardModel>()
    private lateinit var sharePreferences: Preference
    private var firstEnteredPin: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPinCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharePreferences = Preference.getInstance(applicationContext)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val enteredPin = intent.getStringExtra("savedPin")


        listKeyboard.add(CustomKeyboardModel("1",R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("2",R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("3",R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("4",R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("5",R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("6",R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("7",R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("8",R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("9",R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("-",R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("0",R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("11",R.drawable.delete))

        keyboardAdapter = CustomKeyboardAdapter(listKeyboard){number ->
            if (number == "11") {
                val currentText = binding.PinView.text.toString()
                if (currentText.isNotEmpty()) {
                    val newText = currentText.substring(0, currentText.length - 1)
                    binding.PinView.setText(newText)
                }
            }else {
                val currentText = binding.PinView.text.toString()
                val newText = currentText + number
                binding.PinView.setText(newText)
            }

        }


        binding.keyboard.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = keyboardAdapter
            keyboardAdapter.notifyDataSetChanged()
        }



        binding.appbarCreatePin.BackPage.setOnClickListener{
            this.onBackPressed()
        }

        binding.PinView.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val enteredText = s.toString()
                if (enteredText.length == 6) {
                    firstEnteredPin = enteredText
                    askForPinAgain()
                }

            }

        })



    }
    private fun askForPinAgain() {
        val intent = Intent(this, ConfirmResetPincodeActivity::class.java)
        intent.putExtra("savedPin", firstEnteredPin)
        startActivity(intent)
        binding.PinView.setText("")


    }
}