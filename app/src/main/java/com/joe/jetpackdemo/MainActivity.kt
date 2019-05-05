package com.joe.jetpackdemo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.joe.jetpackdemo.viewmodel.LoginViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var model:LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWidget()
    }

    private fun initWidget() {
        model = LoginViewModel()

        val btn = findViewById<Button>(R.id.btn_random)
        val text = findViewById<TextView>(R.id.tv_content)

        btn.setOnClickListener {
            model.data.value = UUID.randomUUID().toString()
        }

        model.data.observe(this,object :Observer<String>{
            override fun onChanged(t: String?) {
                t?.let {
                   text.text = it
                }
            }

        })



    }


}
