package com.example.composehoo.ui.viewmodel.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composehoo.db.data.FavouriteShoe
import com.example.composehoo.db.data.Shoe
import com.example.composehoo.db.data.User
import com.example.composehoo.db.repository.FavouriteShoeRepository
import com.example.composehoo.db.repository.ShoeRepository
import com.example.composehoo.db.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailModel(private val shoeRepository: ShoeRepository, private val favouriteShoeRepository: FavouriteShoeRepository): ViewModel() {
    fun queryUser(id: Long): LiveData<Shoe?>{
        return shoeRepository.getShoeById(id)
    }

    fun queryFavourite(shoeId: Long, userId: Long): LiveData<FavouriteShoe?>{
        return favouriteShoeRepository.findFavouriteShoe(userId, shoeId)
    }

     fun favShoe(shoeId: Long, userId: Long){
         viewModelScope.launch(Dispatchers.IO) {
             favouriteShoeRepository.createFavouriteShoe(userId, shoeId)
         }
    }

    fun disFavShoe(favouriteShoe: FavouriteShoe){
        viewModelScope.launch {
            favouriteShoeRepository.deleteFavourite(favouriteShoe)
        }
    }
}