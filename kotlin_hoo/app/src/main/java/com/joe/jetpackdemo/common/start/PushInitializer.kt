package com.joe.jetpackdemo.common.start

import android.content.Context
import androidx.startup.Initializer

class PushInitializer  :Initializer<PushSdk>{
    override fun create(context: Context): PushSdk {
        val push = PushSdk("MPush")
        push.registerPush()
        return push
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(RoomInitializer::class.java)
    }
}

/**
 * 这是一个伪造的推送类 PushSdk
 */
data class PushSdk(val name:String){
    fun registerPush(){
        print("Hello,i'm registering push")
    }
}