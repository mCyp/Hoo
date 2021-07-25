package com.example.composehoo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.composehoo.db.dao.FavouriteShoeDao
import com.example.composehoo.db.dao.ShoeDao
import com.example.composehoo.db.dao.UserDao
import com.example.composehoo.db.data.FavouriteShoe
import com.example.composehoo.db.data.Shoe
import com.example.composehoo.db.data.User

/**
 * 数据库文件
 */
@Database(entities = [User::class, Shoe::class, FavouriteShoe::class],version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDataBase: RoomDatabase() {
    // 得到UserDao
    abstract fun userDao(): UserDao
    // 得到ShoeDao
    abstract fun shoeDao(): ShoeDao
    // 得到FavouriteShoeDao
    abstract fun favouriteShoeDao(): FavouriteShoeDao

    companion object{
        @Volatile
        private var instance:AppDataBase? = null

        fun getInstance(context: Context):AppDataBase{
            return instance?: synchronized(this){
                instance?:buildDataBase(context)
                    .also {
                        instance = it
                    }
            }
        }

        private fun buildDataBase(context: Context):AppDataBase{
            return Room
                .databaseBuilder(context,AppDataBase::class.java,"hoo-compose-database")
                .addCallback(object :RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        /*val isFirstLaunch = AppPrefsUtils.getBoolean(BaseConstant.IS_FIRST_LAUNCH)
                        if(isFirstLaunch){
                            // 读取鞋的集合
                            val request = OneTimeWorkRequestBuilder<ShoeWorker>().build()
                            WorkManager.getInstance().enqueue(request)
                        }*/

                    }
                })
                .build()
        }
    }
}