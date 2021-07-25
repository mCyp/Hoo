package com.example.composehoo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.composehoo.ui.activity.MainActivity
import com.example.composehoo.ui.page.login.LoginPage
import com.example.composehoo.ui.theme.ComposeHooTheme

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                ComposeHooTheme {
                    LoginPage(
                        onClickBack = {
                            findNavController().popBackStack()
                        },
                        onLoginSuccess = {
                            MainActivity.startActivity(requireContext())
                        }
                    )
                }
            }
        }
    }
}