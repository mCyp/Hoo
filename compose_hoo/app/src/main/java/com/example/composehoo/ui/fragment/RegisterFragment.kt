package com.example.composehoo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.composehoo.R
import com.example.composehoo.ui.page.login.RegisterPage
import com.example.composehoo.ui.theme.ComposeHooTheme

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                ComposeHooTheme {
                    RegisterPage(
                        onClickBack = {
                            findNavController().popBackStack()
                        },
                        onRegisterSuccess = {
                            val action = RegisterFragmentDirections.actionRegisterToLogin()
                            findNavController().navigate(action)
                        })
                }
            }
        }
    }
}