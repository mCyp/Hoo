package com.joe.jetpackdemo.common

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.joe.jetpackdemo.db.data.Shoe

fun <T> DataSource.Factory<Int, T>.createPagerList(pageSize:Int,defaultSize:Int): LiveData<PagedList<T>> {
    return LivePagedListBuilder<Int, T>(
        this, PagedList.Config.Builder()
            .setPageSize(2)
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(2).build()
    ).build()
}