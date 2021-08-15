package com.example.composehoo.ui.page.login

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.composehoo.R
import com.example.composehoo.ui.common.view.HooCutButton
import com.example.composehoo.ui.theme.ComposeHooTheme


@ExperimentalAnimationApi
@Composable
fun WelcomePage(
    onClickToLogin: () -> Unit = {},
    onClickToRegister: () -> Unit = {},
) {
    val configuration = LocalConfiguration.current
    val imageHeight = configuration.screenHeightDp * 0.45f

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome_bg),
                contentDescription = "Welcome bg",
                modifier = Modifier
                    .height(Dp(imageHeight))
                    .aspectRatio(1f)
            )

            Spacer(modifier = Modifier.weight(weight = 1f))
            Text(
                text = "Hoo is a App about sneakers.\nIf you want to learn more about sneakers.\nLet's Begin",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = Dp(32f))
            )
            HooCutButton(
                btnText = stringResource(id = R.string.welcome_login_desc),
                onClick = onClickToLogin,
                leftMargin = 16.dp,
                rightMargin = 16.dp
            )
            HooCutButton(
                btnText = stringResource(id = R.string.welcome_register_desc),
                onClick = onClickToRegister,
                leftMargin = 16.dp,
                rightMargin = 16.dp
            )
            Spacer(modifier = Modifier.weight(weight = 1f))
        }

    }

}

@ExperimentalAnimationApi
@Preview
@Composable
fun showWelCome() {
    ComposeHooTheme() {
        WelcomePage()
    }
}