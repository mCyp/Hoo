package com.joe.jetpackdemo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.joe.jetpackdemo.db.dao.FavouriteShoeDao
import com.joe.jetpackdemo.db.data.FavouriteShoe
import com.joe.jetpackdemo.db.data.Shoe
import com.joe.jetpackdemo.db.repository.FavouriteShoeRepository
import com.joe.jetpackdemo.db.repository.ShoeRepository

class DetailModel constructor(shoeRepository: ShoeRepository, favouriteShoeRepository: FavouriteShoeRepository, shoeId: Long,userId:Long) :
    ViewModel() {

    // 鞋
    val shoe: LiveData<Shoe> = shoeRepository.getShoeById(shoeId)

    // 收藏记录
    val favouriteShoe:LiveData<FavouriteShoe?> = favouriteShoeRepository.findFavouriteShoe(userId,shoeId)
}