package com.joe.jetpackdemo.viewmodel

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.db.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterModel constructor(
    private val repository: UserRepository
) : ViewModel() {

    val n = MutableLiveData<String>("")
    val p = MutableLiveData<String>("")
    val mail = MutableLiveData<String>("")

    /**
     * 用户名改变回调的函数
     */
    fun onNameChanged(s: CharSequence) {
        //n.set(s.toString())
        n.value = s.toString()
    }

    /**
     * 邮箱改变的时候
     */
    fun onEmailChanged(s: CharSequence) {
        //n.set(s.toString())
        mail.value = s.toString()
    }

    /**
     * 密码改变的回调函数
     */
    fun onPwdChanged(s: CharSequence) {
        //p.set(s.toString())
        p.value = s.toString()
    }

    fun register() {
        viewModelScope.launch {
            repository.register(mail.value!!, n.value!!, p.value!!)
        }
    }
}