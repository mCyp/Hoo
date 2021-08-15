package com.example.composehoo.ui.page.detail

import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberImagePainter
import com.bumptech.glide.Glide
import com.example.composehoo.R
import com.example.composehoo.ui.common.ext.gray50
import com.example.composehoo.ui.theme.BgColor
import com.example.composehoo.ui.theme.BlackAlpha10
import com.github.chrisbanes.photoview.PhotoView
import kotlin.math.absoluteValue


@ExperimentalComposeUiApi
@Composable
fun DetailImagePagePhotoView(url: String, title: String, onBack: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(factory = { ctx ->
            PhotoView(ctx).apply {
                Glide.with(ctx)
                    .asBitmap()
                    .load(url)
                    .fitCenter()
                    .placeholder(R.drawable.glide_placeholder)
                    .into(this)
            }
        }, modifier = Modifier.fillMaxSize())

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(color = BlackAlpha10)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.common_ic_back),
                contentDescription = "icon",
                modifier = Modifier
                    .width(52.dp)
                    .height(52.dp)
                    .clickable(onClick = onBack)
                    .padding(0.dp)
                    .padding(10.dp),
                tint = Color.White
            )
            Text(text = title, style = MaterialTheme.typography.h5.copy(color = Color.White), modifier = Modifier.padding(start = 10.dp))
        }

    }
}


@ExperimentalComposeUiApi
@Composable
fun DetailImagePage(url: String) {
    // 图片查看器
    // 1. 最小放大倍数 1f
    // 2. 只有图片大于屏幕时才能滑动

    // 屏幕宽高
    var screenSize: IntSize = IntSize.Zero
    var imageSize: IntSize = IntSize.Zero
    var curImageSize: IntSize = IntSize.Zero
    var screenX = 0f
    var screenY = 0f

    var scale = remember { mutableStateOf(1f) }
    var offset = remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        val curScale = scale.value * zoomChange
        if (curScale < 1f)
            scale.value = 1f
        else
            scale.value *= zoomChange
        // offset.value += offsetChange
    }

    var o = remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.gray50)
            .onSizeChanged { screenSize = it },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberImagePainter(
                data = url,
                builder = {
                    crossfade(true)
                    placeholder(R.drawable.glide_placeholder)
                },
            ),
            contentDescription = "detail image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    imageSize = IntSize(it.width, it.height)
                }
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    curImageSize = IntSize(
                        (imageSize.width * scaleX).toInt(),
                        (imageSize.height * scaleY).toInt()
                    )
                }
                .offset { IntOffset(offset.value.x.toInt(), offset.value.y.toInt()) }
                .transformable(state = state)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        // change.consumeAllChanges()
                        if (curImageSize.width == 0 || curImageSize.height == 0) {
                            curImageSize = imageSize
                        }
                        if (curImageSize.width == 0 || curImageSize.height == 0)
                            return@detectDragGestures

                        val leftXInImage = change.position.x
                        val rightXInImage = curImageSize.width - change.position.x
                        val topYInImage = change.position.y
                        val bottomYInImage = curImageSize.height - change.position.y
                        val leftXInScreen = screenX
                        val rightXInScreen = screenSize.width - screenX
                        val topYInScreen = screenY
                        val bottomYInScreen = screenSize.height - screenY
                        var x = offset.value.x
                        var y = offset.value.y
                        if (leftXInImage >= leftXInScreen && rightXInImage >= rightXInScreen) {
                            x = offset.value.x + dragAmount.x
                        }
                        if (topYInImage >= topYInScreen && bottomYInImage >= bottomYInScreen) {
                            y = offset.value.y + dragAmount.y
                        }
                        if (x == offset.value.x && y == offset.value.y)
                            return@detectDragGestures
                        offset.value = Offset(x, y)
                    }
                }
                .pointerInteropFilter {
                    screenX = it.rawX
                    screenY = it.rawY
                    /*Log.d("wangjie","x: ${it.x}, y: ${it.y}, rawX: ${it.rawX}, rawY: ${it.rawY}")
                    when (it.action) {
                        MotionEvent.ACTION_UP -> {
                        }
                        MotionEvent.ACTION_DOWN -> {
                            Log.d("wangjie","ACTION_DOWN")
                        }
                        else -> {

                        }
                    }*/
                    true
                }
        )

        // Icon(painter = , contentDescription = )
    }
}