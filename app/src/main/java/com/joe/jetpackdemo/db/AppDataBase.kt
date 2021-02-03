package com.joe.jetpackdemo.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.joe.jetpackdemo.common.BaseConstant
import com.joe.jetpackdemo.db.dao.FavouriteShoeDao
import com.joe.jetpackdemo.db.dao.ShoeDao
import com.joe.jetpackdemo.db.dao.StorageDataDao
import com.joe.jetpackdemo.db.dao.UserDao
import com.joe.jetpackdemo.db.data.FavouriteShoe
import com.joe.jetpackdemo.db.data.Shoe
import com.joe.jetpackdemo.db.data.StorageData
import com.joe.jetpackdemo.db.data.User
import com.joe.jetpackdemo.utils.AppPrefsUtils
import com.joe.jetpackdemo.worker.ShoeWorker

/**
 * 数据库文件
 */
@Database(entities = [User::class,Shoe::class,FavouriteShoe::class,StorageData::class],version = 2,exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDataBase:RoomDatabase() {
    // 得到UserDao
    abstract fun userDao():UserDao
    // 得到ShoeDao
    abstract fun shoeDao():ShoeDao
    // 得到FavouriteShoeDao
    abstract fun favouriteShoeDao():FavouriteShoeDao
    // 得到StorageDataDao
    abstract fun storageDataDao(): StorageDataDao

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
            return Room
                .databaseBuilder(context,AppDataBase::class.java,"jetPackDemo-database")
                .addCallback(object :RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        val isFirstLaunch = AppPrefsUtils.getBoolean(BaseConstant.IS_FIRST_LAUNCH)
                        if(isFirstLaunch){
                            // 读取鞋的集合
                            val request = OneTimeWorkRequestBuilder<ShoeWorker>().build()
                            WorkManager.getInstance().enqueue(request)
                        }

                    }
                })
                .build()
        }
    }
}