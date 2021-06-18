package com.joe.jetpackdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.joe.jetpackdemo.db.repository.ShoeRepository
import kotlinx.coroutines.flow.*

class ShoeModel constructor(private val shoeRepository: ShoeRepository) : ViewModel() {

    private val selectedBrand = MutableStateFlow(ALL)

    /**
     * @param config 分页的参数
     * @param pagingSourceFactory 单一数据源的工厂，在闭包中提供一个PageSource即可
     * @param remoteMediator 同时支持网络请求和数据库请求的数据源
     * @param initialKey 初始化使用的key
     */
    val shoes = selectedBrand.flatMapLatest { selected ->
        val brand: Array<String>? = when (selected) {
            ALL -> null
            NIKE -> arrayOf("Nike", "Air Jordan")
            ADIDAS -> arrayOf("Adidas")
            else -> arrayOf("Converse", "UA", "ANTA")
        }

        Pager(config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            initialLoadSize = 20
        ), pagingSourceFactory = {
            brand?.let { shoeRepository.getShoesByBrandPagingSource(it) }
                ?: shoeRepository.getAllShoesPagingSource()
        }).flow
    }

    fun setBrand(br: String) {
        selectedBrand.value = br
    }

    companion object {
        const val ALL = "所有"
        const val NIKE = "Nike"
        const val ADIDAS = "Adidas"
        const val OTHER = "other"
    }
}