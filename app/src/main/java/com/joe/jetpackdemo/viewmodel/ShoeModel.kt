package com.joe.jetpackdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.joe.jetpackdemo.common.createPager
import com.joe.jetpackdemo.db.datasource.CustomPageDataSource
import com.joe.jetpackdemo.db.repository.ShoeRepository

class ShoeModel constructor(val shoeRepository: ShoeRepository) : ViewModel() {

    var shoes = Pager(config = PagingConfig(
        pageSize = 20
        , prefetchDistance = 10
        , enablePlaceholders = false
        , initialLoadSize = 20
    ), pagingSourceFactory = { CustomPageDataSource(shoeRepository) }).flow

    fun setBrand(br: String) {
        if (br == ALL) {
            shoes = Pager(config = PagingConfig(
                pageSize = 20
                , prefetchDistance = 10
                , enablePlaceholders = false
                , initialLoadSize = 20
            ), pagingSourceFactory = { CustomPageDataSource(shoeRepository) }).flow
        } else {
            val array: Array<String> =
                when (br) {
                    NIKE -> arrayOf("Nike", "Air Jordan")
                    ADIDAS -> arrayOf("Adidas")
                    else -> arrayOf(
                        "Converse", "UA"
                        , "ANTA"
                    )
                }
            shoes = shoeRepository.getShoesByBrand(array).createPager(20, 20).flow
        }
    }

    /*fun clearBrand() {
        this.brand.value = ALL
    }*/

    companion object {
        const val ALL = "所有"
        const val NIKE = "Nike"
        const val ADIDAS = "Adidas"
        const val OTHER = "other"
    }
}