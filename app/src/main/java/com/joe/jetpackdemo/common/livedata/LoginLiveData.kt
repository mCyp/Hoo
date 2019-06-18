package com.joe.jetpackdemo.common.livedata

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData

/**
 * 自定义单例LiveData
 */
class LoginLiveData:LiveData<LoginInfo>() {

    companion object {
        private lateinit var sInstance: LoginLiveData

        @MainThread
        fun get(): LoginLiveData {
            sInstance = if (::sInstance.isInitialized) sInstance else LoginLiveData()
            return sInstance
        }
    }
}