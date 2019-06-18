package com.joe.jetpackdemo.viewmodel

import androidx.lifecycle.*
import com.joe.jetpackdemo.db.data.Shoe
import com.joe.jetpackdemo.db.repository.ShoeRepository

class ShoeModel constructor(shoeRepository: ShoeRepository) : ViewModel() {

    // 品牌的观察对象 默认观察所有的品牌
    private val brand = MutableLiveData<String>().apply {
        value = ALL
    }

    // 鞋子集合的观察类
    val shoes: LiveData<List<Shoe>> = brand.switchMap {
        // Room数据库查询，只要知道返回的是LiveData<List<Shoe>>即可
        if (it == ALL) {
            shoeRepository.getAllShoes()
        } else {
            shoeRepository.getShoesByBrand(it)
        }
    }

    fun setBrand(brand:String){
        this.brand.value = brand

        this.brand.map {

        }
    }

    fun clearBrand(){
        this.brand.value = ALL
    }

    companion object {
        private const val ALL = "所有"
    }
}