package com.joe.jetpackdemo

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var mToolbar:Toolbar
    lateinit var mCamera:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = host.navController


        initWidget()

        initBottomNavigationView(bottomNavigationView,navController)
    }

    private fun initBottomNavigationView(bottomNavigationView: BottomNavigationView, navController: NavController) {
        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.meFragment -> mCamera.visibility = View.VISIBLE
                else -> mCamera.visibility = View.GONE
            }
        }
    }

    private fun initWidget() {
        bottomNavigationView = findViewById(R.id.navigation_view)
        mToolbar = findViewById(R.id.toolbar)
        mCamera = findViewById(R.id.iv_camera)
    }


}
