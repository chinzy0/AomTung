package com.money.moneyx.main.homeScreen

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.money.moneyx.R
import com.money.moneyx.data.Preference
import com.money.moneyx.databinding.ActivityHomeBinding
import com.money.moneyx.function.loadingScreen
import com.money.moneyx.main.addListPage.AddListScreenActivity
import com.money.moneyx.main.addListPage.addExpends.GetAllCategoryExpenses
import com.money.moneyx.main.addListPage.addExpends.GetAllTypeExpenses
import com.money.moneyx.main.addListPage.addIncome.GetAllCategoryincome
import com.money.moneyx.main.addListPage.addIncome.GetAllTypeIncome
import com.money.moneyx.main.addListPage.addIncome.ListScheduleAuto
import com.money.moneyx.main.autoSave.AutoSaveFragment
import com.money.moneyx.main.autoSave.GetListAuto
import com.money.moneyx.main.homeScreen.fragments.home.HomeFragment
import com.money.moneyx.main.incomeExpends.IncomeExpendsFragment
import com.money.moneyx.main.profile.ProfileFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var positionClick = ""
    private var idMember = 0


    companion object {
        var getAllTypeIncomeData: GetAllTypeIncome? = null
        var getAllCategoryincome: GetAllCategoryincome? = null
        var getAllTypeExpenses: GetAllTypeExpenses? = null
        var getAllCategoryExpenses: GetAllCategoryExpenses? = null
        var listScheduleAuto: ListScheduleAuto? = null
        var listAutoSave: GetListAuto? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.homeViewModel = viewModel
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        positionClick = intent.getStringExtra("positionClick").toString()
        val preferences = Preference.getInstance(this)
        idMember = preferences.getInt("idmember", 0)
        loadingScreen(this)
        changePage()


        viewModel.getAllCategoryincome { Categoryincome -> getAllCategoryincome = Categoryincome }
        viewModel.getAllTypeIncome { TypeIncome -> getAllTypeIncomeData = TypeIncome }
        viewModel.getAllTypeExpenses { TypeExpenses -> getAllTypeExpenses = TypeExpenses }
        viewModel.getAllCategoryExpenses { CategoryExpenses -> getAllCategoryExpenses = CategoryExpenses }
        viewModel.listScheduleAuto { ScheduleAuto -> listScheduleAuto = ScheduleAuto }
        viewModel.getListAuto(idMember){ AutoSave -> listAutoSave = AutoSave }

    }

    private fun changePage() {
        if (positionClick == "ProfilePage") {
            replaceFragment(ProfileFragment())
            binding.bottomNavigationView.selectedItemId = R.id.icon_profile
        } else {
            replaceFragment(HomeFragment())
        }

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->

            val fragment = when (menuItem.itemId) {
                R.id.icon_home -> HomeFragment()
                R.id.icon_income -> IncomeExpendsFragment()
                R.id.icon_autosave -> AutoSaveFragment()
                R.id.icon_profile -> ProfileFragment()
                else -> null
            }
            if (fragment != null && fragment::class.java != supportFragmentManager.findFragmentById(R.id.frame_layout)?.javaClass) {
                replaceFragment(fragment)
            }
            true
        }
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddListScreenActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }


    }

    private fun replaceFragment(fragment: Fragment) {
        runOnUiThread {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.commit()
        }
    }
}