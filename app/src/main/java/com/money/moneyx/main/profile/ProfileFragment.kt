package com.money.moneyx.main.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.FragmentProfileBinding
import com.money.moneyx.function.showExitDialog
import com.money.moneyx.login.loginScreen.LoginActivity
import com.money.moneyx.main.profile.security.SubmitPinActivity


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileAdapter: ProfileAdapter
    private var listMenu = ArrayList<ProfileModel>()
    private lateinit var viewModel: ProfileViewModel
    private lateinit var sharePreferences: Preference
    private val onClickDialog = MutableLiveData<String>()

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

    private fun addMenu() {
        listMenu.add(ProfileModel(R.drawable.profile_menu, "ตั้งค่าบัญชี"))
        listMenu.add(ProfileModel(R.drawable.tel_profile, "หมายเลขโทรศัพท์"))
        listMenu.add(ProfileModel(R.drawable.security_profile, "รหัสผ่านและความปลอดภัย"))

        profileAdapter = ProfileAdapter(listMenu) {
            Log.i("asdsadas", it)
            when (it) {
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


    private fun setEventClick() {
        onClickDialog.observe(requireActivity(), Observer {
            when (it) {
                "confirm" -> {
                    sharePreferences.clear()
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        })


        viewModel.onClick.observe(requireActivity(), Observer {
            when (it) {
                "exit" -> showExitDialog(requireActivity(), onClickDialog)
            }
        })
    }


}