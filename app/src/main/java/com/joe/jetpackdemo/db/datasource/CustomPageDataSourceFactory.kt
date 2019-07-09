package com.joe.jetpackdemo.db.datasource

import androidx.paging.DataSource
import com.joe.jetpackdemo.db.data.Shoe
import com.joe.jetpackdemo.db.repository.ShoeRepository

/**
 * 构建CustomPageDataSource的工厂
 */
class CustomPageDataSourceFactory(private val shoeRepository: ShoeRepository):DataSource.Factory<Int,Shoe>() {
    override fun create(): DataSource<Int, Shoe> {
        return CustomPageDataSource(shoeRepository)
    }
}