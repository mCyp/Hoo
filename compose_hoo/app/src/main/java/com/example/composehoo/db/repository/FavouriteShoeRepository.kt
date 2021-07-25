package com.example.composehoo.db.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.composehoo.db.dao.FavouriteShoeDao
import com.example.composehoo.db.data.FavouriteShoe
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.util.*

class FavouriteShoeRepository private constructor(private val favouriteShoeDao: FavouriteShoeDao) {

    /**
     * 查看某个用户是否有喜欢记录
     */
    fun findFavouriteShoe(userId: Long, shoeId: Long): LiveData<FavouriteShoe?> =
        favouriteShoeDao.findFavouriteShoeByUserIdAndShoeId(userId, shoeId)

    suspend fun findFavouriteShoes(userId: Long): List<FavouriteShoe> =
        favouriteShoeDao.findFavouriteShoesByUserId(userId)


    /**
     * 收藏一双鞋
     */
    suspend fun createFavouriteShoe(userId: Long, shoeId: Long) {
        favouriteShoeDao.insertFavouriteShoe(FavouriteShoe(shoeId, userId, Calendar.getInstance()))
    }

    suspend fun deleteFavourite(favouriteShoe: FavouriteShoe) {
        withContext(IO) {
            favouriteShoeDao.deleteFavouriteShoe(favouriteShoe)
        }
    }

    companion object {
        @Volatile
        private var instance: FavouriteShoeRepository? = null

        fun getInstance(favouriteShoeDao: FavouriteShoeDao): FavouriteShoeRepository =
            instance ?: synchronized(this) {
                instance
                    ?: FavouriteShoeRepository(favouriteShoeDao).also {
                        instance = it
                    }
            }

    }
}