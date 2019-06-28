package com.joe.jetpackdemo.db.repository

import com.joe.jetpackdemo.db.dao.ShoeDao
import com.joe.jetpackdemo.db.data.Shoe

class ShoeRepository private constructor(private val shoeDao: ShoeDao) {

    fun getAllShoes() = shoeDao.getAllShoesLD()

    /**
     * 通过品牌查询鞋子
     */
    fun getShoesByBrand(brand:String) = shoeDao.findShoesByBrandLD(brand)

    /**
     * 通过Id查询一双鞋
     */
    fun getShoeById(id:Long) = shoeDao.findShoeByIdLD(id)

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