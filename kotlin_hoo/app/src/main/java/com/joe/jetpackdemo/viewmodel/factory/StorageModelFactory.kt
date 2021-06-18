package com.joe.jetpackdemo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.jetpackdemo.db.repository.ShoeRepository
import com.joe.jetpackdemo.db.repository.StorageDataRepository
import com.joe.jetpackdemo.viewmodel.ShoeModel
import com.joe.jetpackdemo.viewmodel.StorageModel

class StorageModelFactory(
    private val repository: StorageDataRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StorageModel(repository) as T
    }
}