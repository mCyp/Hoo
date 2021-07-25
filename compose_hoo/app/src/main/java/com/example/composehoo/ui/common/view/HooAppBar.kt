package com.example.composehoo.ui.common.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.composehoo.R
import com.example.composehoo.ui.theme.ComposeHooTheme

@Preview
@Composable
fun testAppBar() {
    ComposeHooTheme() {
        HooAppBar(titleStr = "Login")
    }

}

@Composable
fun HooAppBar(
    titleStr: String = "",
    backColor: androidx.compose.ui.graphics.Color = MaterialTheme.colors.background,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    elevation: Dp = 0.dp,
    isShowIcon: Boolean = true
) {
    TopAppBar(
        title = {
            AppTitle(title = titleStr)
        },
        modifier = modifier,
        navigationIcon = { if (isShowIcon) BackIcon(onBackClick) },
        actions = actions,
        backgroundColor = backColor,
        elevation = elevation
    )
}

@Composable
fun BackIcon(onBackClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.common_ic_back),
        contentDescription = "返回按钮",
        modifier = Modifier
            .size(52.dp)
            .clickable(true, onClick = onBackClick)
            .padding(14.dp),
        colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
    )
}

@Composable
fun AppTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.primary
    )
}

