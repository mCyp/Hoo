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
import com.joe.jetpackdemo.db.data.User

class LoginModel constructor():ViewModel() {
    // TODO Data Binding 中的例子
    //val n = ObservableField<String>(name)
    //val p = ObservableField<String>(pwd)

    companion object{
        @JvmStatic
        @BindingAdapter("addTextChangedListener")
        fun addTextChangedListener(editText: EditText, simpleWatcher: SimpleWatcher) {
            editText.addTextChangedListener(simpleWatcher)
        }
    }

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

    fun login() {
        if (n.value.equals(BaseConstant.USER_NAME)
            && p.value.equals(BaseConstant.USER_PWD)
        ) {
            Toast.makeText(context, "账号密码正确", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    // SimpleWatcher 是简化了的TextWatcher
    val nameWatcher = object : SimpleWatcher() {
        override fun afterTextChanged(s: Editable) {
            super.afterTextChanged(s)

            n.value = s.toString()
        }
    }

    val pwdWatcher = object : SimpleWatcher() {
        override fun afterTextChanged(s: Editable) {
            super.afterTextChanged(s)

            //p.set(s.toString())
            p.value = s.toString()
        }
    }


}