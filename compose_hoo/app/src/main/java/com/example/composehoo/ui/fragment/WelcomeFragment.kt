package com.example.composehoo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.composehoo.R
import com.example.composehoo.ui.page.login.WelcomePage
import com.example.composehoo.ui.theme.ComposeHooTheme

class WelcomeFragment: Fragment() {

    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = activity?.window
        return ComposeView(requireContext()).apply {
            setContent {
                ComposeHooTheme {
                    window?.statusBarColor = MaterialTheme.colors.onBackground.toArgb()

                    WelcomePage(
                        onClickToLogin = {
                            val action = WelcomeFragmentDirections
                                .actionWelcomeToLogin()
                            val navOption = navOptions {
                                anim {
                                    enter = R.anim.common_slide_in_right
                                    popExit = R.anim.common_slide_out_right
                                }
                            }
                            findNavController().navigate(action, navOption)
                        },
                        onClickToRegister = {
                            val action = WelcomeFragmentDirections
                                .actionWelcomeToRegister()
                                .setEMAIL("JiuXinDev@qq.com")
                            findNavController().navigate(action)
                        }
                    )
                }
            }
        }
    }



    /*@ExperimentalAnimationApi
    @Composable
    fun WelComeContent() {
        ComposeHooTheme {
            val navController = rememberNavController()
            LoginNavHost(controller = navController, modifier = Modifier.fillMaxSize())
        }
    }

    @ExperimentalAnimationApi
    @Composable
    fun LoginNavHost(controller: NavHostController, startDestination: String = "", modifier: Modifier) {
        var pos by remember { mutableStateOf(0) }
        NavHost(
            navController = controller,
            startDestination = LoginType.WelCome.name,
            modifier = modifier
        ) {
            composable(LoginType.WelCome.name) {
                pos = 0
                createEnterAnimation(pos == 0) {
                    WelcomePage(
                        onClickToLogin = {
                            controller.navigate(LoginType.Login.name)
                        },
                        onClickToRegister = {
                            controller.navigate(LoginType.WelCome.name)
                        },
                    )
                }

            }
            composable(LoginType.Login.name) {
                pos = 1
                createEnterAnimation(pos == 1) {
                    LoginPage{ controller.popBackStack()}
                }
            }
            composable(LoginType.Register.name) {
                pos = 2
                createEnterAnimation(pos == 2) {
                    RegisterPage()
                }
            }
        }
    }*/
}