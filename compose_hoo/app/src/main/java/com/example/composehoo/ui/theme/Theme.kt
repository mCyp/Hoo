package com.example.composehoo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.example.composehoo.base.BaseConstant
import com.example.composehoo.utils.AppPrefsUtils

private val DarkColorPalette = darkColors(
    primary = PrimaryColorDark,
    primaryVariant = PrimaryVariantDark,
    secondary = PrimaryColorDark,
    background = BgColorDark,
    onError = RedDark
)

private val LightColorPalette = lightColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryVariant,
    secondary = PrimaryColor,
    background = BgColor,
    error = Red,
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ComposeHooTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}