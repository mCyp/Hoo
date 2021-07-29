package com.example.composehoo.db.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.composehoo.db.data.Shoe
import com.example.composehoo.db.repository.ShoeRepository
import kotlinx.coroutines.delay

class ShoePagingSource(private val shoeRepo: ShoeRepository): PagingSource<Int,Shoe>() {
    override fun getRefreshKey(state: PagingState<Int, Shoe>): Int?  = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Shoe> {
        val startKey = params.key ?: return LoadResult.Page(emptyList(), null, null)
        val size = params.loadSize
        val endKey = startKey + size - 1
        val list = shoeRepo.getPageShoes(startKey.toLong(), endKey.toLong())
        return LoadResult.Page(list, if(startKey == 0) null else startKey - size, if(!list.isNullOrEmpty()) startKey + size else null)
    }
}