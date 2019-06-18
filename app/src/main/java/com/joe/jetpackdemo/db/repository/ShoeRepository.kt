package com.joe.jetpackdemo.db.repository

import com.joe.jetpackdemo.db.dao.ShoeDao
import com.joe.jetpackdemo.db.dao.UserDao
import com.joe.jetpackdemo.db.data.Shoe

class ShoeRepository private constructor(private val shoeDao: ShoeDao) {

    fun getAllShoes() = shoeDao.getAllShoes()

    /**
     * 通过品牌查询鞋子
     */
    fun getShoesByBrand(brand:String) = shoeDao.findShoeByBrand(brand)

    /**
     * 插入鞋子的集合
     */
    fun insertShoes(shoes: List<Shoe>) = shoeDao.insertShoes(shoes)

    companion object {
        @Volatile
        private var instance: ShoeRepository? = null

        fun getInstance(shoeDao: ShoeDao): ShoeRepository =
            instance ?: synchronized(this) {
                instance
                    ?: ShoeRepository(shoeDao).also {
                        instance = it
                    }
            }

    }
}