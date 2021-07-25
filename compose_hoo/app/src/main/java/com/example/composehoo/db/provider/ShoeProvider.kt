package com.example.composehoo.db.provider

import android.util.Log
import com.example.composehoo.base.BaseConstant
import com.example.composehoo.db.data.Shoe
import com.example.composehoo.db.repository.ShoeRepository
import kotlinx.coroutines.delay

class ShoeProvider(val repository: ShoeRepository): BaseProvider<Shoe>() {

    private var index: Int = 0;
    private val pageSize: Int = BaseConstant.PAGE_SIZE

    override suspend fun onRefresh(): Boolean {
        // delay(3000)
        index = 0
        val start = index.toLong()
        val end = (index + pageSize).toLong()
        val list = repository.getPageShoes(start + 1, end)
        currentList.clear()
        if(!list.isNullOrEmpty()){
            currentList.addAll(list)
            index = end.toInt()
            return true
        }
        return false
    }

    override suspend fun onLoadMore(): Boolean {
        // delay(3000)
        val start = index.toLong()
        val end = (index + pageSize).toLong()
        val list = repository.getPageShoes(start + 1, end)
        currentList.clear()
        if(!list.isNullOrEmpty()){
            currentList.addAll(list)
            index = end.toInt()
            return true
        }
        return false
    }
}