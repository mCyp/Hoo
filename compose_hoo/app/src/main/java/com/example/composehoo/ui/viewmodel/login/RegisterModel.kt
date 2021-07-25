package com.example.composehoo.ui.viewmodel.login

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.composehoo.db.data.User
import com.example.composehoo.db.repository.UserRepository


class RegisterModel (val repository: UserRepository): ViewModel() {

    suspend fun register(email: String, name: String, pwd: String): Long = repository.register(email, name, pwd)

}