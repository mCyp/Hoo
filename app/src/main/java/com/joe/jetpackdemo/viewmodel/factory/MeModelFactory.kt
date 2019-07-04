package com.joe.jetpackdemo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.jetpackdemo.db.repository.ShoeRepository
import com.joe.jetpackdemo.db.repository.UserRepository
import com.joe.jetpackdemo.viewmodel.MeModel
import com.joe.jetpackdemo.viewmodel.ShoeModel

class MeModelFactory(
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MeModel(repository) as T
    }
}