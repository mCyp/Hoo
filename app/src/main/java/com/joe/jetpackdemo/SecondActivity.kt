package com.joe.jetpackdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val host:NavHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = host.navController

        setupBottomNavMenu(navController)

    }

    private fun setupBottomNavMenu(navController: NavController){
        val bottomNavigate:BottomNavigationView = findViewById(R.id.navigation_view)
        //bottomNavigate.setupWithNavController(navController)
        NavigationUI.setupWithNavController(bottomNavigate,navController)
        /*bottomNavigate.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> Navigation.findNavController(bottomNavigate).navigate(R.id.navHostFragment)
                R.id.nav_trans -> Navigation.findNavController(bottomNavigate).navigate(R.id.setttingFragment)
            }
            true
        }*/
    }

    override fun onSupportNavigateUp() = findNavController(this, R.id.my_nav_host_fragment).navigateUp()


}
