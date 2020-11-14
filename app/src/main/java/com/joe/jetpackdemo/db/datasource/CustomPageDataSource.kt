package com.joe.jetpackdemo.db.datasource

import androidx.paging.PagingSource
import com.joe.jetpackdemo.db.data.Shoe
import com.joe.jetpackdemo.db.repository.ShoeRepository

/**
 * 自定义PageKeyedDataSource
 * 演示Page库的时候使用
 */
private const val SHOE_START_INDEX = 0;

class CustomPageDataSource(private val shoeRepository: ShoeRepository) : PagingSource<Int, Shoe>() {

    private val TAG: String by lazy {
        this::class.java.simpleName
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Shoe> {
        val pos = params.key ?: SHOE_START_INDEX
        val startIndex = pos.toLong()
        val endIndex = startIndex + params.loadSize
        val shoes = shoeRepository.getPageShoes(startIndex, endIndex)
        return LoadResult.Page(
            shoes,
            if (pos == SHOE_START_INDEX) null else pos - 1,
            if (shoes.isNullOrEmpty()) null else pos + 1
        )
    }
}