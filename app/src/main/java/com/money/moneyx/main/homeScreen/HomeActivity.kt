package com.money.moneyx.main.homeScreen

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.ContactsContract.Profile
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.money.moneyx.R
import com.money.moneyx.databinding.ActivityHomeBinding
import com.money.moneyx.main.addListPage.AddListScreenActivity
import com.money.moneyx.main.homeScreen.fragments.home.HomeFragment
import com.money.moneyx.main.incomeExpends.IncomeExpendsFragment
import com.money.moneyx.main.profile.ProfileFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.icon_home -> HomeFragment()
                R.id.icon_income -> IncomeExpendsFragment()
                R.id.icon_autosave -> HomeFragment()
                R.id.icon_profile -> ProfileFragment()
                else -> null
            }

            fragment?.let { replaceFragment(it) }
            true
        }
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddListScreenActivity::class.java)
            startActivity(intent)
        }


    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }


}