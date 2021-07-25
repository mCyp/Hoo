package com.example.composehoo.ui.page.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.composehoo.R
import com.example.composehoo.db.RepositoryProvider
import com.example.composehoo.ui.common.view.HooAppBar
import com.example.composehoo.ui.common.view.HooCutButton
import com.example.composehoo.ui.common.view.HooTextField
import com.example.composehoo.ui.viewmodel.login.RegisterModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern

@Composable
fun RegisterPage(onClickBack: () -> Unit, onRegisterSuccess: () -> Unit) {
    Scaffold(
        topBar = {
            HooAppBar(
                titleStr = stringResource(id = R.string.register),
                onBackClick = onClickBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            )
        },
    ) {
        RegisterContent(onRegisterSuccess)
    }
}


@Composable
fun RegisterContent(onRegisterSuccess: () -> Unit) {
    val context = LocalContext.current
    val registerErrorText = stringResource(id = R.string.register_error_text)
    val userRepository = RepositoryProvider.providerUserRepository(context = context)
    val registerModel: RegisterModel =
        com.example.composehoo.ui.common.ext.viewModel { RegisterModel(userRepository) }
    // 携程相关
    val scope = rememberCoroutineScope()
    var currentJob by remember { mutableStateOf<Job?>(null) }
    var email by remember { mutableStateOf("") }
    var account by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.register_join_us),
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                .padding(4.dp)
        )
        HooTextField(
            content = email,
            onTextChanged = { str ->
                email = str
                val check =
                    "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
                val regex: Pattern = Pattern.compile(check)
                val matcher: Matcher = regex.matcher(str)
                isError = !matcher.matches()
            },
            id = R.drawable.common_ic_email,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp),
            isError = isError,
            label = stringResource(id = R.string.common_email)
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
            label = stringResource(id = R.string.common_pwd)
        )

        Spacer(modifier = Modifier.weight(2f))

        HooCutButton(btnText = stringResource(id = R.string.login_sign_in), onClick = {
            currentJob?.cancel()
            currentJob = scope.launch {
                val userId = registerModel.register(email, account, pwd)
                if (userId > 0L) {
                    onRegisterSuccess()
                } else {
                    Toast.makeText(context, registerErrorText, Toast.LENGTH_SHORT).show()
                }
            }
        })

        Spacer(modifier = Modifier.weight(1f))

    }
}