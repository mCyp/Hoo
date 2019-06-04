package com.joe.jetpackdemo.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.joe.jetpackdemo.db.data.User

/**
 * 用户的方法
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE user_account = :account AND user_pwd = :pwd")
    fun login(account:String,pwd:String):List<User>

    @Insert
    fun insertUser(user:User)

    @Delete
    fun deleteUser(user: User)
}