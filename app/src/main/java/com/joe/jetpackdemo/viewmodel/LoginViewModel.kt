package com.joe.jetpackdemo.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class LoginViewModel :ViewModel(){

    public val data = MutableLiveData<String>();


    init {
        data.value = "Hello World"
    }


}