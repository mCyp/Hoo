package com.joe.jetpackdemo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class SecondActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration:AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host:NavHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        setActionBar(navController,appBarConfiguration)

        setupBottomNavMenu(navController)
    }

    /**
     * 设置ActionBar
     */
    private fun setActionBar(navController: NavController,appBarConfig: AppBarConfiguration){
        setupActionBarWithNavController(navController,appBarConfig)
    }

    private fun setupBottomNavMenu(navController: NavController){
        val bottomNavigate:BottomNavigationView = findViewById(R.id.navigation_view)
        bottomNavigate.setupWithNavController(navController)
        //NavigationUI.setupWithNavController(bottomNavigate,navController)
       /* bottomNavigate.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> navController.navigate(R.id.navHostFragment)
                R.id.nav_trans -> navController.navigate(R.id.setttingFragment)
            }
            true
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp() = findNavController(this, R.id.my_nav_host_fragment).navigateUp()


}
