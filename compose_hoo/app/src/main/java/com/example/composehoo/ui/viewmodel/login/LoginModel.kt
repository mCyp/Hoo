package com.example.composehoo.ui.viewmodel.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composehoo.db.RepositoryProvider
import com.example.composehoo.db.data.Shoe
import com.example.composehoo.db.data.User
import com.example.composehoo.db.repository.UserRepository
import com.example.composehoo.ui.page.login.SP_FIRST_INIT
import com.example.composehoo.utils.AppPrefsUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginModel (val repository: UserRepository): ViewModel() {

    // var repository: UserRepository? = null

    suspend fun login(account: String, pwd: String): User? = repository?.login(account, pwd)

    fun onFirstLaunch(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            context.assets.open("shoes.json").use {
                JsonReader(it.reader()).use {
                    val shoeType = object : TypeToken<List<Shoe>>() {}.type
                    val shoeList: List<Shoe> = Gson().fromJson(it, shoeType)

                    val shoeDao = RepositoryProvider.providerShoeRepository(context)
                    shoeDao.insertShoes(shoeList)
                    for (i in 0..2) {
                        for (j in shoeList.indices) {
                            val shoe = shoeList.get(j)
                            shoe.id = j + 1 + (shoeList.size * i).toLong()
                            Log.i("wangjie",shoe.toString())
                        }
                        shoeDao.insertShoes(shoeList)
                    }
                }
            }
            AppPrefsUtils.putBoolean(SP_FIRST_INIT, true)
        }
    }

}