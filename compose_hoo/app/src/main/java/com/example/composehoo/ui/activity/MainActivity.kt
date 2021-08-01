package com.example.composehoo.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composehoo.R
import com.example.composehoo.base.BaseConstant
import com.example.composehoo.ui.common.ext.gray400
import com.example.composehoo.ui.common.view.HooAppBar
import com.example.composehoo.ui.common.view.refresh.HooErrorView
import com.example.composehoo.ui.common.view.refresh.LoadingError
import com.example.composehoo.ui.common.view.refresh.RefreshEmpty
import com.example.composehoo.ui.common.view.refresh.RefreshError
import com.example.composehoo.ui.page.main.FavouriteShoePage
import com.example.composehoo.ui.page.main.MainType
import com.example.composehoo.ui.page.main.MePage
import com.example.composehoo.ui.page.main.ShoePage
import com.example.composehoo.ui.theme.*
import com.example.composehoo.utils.AppPrefsUtils
import com.google.accompanist.pager.ExperimentalPagerApi


class MainActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContent()
        }
    }

}


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun MainContent() {
    var currentSelect by remember { mutableStateOf(MainType.Shoe.route) }
    val controller = rememberNavController()
    // 控制黑夜模式
    val useDark = remember { mutableStateOf(false) }
    val isUserSystem = AppPrefsUtils.getBoolean(BaseConstant.USE_SYSTEM_DARK, false)
    val darkChangeFun: (Boolean, Boolean) -> Unit = { followSystem, systemDark ->
        if (followSystem) {
            useDark.value = systemDark
        } else {
            useDark.value = AppPrefsUtils.getBoolean(BaseConstant.USE_DARK, false)
        }
    }
    val userDarkInSystem = isSystemInDarkTheme()
    darkChangeFun(isUserSystem, userDarkInSystem)
    ComposeHooTheme(useDark.value) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.primary)
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.app_title),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onPrimary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    AnimatedVisibility(visible = currentSelect == "me") {
                        Text(
                            text = stringResource(id = if (useDark.value) R.string.main_dark else R.string.main_light),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.background
                        )
                    }
                    AnimatedVisibility(visible = currentSelect == "me") {
                        Switch(
                            checked = useDark.value, enabled = !isUserSystem,
                            onCheckedChange = {
                                useDark.value = it
                                AppPrefsUtils.putBoolean(BaseConstant.USE_DARK, it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextMainWhiteColorDark,
                                uncheckedThumbColor = TextMainWhiteColor
                            )
                        )
                    }
                }
            },
            bottomBar = {
                MainBottomBar(selectValue = currentSelect) { route ->
                    currentSelect = route
                    controller.navigate(currentSelect) {
                        popUpTo(controller.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

            },
        ) {
            HooMainNavHost(
                navController = controller,
                start = MainType.Shoe.route,
                Modifier
                    .fillMaxWidth().padding(bottom = it.calculateBottomPadding()),
                followSystemChange = { isFollowSystem ->
                    darkChangeFun(isFollowSystem, userDarkInSystem)
                    AppPrefsUtils.putBoolean(BaseConstant.USE_SYSTEM_DARK, isFollowSystem)
                }
            )
        }
    }
}


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun HooMainNavHost(
    navController: NavHostController,
    start: String = "",
    modifier: Modifier = Modifier,
    followSystemChange: (Boolean) -> Unit
) {
    NavHost(navController = navController, startDestination = start, modifier = modifier) {
        composable(MainType.Shoe.route) {
            ShoePage()
        }
        composable(MainType.Favourite.route) {
            FavouriteShoePage()
        }
        composable(MainType.Me.route) {
            MePage(followSystemChange)
        }
    }
}

@Composable
fun MainBottomBar(selectValue: String, onSelect: (String) -> Unit = {}) {
    BottomNavigation(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 2.dp
    ) {
        BottomNavigationItem(
            selected = selectValue == MainType.Shoe.route,
            onClick = { onSelect(MainType.Shoe.route) },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = MaterialTheme.colors.gray400,
            icon = {
                Icon(
                    painter = painterResource(id = MainType.Shoe.icon),
                    contentDescription = MainType.Shoe.route,
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            },
            label = {
                Text(text = stringResource(id = MainType.Shoe.name))
            },
            alwaysShowLabel = true
        )
        BottomNavigationItem(
            selected = selectValue == MainType.Favourite.route,
            onClick = { onSelect(MainType.Favourite.route) },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = MaterialTheme.colors.gray400,
            icon = {
                Icon(
                    painter = painterResource(id = MainType.Favourite.icon),
                    contentDescription = MainType.Favourite.route,
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            },
            label = {
                Text(text = stringResource(id = MainType.Favourite.name))
            },
            alwaysShowLabel = true
        )
        BottomNavigationItem(
            selected = selectValue == MainType.Me.route,
            onClick = { onSelect(MainType.Me.route) },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = MaterialTheme.colors.gray400,
            icon = {
                Icon(
                    painter = painterResource(id = MainType.Me.icon),
                    contentDescription = MainType.Me.route,
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            },
            label = {
                Text(text = stringResource(id = MainType.Me.name))
            },
            alwaysShowLabel = true
        )
    }
}