package com.joe.jetpackdemo.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.joe.jetpackdemo.db.repository.ShoeRepository
import com.joe.jetpackdemo.db.repository.UserRepository
import com.joe.jetpackdemo.viewmodel.FavouriteModel
import com.joe.jetpackdemo.viewmodel.RegisterModel
import com.joe.jetpackdemo.viewmodel.ShoeModel

class FavouriteModelFactory(
    private val repository: ShoeRepository
,private val userId:Long
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavouriteModel(repository,userId) as T
    }
}