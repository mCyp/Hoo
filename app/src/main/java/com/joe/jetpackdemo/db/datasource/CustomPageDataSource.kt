package com.joe.jetpackdemo.db.datasource

import androidx.paging.PagingSource
import com.joe.jetpackdemo.db.data.Shoe
import com.joe.jetpackdemo.db.repository.ShoeRepository
import java.lang.Exception

/**
 * 自定义PageKeyedDataSource
 * 演示Page库的时候使用
 */
private const val SHOE_START_INDEX = 0;

class CustomPageDataSource(private val shoeRepository: ShoeRepository) : PagingSource<Int, Shoe>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Shoe> {
        val pos = params.key ?: SHOE_START_INDEX
        val startIndex = pos * params.loadSize + 1
        val endIndex = (pos + 1) * params.loadSize
        return try {
            Thread.sleep(5000)
            // 从数据库拉去数据
            val shoes = shoeRepository.getPageShoes(startIndex.toLong(), endIndex.toLong())
            // 返回你的分页结果，并填入前一页的 key 和后一页的 key
            LoadResult.Page(
                shoes,
                if (pos <= SHOE_START_INDEX) null else pos - 1,
                if (shoes.isNullOrEmpty()) null else pos + 1
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }

    }
}