package com.example.composehoo.ui.common.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HooButton(
    btnText: String,
    onClick: () -> Unit = {},
    shape: Shape,
    leftPadding: Dp = 0.dp,
    topPadding: Dp = 0.dp,
    rightPadding: Dp = 0.dp,
    bottomPadding: Dp = 0.dp,
    leftMargin: Dp = 0.dp,
    topMargin: Dp = 0.dp,
    rightMargin: Dp = 0.dp,
    bottomMargin: Dp = 0.dp,
    borderWidth: Dp = 0.dp,
    borderColor: Color
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(Dp(56f))
            .padding(leftMargin, topMargin, rightMargin, bottomMargin)
            .padding(leftPadding, topPadding, rightPadding, bottomPadding),
        shape = shape
    ) {
        Text(text = btnText, textAlign = TextAlign.Center, style = MaterialTheme.typography.button)
    }
}

@Composable
fun HooCutButton(
    btnText: String, onClick: () -> Unit = {},
    leftMargin: Dp = 0.dp,
    topMargin: Dp = 0.dp,
    rightMargin: Dp = 0.dp,
    bottomMargin: Dp = 0.dp,
) {
    HooButton(
        btnText,
        onClick,
        CutCornerShape(Dp(10f)),
        leftPadding = 4.dp,
        topPadding = 4.dp,
        rightPadding = 4.dp,
        bottomPadding = 4.dp,
        leftMargin,
        topMargin,
        rightMargin,
        bottomMargin,
        borderWidth = 0.dp,
        borderColor = MaterialTheme.colors.onBackground
    )
}

@Composable
fun HooRoundCornerButton(
    btnText: String, onClick: () -> Unit = {}, cornerSize: Float,
    leftMargin: Dp = 0.dp,
    topMargin: Dp = 0.dp,
    rightMargin: Dp = 0.dp,
    bottomMargin: Dp = 0.dp,
) {
    HooButton(
        btnText, onClick, RoundedCornerShape(Dp(cornerSize)), leftPadding = 4.dp,
        topPadding = 4.dp,
        rightPadding = 4.dp,
        bottomPadding = 4.dp,
        leftMargin,
        topMargin,
        rightMargin,
        bottomMargin,
        borderWidth = 1.dp,
        borderColor = MaterialTheme.colors.onBackground
    )
}


@Composable
fun HooTextField(
    content: String = "",
    onTextChanged: (String) -> Unit = {},
    @DrawableRes id: Int,
    modifier: Modifier = Modifier.fillMaxWidth(),
    isError: Boolean = false,
    label: String = "",
    isPassword: Boolean = false
) {
    val visualTransformation = if(!isPassword) VisualTransformation.None else PasswordVisualTransformation()
    OutlinedTextField(
        // keyboardOptions = KeyboardOptions(keyboardType = if(isPassword) KeyboardType.Password else KeyboardType.Text),
        visualTransformation = visualTransformation,
        value = content,
        onValueChange = onTextChanged,
        modifier = modifier,
        leadingIcon = { HooInnerIcon(id = id) },
        isError = isError,
        label = { HooInnerLabel(label = label) },
        textStyle = MaterialTheme.typography.body1
    )
}

@Composable
private fun HooInnerLabel(label: String) {
    Box(contentAlignment = Alignment.CenterStart) {
        Text(text = label, style = MaterialTheme.typography.body1)
    }
}

@Composable
private fun HooInnerIcon(@DrawableRes id: Int) {
    Icon(
        painter = painterResource(id = id),
        contentDescription = "前置图标"
    )
}


