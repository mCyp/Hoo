package com.example.composehoo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composehoo.ui.common.view.createEnterAnimation
import com.example.composehoo.ui.page.login.LoginPage
import com.example.composehoo.ui.page.login.LoginType
import com.example.composehoo.ui.page.login.RegisterPage
import com.example.composehoo.ui.page.login.WelcomePage
import com.example.composehoo.ui.theme.ComposeHooTheme


/**
 * AAC 架构
 * ConstraintLayout 必备
 * Room
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
    }

}


