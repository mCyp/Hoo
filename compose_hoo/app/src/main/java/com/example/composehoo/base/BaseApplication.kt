package com.example.composehoo.base

import android.app.Application
import android.content.Context
import com.example.composehoo.db.AppDataBase

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        context = this
        AppDataBase.getInstance(this)
    }

    companion object {
        lateinit var context: Context
    }
}