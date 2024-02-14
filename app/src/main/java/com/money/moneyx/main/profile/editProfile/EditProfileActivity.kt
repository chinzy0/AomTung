package com.money.moneyx.main.profile.editProfile

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.iamauttamai.avloading.ui.AVLoading
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivityEditProfileBinding
import com.money.moneyx.databinding.ActivitySubmitPinBinding
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.function.showExitDialog
import com.money.moneyx.login.createPincode.ConfirmPincodeActivity
import com.money.moneyx.login.loginScreen.LoginActivity
import com.money.moneyx.main.homeScreen.HomeActivity
import com.money.moneyx.main.profile.ProfileViewModel

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    private val preferences = Preference.getInstance(this)
    private var newName = ""
    private var name = ""
    private var idMember = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val preferences = Preference.getInstance(this)
        viewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]
        binding.editProfileViewModel = viewModel
        idMember = preferences.getInt("idmember", 0)
        name =  preferences.getString("username","")
        binding.textName.setText(name)
        setEventClick()
        validateButtonName()
        loadingScreen(this)
        setContentView(binding.root)
    }

    private fun validateButtonName() {
        binding.textName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val enteredText = s.toString()
                newName = enteredText
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val enteredText = s.toString()
                if (enteredText != name) {
                    binding.buttonSaveName.isEnabled = true
                    binding.buttonSaveName.backgroundTintList = ColorStateList.valueOf(getColor(R.color.income))
                }else{
                    binding.buttonSaveName.isEnabled = false
                    binding.buttonSaveName.backgroundTintList = ColorStateList.valueOf(getColor(R.color.button_disable))
                }
            }
        })
    }

    private fun setEventClick() {
        viewModel.onClick.observe(this, Observer {
            when (it) {
                "onBack" -> {
                    onBackPressed()
                }
                "saveNameChange" -> {
                    AVLoading.startAnimLoading()
                    viewModel.resetUsernameAccount(idmember = idMember, name = name){ model ->
                        AVLoading.stopAnimLoading()
                        if (model.data.is_Reseted) {
                            preferences.saveString("username", newName)
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        } else { }
                    }
                }
            }
        })
    }
}