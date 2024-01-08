package com.money.moneyx.login.forgotPassword

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivityConfirmForgotPasswordBinding
import com.money.moneyx.login.createPincode.CustomKeyboardAdapter
import com.money.moneyx.login.createPincode.CustomKeyboardModel
import com.money.moneyx.login.loginScreen.LoginViewModel
import com.money.moneyx.main.homeScreen.HomeActivity

class ConfirmResetPincodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmForgotPasswordBinding
    private lateinit var viewModel : LoginViewModel
    private lateinit var keyboardAdapter: CustomKeyboardAdapter
    private var listKeyboard = ArrayList<CustomKeyboardModel>()
    private lateinit var sharePreferences: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharePreferences = Preference.getInstance(applicationContext)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val savedPin = intent.getStringExtra("savedPin")

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
        listKeyboard.add(CustomKeyboardModel("delete",R.drawable.delete))


        keyboardAdapter = CustomKeyboardAdapter(listKeyboard){number ->
            if (number == "delete") {
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

        binding.PinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val enteredText = s.toString()
                if (enteredText.length == 6 ) {
                    if (enteredText == savedPin){
                        binding.textAlert.visibility = View.GONE
                        binding.textView.text = "ยืนยัน PINCODE"
                        binding.textView.setTextColor(ContextCompat.getColor(this@ConfirmResetPincodeActivity, R.color.sure_pinCode))
                        savePin(enteredText)
                        pinConfirmationSuccess()
                    }else{
                        binding.PinView.text?.clear()
                        binding.textView.text = "ลองใหม่อีกครั้ง"
                        binding.textAlert.visibility = View.VISIBLE
                        binding.textView.setTextColor(ContextCompat.getColor(this@ConfirmResetPincodeActivity, R.color.red))
                    }

                }





            }

        })


    }
    private fun savePin(pin: String) {
        //เก็บ pin code ลง preference
        val preferences = Preference.getInstance(this)
        sharePreferences.saveString("PINCODE",pin)
        Log.i("asdujhbsj",pin)
    }

    private fun pinConfirmationSuccess() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        binding.PinView.text?.clear()
        listKeyboard.clear()
        keyboardAdapter.notifyDataSetChanged()
        finish()
        startActivity(intent)
    }
}