package com.joe.jetpackdemo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.common.BaseConstant
import com.joe.jetpackdemo.utils.AppPrefsUtils

class LoginActivity : AppCompatActivity() {

    //lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*val host:NavHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        navController = host.navController*/

        initShoesOnFirstLaunch()

    }

    /**
     * 在程序第一次使用的时候监听A
     */
    private fun initShoesOnFirstLaunch() {
        val isFirst = AppPrefsUtils.getBoolean(BaseConstant.IS_FIRST_LAUNCH)
        if(isFirst){


            // 初始化数据
            AppPrefsUtils.putBoolean(BaseConstant.IS_FIRST_LAUNCH,false)
        }
    }


}
