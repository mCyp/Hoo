package com.joe.jetpackdemo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.joe.jetpackdemo.db.repository.UserRepository
import com.joe.jetpackdemo.viewmodel.RegisterModel

class RegisterModelFactory(
    private val repository: UserRepository
    , private val navController: NavController
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegisterModel(navController, repository) as T
    }
}