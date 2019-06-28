package com.joe.jetpackdemo.viewmodel

import android.content.Context
import androidx.navigation.NavController
import com.joe.jetpackdemo.db.RepositoryProvider
import com.joe.jetpackdemo.db.repository.FavouriteShoeRepository
import com.joe.jetpackdemo.db.repository.ShoeRepository
import com.joe.jetpackdemo.db.repository.UserRepository
import com.joe.jetpackdemo.viewmodel.factory.FavouriteShoeModelFactory
import com.joe.jetpackdemo.viewmodel.factory.LoginModelFactory
import com.joe.jetpackdemo.viewmodel.factory.RegisterModelFactory
import com.joe.jetpackdemo.viewmodel.factory.ShoeModelFactory

/**
 * ViewModel提供者
 */
object CustomViewModelProvider {

    fun providerRegisterModel(context: Context, navController: NavController): RegisterModelFactory {
        val repository: UserRepository = RepositoryProvider.providerUserRepository(context)
        return RegisterModelFactory(repository, navController)
    }

    fun providerLoginModel(context: Context): LoginModelFactory {
        val repository: UserRepository = RepositoryProvider.providerUserRepository(context)
        return LoginModelFactory(repository, context)
    }

    fun providerShoeModel(context: Context): ShoeModelFactory {
        val repository: ShoeRepository = RepositoryProvider.providerShoeRepository(context)
        return ShoeModelFactory(repository)
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
}