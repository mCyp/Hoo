package com.joe.jetpackdemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.joe.jetpackdemo.db.dao.UserDao
import com.joe.jetpackdemo.db.data.User

/**
 * 数据库文件
 */
@Database(entities = [User::class],version = 1,exportSchema = false)
abstract class AppDataBase:RoomDatabase() {
    abstract fun userDao():UserDao

    companion object{
        @Volatile
        private var instance:AppDataBase? = null

        fun getInstance(context:Context):AppDataBase{
            return instance?: synchronized(this){
                instance?:buildDataBase(context)
                    .also {
                        instance = it
                    }
            }
        }

        private fun buildDataBase(context: Context):AppDataBase{
            // TODO WorkmManger好像需要回调
            return Room.databaseBuilder(context,AppDataBase::class.java,"jetpack_db")
                .build()
        }
    }

}