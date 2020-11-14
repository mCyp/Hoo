package com.joe.jetpackdemo.common

import androidx.lifecycle.LiveData
import androidx.paging.*

fun <T : Any> PagingSource<Int, T>.createPager(pageSize:Int, defaultSize:Int): Pager<Int,T> {
    return Pager<Int, T>(
        config = PagingConfig(pageSize = pageSize,prefetchDistance = 10,enablePlaceholders = false,initialLoadSize = defaultSize),
        pagingSourceFactory = {this}
    )
}