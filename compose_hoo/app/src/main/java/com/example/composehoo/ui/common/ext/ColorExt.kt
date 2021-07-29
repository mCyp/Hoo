package com.example.composehoo.ui.common.ext

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.composehoo.ui.theme.*

val Colors.grayBackgroundColor: Color
    @Composable get() = if(isLight) BgGrayColor else BgGrayColorDark

val Colors.textMainColor: Color
    @Composable get() = if(isLight) TextMainColor else TextMainColorDark

val Colors.textSecondColor: Color
    @Composable get() = if(isLight) GRAY400 else GRAY400DARK

val Colors.unSelectedColor: Color
    @Composable get() = if(isLight) GRAY400 else GRAY400DARK

val Colors.red50: Color
    @Composable get() = if(isLight) RED50 else RED50Dark

val Colors.gray50: Color
    @Composable get() = if(isLight) GRAY50 else GRAY50DARK

val Colors.gray400: Color
    @Composable get() = if(isLight) GRAY400 else GRAY400DARK