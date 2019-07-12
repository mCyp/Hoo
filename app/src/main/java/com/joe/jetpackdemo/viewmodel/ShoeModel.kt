package com.joe.jetpackdemo.viewmodel

import android.renderscript.Sampler
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.joe.jetpackdemo.common.createPagerList
import com.joe.jetpackdemo.db.data.Shoe
import com.joe.jetpackdemo.db.datasource.CustomPageDataSource
import com.joe.jetpackdemo.db.datasource.CustomPageDataSourceFactory
import com.joe.jetpackdemo.db.repository.ShoeRepository

class ShoeModel constructor(shoeRepository: ShoeRepository) : ViewModel() {

    // 品牌的观察对象 默认观察所有的品牌
    private val brand = MutableLiveData<String>().apply {
        value = ALL
    }

    // 鞋子集合的观察类
    val shoes: LiveData<PagedList<Shoe>> = brand.switchMap {
        // Room数据库查询，只要知道返回的是LiveData<List<Shoe>>即可
        if (it == ALL) {
            // LivePagedListBuilder<Int,Shoe>( shoeRepository.getAllShoes(),PagedList.Config.Builder()
            LivePagedListBuilder<Int, Shoe>(
                CustomPageDataSourceFactory(shoeRepository) // DataSourceFactory
                , PagedList.Config.Builder()
                    .setPageSize(10) // 分页加载的数量
                    .setEnablePlaceholders(false) // 当item为null是否使用PlaceHolder展示
                    .setInitialLoadSizeHint(10) // 预加载的数量
                    .build()
            )
                .build()
            //shoeRepository.getAllShoes()
        } else {
            val array: Array<String> =
                when (it) {
                    NIKE -> arrayOf("Nike", "Air Jordan")
                    ADIDAS -> arrayOf("Adidas")
                    else -> arrayOf(
                        "Converse", "UA"
                        , "ANTA"
                    )
                }
            shoeRepository.getShoesByBrand(array)
                .createPagerList(6, 6)
        }
    }

    // 鞋子集合的观察类
    /*val shoes: LiveData<PagedList<Shoe>> = LivePagedListBuilder<Int, Shoe>(
        CustomPageDataSourceFactory(shoeRepository) // DataSourceFactory
        , PagedList.Config.Builder()
            .setPageSize(10) // 分页加载的数量
            .setEnablePlaceholders(false) // 当item为null是否使用PlaceHolder展示
            .setInitialLoadSizeHint(10) // 预加载的数量
            .build())
        .build()*/


    fun setBrand(brand: String) {
        this.brand.value = brand

        this.brand.map {

        }
    }

    fun clearBrand() {
        this.brand.value = ALL
    }

    companion object {
        public const val ALL = "所有"

        public const val NIKE = "Nike"
        public const val ADIDAS = "Adidas"
        public const val OTHER = "other"
    }
}