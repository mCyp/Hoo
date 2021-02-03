package com.joe.jetpackdemo.common

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel

open class BaseApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        context = this

        // MMKV 初始化
        val root = context.filesDir.absolutePath + "/mmkv"
        MMKV.initialize(root, MMKVLogLevel.LevelInfo)
    }

    companion object {
        lateinit var context: Context
    }
}