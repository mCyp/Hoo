package com.example.composehoo.ui.page.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.composehoo.R

sealed class MainType(val route: String, @StringRes val name: Int, @DrawableRes val icon: Int) {
    object Shoe: MainType("home", R.string.main_home, R.drawable.main_ic_market)
    object Favourite: MainType("favourite", R.string.main_favourite, R.drawable.main_ic_favorite)
    object Me: MainType("me", R.string.main_me, R.drawable.main_ic_me)
}