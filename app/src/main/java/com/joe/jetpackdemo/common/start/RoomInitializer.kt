package com.joe.jetpackdemo.common.start

import android.content.Context
import androidx.startup.Initializer
import com.joe.jetpackdemo.db.AppDataBase

/**
 * Room的初始化容器
 */
class RoomInitializer : Initializer<AppDataBase> {
    override fun create(context: Context): AppDataBase {
        return AppDataBase.getInstance(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}