package com.joe.jetpackdemo.viewmodel

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joe.jetpackdemo.MainActivity
import com.joe.jetpackdemo.common.BaseConstant
import com.joe.jetpackdemo.common.listener.SimpleWatcher
import com.joe.jetpackdemo.db.AppDataBase
import com.joe.jetpackdemo.db.UserRepository
import com.joe.jetpackdemo.db.dao.UserDao_Impl
import com.joe.jetpackdemo.db.data.User

class RegisterModel constructor():ViewModel() {

    val n = MutableLiveData<String>("")
    val p = MutableLiveData<String>("")
    val mail = MutableLiveData<String>("")

    lateinit var context: Context

    /**
     * 用户名改变回调的函数
     */
    fun onNameChanged(s: CharSequence) {
        //n.set(s.toString())
        n.value = s.toString()
    }

    /**
     * 密码改变的回调函数
     */
    fun onPwdChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        //p.set(s.toString())
        p.value = s.toString()
    }

    fun register() {
        UserRepository.getInstance(AppDataBase.getInstance(context).userDao())
            .register(n.value!!,p.value!!, mail.value!!)
        // TODO 跳转到登录界面
    }
}