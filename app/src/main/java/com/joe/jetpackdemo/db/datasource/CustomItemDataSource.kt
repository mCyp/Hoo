package com.joe.jetpackdemo.db.datasource

import androidx.paging.ItemKeyedDataSource
import com.joe.jetpackdemo.db.data.Shoe

/**
 * 自定义ItemKeyedDataSource资源
 * 演示Page库的时候使用
 */
class CustomItemDataSource:ItemKeyedDataSource<Int,Shoe>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Shoe>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Shoe>) {

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Shoe>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getKey(item: Shoe): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}