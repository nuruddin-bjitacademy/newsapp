package com.graphicless.newsapp.ui

import WorkManagerUtils
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.graphicless.newsapp.R
import com.graphicless.newsapp.databinding.ActivityMainBinding
import com.graphicless.newsapp.utils.*
import com.graphicless.newsapp.viewmodel.NewsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Night Mode Start
        val sharedPreference = SharedPreference(this)

        if (sharedPreference.getValueString("theme") != null) {
            val myTheme = sharedPreference.getValueString("theme")
            if (myTheme == "light")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        // Night Mode End
//        insertNews()
        WorkManagerUtils().syncData(applicationContext)



        val snackBar =
            Snackbar.make(binding.root, "No internet connection", Snackbar.LENGTH_INDEFINITE)
        CheckInternet(application).observe(this) {
            if (!it) {
                snackBar.setAction("Dismiss") {

                }
                snackBar.show()
            } else {
                snackBar.dismiss()
            }
        }


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        // Create a new ColorStateList for the selected icon
        val states = arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(-android.R.attr.state_selected)
        )
        val colors = intArrayOf(
            Color.WHITE,  // selected
            Color.DKGRAY
        )
        val colorStateList = ColorStateList(states, colors)
        binding.bottomNavMenu.itemIconTintList = colorStateList

        binding.bottomNavMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favoriteFragment -> {
                    navController.navigate(R.id.favoriteFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.homeFragment -> {
                    navController.navigate(R.id.categoriesFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

    private fun insertNews() {
        if (!CheckNetwork().isConnected) {
            val snackBar =
                Snackbar.make(binding.root, "No internet connection", Snackbar.LENGTH_LONG)
            snackBar.setAction("Retry") {
                if (!CheckNetwork().isConnected) {
                    snackBar.show()
                } else {
                    insertNews()
                }
            }
            snackBar.show()
        } else {
            viewModel.insertTopNews(AppConstants.COUNTRY)
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.insertNews(AppConstants.BUSINESS)
                delay(1000)
                viewModel.insertNews(AppConstants.ENTERTAINMENT)
                delay(1000)
                viewModel.insertNews(AppConstants.GENERAL)
                delay(1000)
                viewModel.insertNews(AppConstants.HEALTH)
                delay(1000)
                viewModel.insertNews(AppConstants.SCIENCE)
                delay(1000)
                viewModel.insertNews(AppConstants.SPORTS)
                delay(1000)
                viewModel.insertNews(AppConstants.TECHNOLOGY)
                delay(1000)
            }

        }

    }
}