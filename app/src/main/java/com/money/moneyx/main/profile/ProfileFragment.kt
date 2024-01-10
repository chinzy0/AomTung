package com.money.moneyx.main.profile

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.FragmentProfileBinding
import com.money.moneyx.login.forgotPassword.ForgotPasswordActivity
import com.money.moneyx.login.loginScreen.LoginActivity
import com.money.moneyx.login.loginScreen.LoginViewModel
import com.money.moneyx.login.otpScreen.OtpScreenActivity
import com.money.moneyx.main.addListPage.ReportViewModel
import com.money.moneyx.main.profile.security.SubmitPinActivity


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileAdapter: ProfileAdapter
    private var listMenu = ArrayList<ProfileModel>()
    private lateinit var viewModel: ProfileViewModel
    private lateinit var sharePreferences: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )
        sharePreferences = Preference.getInstance(requireActivity())
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        binding.profileViewModel = viewModel

        addMenu()
        setEventClick()

        return binding.root
    }

    private fun addMenu(){
        listMenu.add(ProfileModel(R.drawable.profile_menu,"ตั้งค่าบัญชี"))
        listMenu.add(ProfileModel(R.drawable.tel_profile,"หมายเลขโทรศัพท์"))
        listMenu.add(ProfileModel(R.drawable.security_profile,"รหัสผ่านและความปลอดภัย"))

        profileAdapter = ProfileAdapter(listMenu){
            Log.i("asdsadas",it)
            when(it) {
                "ตั้งค่าบัญชี" -> {
                }
                "หมายเลขโทรศัพท์" -> {
                }
                "รหัสผ่านและความปลอดภัย" -> {
                    val intent = Intent(requireActivity(), SubmitPinActivity::class.java)
                    startActivity(intent)
                }
            }
        }


        binding.RcvMenuProfile.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = profileAdapter
            profileAdapter.notifyDataSetChanged()
        }
    }

    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.exit_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
        var cancel = dialog.findViewById<ConstraintLayout>(R.id.cancle_exit_button)
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        var exit = dialog.findViewById<ConstraintLayout>(R.id.confirm_exit_button)
        exit.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            sharePreferences.clear()
        }

    }

    private fun setEventClick() {
        viewModel.onClick.observe(requireActivity(), Observer {
            when (it) {
                "exit" -> showCustomDialog()
            }
        })
    }



}