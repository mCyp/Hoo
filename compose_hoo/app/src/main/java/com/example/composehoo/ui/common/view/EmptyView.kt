package com.example.composehoo.ui.common.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.composehoo.R

@Composable
fun HooEmptyView(){

}

@Composable
fun HooLoadingView(){
    Column(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            strokeWidth = 2.dp, modifier = Modifier
                .width(36.dp)
                .height(36.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.common_loading),
            style = MaterialTheme.typography.body1,
        )
    }
}