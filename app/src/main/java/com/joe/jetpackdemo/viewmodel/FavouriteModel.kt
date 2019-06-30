package com.joe.jetpackdemo.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.joe.jetpackdemo.db.data.Shoe
import com.joe.jetpackdemo.db.repository.ShoeRepository

class FavouriteModel constructor(shoeRepository: ShoeRepository,userId:Long) : ViewModel() {

    // 鞋子集合的观察类
    val shoes: LiveData<List<Shoe>> = shoeRepository.getShoesByUserId(userId)

}