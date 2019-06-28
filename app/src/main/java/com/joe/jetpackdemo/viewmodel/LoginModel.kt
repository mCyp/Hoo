package com.joe.jetpackdemo.viewmodel

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.joe.jetpackdemo.MainActivity
import com.joe.jetpackdemo.common.BaseConstant
import com.joe.jetpackdemo.common.listener.SimpleWatcher
import com.joe.jetpackdemo.db.repository.UserRepository
import com.joe.jetpackdemo.utils.AppPrefsUtils

class LoginModel constructor(
    private val repository: UserRepository
    , private val context: Context
) : ViewModel() {

    companion object {
        @JvmStatic
        @BindingAdapter("addTextChangedListener")
        fun addTextChangedListener(editText: EditText, simpleWatcher: SimpleWatcher) {
            editText.addTextChangedListener(simpleWatcher)
        }
    }

    val n = MutableLiveData<String>("")
    val p = MutableLiveData<String>("")
    lateinit var lifecycleOwner: LifecycleOwner

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
        if (!TextUtils.isEmpty(n.value)
            && !TextUtils.isEmpty(p.value)
        ) {
            val pwd = p.value!!
            val account = n.value!!
            repository.login(account, pwd).observe(lifecycleOwner, Observer {
                if (it != null) {
                    AppPrefsUtils.putLong(BaseConstant.SP_USER_ID, it.id)
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    Toast.makeText(context, "登录成功！", Toast.LENGTH_SHORT).show()
                }
            })
            /*viewModelScope.launch {
                val u =  repository.login(account,pwd)
                if(u != null){
                    Toast.makeText(context,"登录成功！",Toast.LENGTH_SHORT).show()
                }
            }*/
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