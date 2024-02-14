package com.money.moneyx.main.profile.security

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivitySubmitPinBinding
import com.money.moneyx.login.createPincode.CustomKeyboardAdapter
import com.money.moneyx.login.createPincode.CustomKeyboardModel
import com.money.moneyx.login.forgotPassword.ForgotPasswordActivity
import com.money.moneyx.login.loginScreen.LoginViewModel
import com.money.moneyx.main.homeScreen.HomeActivity
import com.money.moneyx.main.profile.editProfile.DeleteAccountActivity

class SubmitPinActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySubmitPinBinding
    private lateinit var keyboardAdapter: CustomKeyboardAdapter
    private var listKeyboard = ArrayList<CustomKeyboardModel>()
    private lateinit var viewModel : LoginViewModel
    private var savedPin = ""
    private var postition = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmitPinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        val preferences = Preference.getInstance(this)
        savedPin = preferences.getString("pincode","")
        postition = intent.getStringExtra("EditProfile").toString()
        Log.i("sakldhjasnlkdsad",postition)


        makeKeyBoard()
        pinView()
        onBack()
        binding.textView2.paintFlags =  Paint.UNDERLINE_TEXT_FLAG

    }

    private fun makeKeyBoard() {
        listKeyboard.add(CustomKeyboardModel("1", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("2", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("3", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("4", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("5", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("6", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("7", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("8", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("9", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("-", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("0", R.drawable.delete))
        listKeyboard.add(CustomKeyboardModel("delete", R.drawable.delete))


        keyboardAdapter = CustomKeyboardAdapter(listKeyboard) { number ->
            if (number == "delete") {
                val currentText = binding.PinView.text.toString()
                if (currentText.isNotEmpty()) {
                    val newText = currentText.substring(0, currentText.length - 1)
                    binding.PinView.setText(newText)
                }
            } else {
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
    }


    private fun pinView(){
        binding.PinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val enteredText = s.toString()
                if (enteredText.length == 6) {
                    if (enteredText == savedPin){
                        if (postition == "EditProfilePage"){
                            pinConfirmationSuccessEditProfile()
                        }else{
                            pinConfirmationSuccess()
                        }
                    }else{
                        binding.PinView.text?.clear()
                        binding.textView.text = "ลองใหม่อีกครั้ง"
                        binding.textView.setTextColor(ContextCompat.getColor(this@SubmitPinActivity, R.color.red))
                        showCustomDialog()
                    }

                }

            }

        })
    }
    private fun pinConfirmationSuccess() {
        val intent = Intent(this, PasswordAndSecurityActivity::class.java)
        val positionClick = intent.getStringExtra("positionClick")
        intent.putExtra("positionClick",positionClick)
        binding.PinView.text?.clear()
        listKeyboard.clear()
        keyboardAdapter.notifyDataSetChanged()
        finish()
        startActivity(intent)
    }
    private fun pinConfirmationSuccessEditProfile() {
        val intent = Intent(this, DeleteAccountActivity::class.java)
        binding.PinView.text?.clear()
        listKeyboard.clear()
        keyboardAdapter.notifyDataSetChanged()
        finish()
        startActivity(intent)
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dailog_wrong_pincode)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
        var ok = dialog.findViewById<TextView>(R.id.okDialog)
        ok.setOnClickListener {
            dialog.dismiss()
        }

    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
    private fun onBack(){
        binding.imageView14.setOnClickListener{
            onBackPressed()
        }
    }
}