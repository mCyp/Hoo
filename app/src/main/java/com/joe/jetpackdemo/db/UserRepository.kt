package com.joe.jetpackdemo.db

import com.joe.jetpackdemo.db.dao.UserDao
import com.joe.jetpackdemo.db.data.User

class UserRepository private constructor(private val userDao: UserDao) {

    /**
     * 登录用户
     */
    fun login(account: String, pwd: String) = userDao.login(account, pwd)

    /**
     * 注册一个用户
     */
    fun register(email: String, account: String, pwd: String) = userDao.insertUser(User(account, pwd, email))

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userDao: UserDao): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userDao).also {
                    instance = it
                }
            }

    }
}