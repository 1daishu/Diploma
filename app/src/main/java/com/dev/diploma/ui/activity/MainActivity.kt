package com.dev.diploma.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.dev.diploma.R
import com.dev.diploma.data.network.dateFromJson.DateFromJson
import com.dev.diploma.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
        val navController = Navigation.findNavController(this, R.id.fragment_container_view)
        NavigationUI.setupWithNavController(bottomNavigation, navController)

    }

    fun showButtonLoginToHome() {
        findViewById<BottomNavigationView>(R.id.btm_nav).visibility = View.VISIBLE
    }

    fun noShowButtonProfileToLogin() {
        findViewById<BottomNavigationView>(R.id.btm_nav).visibility = View.INVISIBLE
    }


}