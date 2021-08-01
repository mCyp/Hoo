package com.example.composehoo.ui.common.view.refresh

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.composehoo.R
import com.example.composehoo.ui.common.ext.gray400
import com.example.composehoo.ui.common.ext.gray50
import com.example.composehoo.ui.common.ext.red50
import com.example.composehoo.ui.common.view.HooCutButton
import com.example.composehoo.ui.theme.*


@Composable
fun LoadStateView(
    state: LoadState,
    loadMore: () -> Unit,
    refreshRetry: () -> Unit,
    loadRetry: () -> Unit
) {
    /*if (state is LoadState.Loading) {
        LoadingMore()
        loadMore()
    }*/
    when (state) {
        is LoadState.Loading -> {
            LoadingMore()
            loadMore()

        }
        is LoadState.Complete -> {
            LoadingComplete()
        }
        is LoadState.Error -> {
            LoadingError(loadRetry)
        }
        is LoadState.RefreshingError -> {
            RefreshError(refreshRetry)
        }
        is LoadState.Empty -> {
            RefreshEmpty()
        }
    }
}

@Composable
fun LoadingMore() {
    Log.d("wangjie", "build loading more")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
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

@Composable
fun LoadingComplete() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.common_load_complete),
            style = MaterialTheme.typography.body1,
        )
    }
}

@Composable
fun LoadingError(retry: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = retry,
            modifier = Modifier
                .height(42.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
        ) {
            Icon(
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp),
                painter = painterResource(id = R.drawable.common_ic_error),
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "图标"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.common_load_error_desc),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Composable
fun RefreshError(retry: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HooErrorView()
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.common_load_error),
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.common_load_error_desc_one),
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = retry,
            modifier = Modifier
                .height(36.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
        ) {
            Text(
                text = stringResource(id = R.string.common_load_error_retry),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Composable
fun RefreshEmpty() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HooEmptyView()
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.common_load_empty),
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.common_load_empty_desc),
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun HooErrorView() {
    val outColor = MaterialTheme.colors.red50
    val innerColor = MaterialTheme.colors.error
    HooImageView(
        outColor = outColor,
        innerColor = innerColor,
        imgRes = R.drawable.common_ic_close,
        true
    )
}

@Composable
fun HooEmptyView() {
    val outColor = MaterialTheme.colors.gray50
    val innerColor = MaterialTheme.colors.gray400
    HooImageView(outColor = outColor, innerColor = innerColor, imgRes = R.drawable.common_ic_empty)
}

@Composable
fun HooImageView(
    outColor: Color,
    innerColor: Color,
    @DrawableRes imgRes: Int,
    isUseCutCorner: Boolean = false
) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(160.dp)
            .clip(if (isUseCutCorner) CutCornerShape(40.dp) else RoundedCornerShape(8.dp))
            .background(color = outColor)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .clip(if (isUseCutCorner) CutCornerShape(25.dp) else RoundedCornerShape(5.dp))
                .background(color = innerColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imgRes),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
                modifier = Modifier
                    .width(56.dp)
                    .height(56.dp),
                contentDescription = "图片"
            )
        }
    }
}


@Preview
@Composable
fun show() {
    LoadingError()
}
