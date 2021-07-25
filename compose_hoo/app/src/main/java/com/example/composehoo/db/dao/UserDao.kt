package com.example.composehoo.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.composehoo.db.data.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE user_account = :account AND user_pwd = :pwd")
    suspend fun login(account:String,pwd:String): User?

    @Query("SELECT * FROM user WHERE id=:id")
    fun findUserById(id:Long):LiveData<User>

    @Query("SELECT * FROM user")
    fun getAllUsers():List<User>

    @Insert
    suspend fun insertUser(user:User):Long

    @Delete
    fun deleteUser(user: User)

    @Update
    fun updateUser(user:User)
}