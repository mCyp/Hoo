package com.joe.jetpackdemo.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.joe.jetpackdemo.db.repository.ShoeRepository
import com.joe.jetpackdemo.db.repository.UserRepository
import com.joe.jetpackdemo.viewmodel.RegisterModel
import com.joe.jetpackdemo.viewmodel.ShoeModel

class ShoeModelFactory(
    private val repository: ShoeRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShoeModel(repository) as T
    }
}