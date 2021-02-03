package com.joe.jetpackdemo.viewmodel

import android.content.Context
import com.joe.jetpackdemo.common.BaseConstant
import com.joe.jetpackdemo.db.RepositoryProvider
import com.joe.jetpackdemo.db.repository.FavouriteShoeRepository
import com.joe.jetpackdemo.db.repository.ShoeRepository
import com.joe.jetpackdemo.db.repository.StorageDataRepository
import com.joe.jetpackdemo.db.repository.UserRepository
import com.joe.jetpackdemo.utils.AppPrefsUtils
import com.joe.jetpackdemo.viewmodel.factory.*

/**
 * ViewModel提供者
 */
object CustomViewModelProvider {

    fun providerRegisterModel(context: Context): RegisterModelFactory {
        val repository: UserRepository = RepositoryProvider.providerUserRepository(context)
        return RegisterModelFactory(repository)
    }

    fun providerLoginModel(context: Context): LoginModelFactory {
        val repository: UserRepository = RepositoryProvider.providerUserRepository(context)
        return LoginModelFactory(repository, context)
    }

    fun providerShoeModel(context: Context): ShoeModelFactory {
        val repository: ShoeRepository = RepositoryProvider.providerShoeRepository(context)
        return ShoeModelFactory(repository)
    }

    fun providerFavouriteModel(context: Context): FavouriteModelFactory {
        val repository: ShoeRepository = RepositoryProvider.providerShoeRepository(context)
        val userId:Long = AppPrefsUtils.getLong(BaseConstant.SP_USER_ID)
        return FavouriteModelFactory(repository,userId)
    }

    fun providerMeModel(context: Context):MeModelFactory{
        val repository:UserRepository = RepositoryProvider.providerUserRepository(context)
        return MeModelFactory(repository)
    }

    /**
     * @shoeId 鞋子的Id
     * @userId 用户的Id
     */
    fun providerDetailModel(context: Context, shoeId: Long, userId: Long): FavouriteShoeModelFactory {
        val repository: ShoeRepository = RepositoryProvider.providerShoeRepository(context)
        val favShoeRepository: FavouriteShoeRepository = RepositoryProvider.providerFavouriteShoeRepository(context)
        return FavouriteShoeModelFactory(repository, favShoeRepository, shoeId, userId)
    }

    fun provideStorageDataModel(context: Context):StorageModelFactory{
        val repository:StorageDataRepository = RepositoryProvider.providerStorageDataRepository(context)
        return StorageModelFactory(repository)
    }
}