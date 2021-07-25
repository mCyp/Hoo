package com.example.composehoo.ui.viewmodel.main

import androidx.lifecycle.ViewModel
import com.example.composehoo.base.BaseConstant
import com.example.composehoo.db.data.Shoe
import com.example.composehoo.db.repository.ShoeRepository

class ShoeModel(val shoeRepository: ShoeRepository) : ViewModel() {

    suspend fun refresh(): List<Shoe>?{
        return shoeRepository.getPageShoes(1, BaseConstant.PAGE_SIZE.toLong())
    }
}