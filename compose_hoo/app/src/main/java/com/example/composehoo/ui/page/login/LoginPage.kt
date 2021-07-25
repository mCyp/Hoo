package com.example.composehoo.ui.page.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.composehoo.R
import com.example.composehoo.ui.common.view.HooAppBar
import androidx.compose.ui.platform.LocalContext
import com.example.composehoo.base.BaseConstant
import com.example.composehoo.db.RepositoryProvider
import com.example.composehoo.ui.common.view.HooCutButton
import com.example.composehoo.ui.common.view.HooTextField
import com.example.composehoo.ui.viewmodel.login.LoginModel
import com.example.composehoo.utils.AppPrefsUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

const val SP_FIRST_INIT = "sp_first_init"

@Composable
fun LoginPage(onClickBack: () -> Unit = {}, onLoginSuccess: () -> Unit = {}) {
    Scaffold(
        topBar = {
            HooAppBar(
                titleStr = stringResource(id = R.string.login),
                onBackClick = onClickBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            )
        },
    ) {
        LoginContent(onLoginSuccess = onLoginSuccess)
    }
}

@Composable
fun LoginContent(
    onLoginSuccess: () -> Unit,
) {
    val loginErrorText = stringResource(id = R.string.login_error_text)

    val context = LocalContext.current
    val userRepository = RepositoryProvider.providerUserRepository(context = context)
    val loginModel: LoginModel =
        com.example.composehoo.ui.common.ext.viewModel { LoginModel(userRepository) }
    // 携程相关
    val scope = rememberCoroutineScope()
    var currentJob by remember { mutableStateOf<Job?>(null) }
    var account by remember {
        mutableStateOf(AppPrefsUtils.getString(BaseConstant.LOGIN_NAME) ?: "")
    }
    var pwd by remember {
        mutableStateOf("")
    }
    val isInitData = AppPrefsUtils.getBoolean(SP_FIRST_INIT, false)
    if (!isInitData) {
        loginModel.onFirstLaunch(context)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.login_welcome_back),
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                .padding(4.dp)
        )

        HooTextField(
            content = account,
            onTextChanged = { str -> account = str },
            id = R.drawable.common_ic_account,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp),
            label = stringResource(id = R.string.common_account)
        )
        HooTextField(
            content = pwd,
            onTextChanged = { str -> pwd = str },
            id = R.drawable.common_ic_pwd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(0.dp),
            label = stringResource(id = R.string.common_pwd),
            isPassword = true
        )

        Spacer(modifier = Modifier.weight(2f))

        HooCutButton(btnText = stringResource(id = R.string.login_sign_in), onClick = {
            currentJob?.cancel()
            currentJob = scope.launch {
                val user = loginModel.login(account, pwd)
                if (user != null) {
                    AppPrefsUtils.putString(BaseConstant.LOGIN_NAME, user.account)
                    AppPrefsUtils.putLong(BaseConstant.USER_ID, user.id)
                    onLoginSuccess()
                } else {
                    Toast.makeText(context, loginErrorText, Toast.LENGTH_SHORT).show()
                }
            }
        })

        Spacer(modifier = Modifier.weight(1f))

    }
}

