package com.example.composehoo.ui.page.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.example.composehoo.R
import com.example.composehoo.db.RepositoryProvider
import com.example.composehoo.db.data.Shoe
import com.example.composehoo.db.provider.ShoeProvider
import com.example.composehoo.ui.common.view.list.GridPage
import com.example.composehoo.ui.theme.BgGrayColor
import com.example.composehoo.ui.theme.BgGrayColorDark
import com.example.composehoo.ui.viewmodel.main.ShoeModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.*


@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun ShoePage() {
    val context = LocalContext.current
    val shoeProvider = ShoeProvider(RepositoryProvider.providerShoeRepository(context))
    GridPage(
        provider = shoeProvider,
        gridNum = 2,
        space = 10.dp,
        modifier = Modifier
            .fillMaxSize()
            .background(color = if (MaterialTheme.colors.isLight) BgGrayColor else BgGrayColorDark)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        headerCount = 2,
        header = { pos ->
            if (pos == 0) {
                Banner()
            } else {
                HomeContentTitle()
            }
        }
    ) { pos, shoe, width ->
        ShoePageItem(shoe = shoe, widthDp = width, heightDp = width)
    }
}

@Composable
fun HomeContentTitle() {
    Box(
        modifier = Modifier
            .height(36.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "Sneakers",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.secondary,
            textAlign = TextAlign.Start
        )
    }

}

@ExperimentalPagerApi
@Composable
fun Banner() {
    val cornerSize = with(LocalDensity.current) { 8.dp.toPx() }
    val shoeBannerList = listOf<String>(
        "https://raw.githubusercontent.com/mCyp/Photo/master/hoo/bannerbanner_one.jpg",
        "https://raw.githubusercontent.com/mCyp/Photo/master/hoo/bannerbanner_two.jpg",
        "https://raw.githubusercontent.com/mCyp/Photo/master/hoo/bannerbanner_three.jpg",
        "https://raw.githubusercontent.com/mCyp/Photo/master/hoo/bannerbanner_four.jpg"
    )
    // viewPager参数
    val viewPagerState = rememberPagerState(pageCount = 4, infiniteLoop = true)
    val scope = rememberCoroutineScope()
    var job: Job? = null
    if(shoeBannerList.size > 1) {
        DisposableEffect(key1 = scope) {
            job = scope.launch(Dispatchers.IO) {
                while (true) {
                    delay(5000)
                    withContext(Dispatchers.Main) {
                        if (viewPagerState.currentPage < viewPagerState.pageCount - 1) {
                            viewPagerState.animateScrollToPage(viewPagerState.currentPage+1)
                        }else {
                            viewPagerState.animateScrollToPage(0)
                        }
                    }
                }
            }
            onDispose {
                job?.cancel()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        HorizontalPager(
            state = viewPagerState,
            dragEnabled = true,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Image(
                painter = rememberImagePainter(
                    data = shoeBannerList[page],
                    builder = {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(cornerSize))
                        placeholder(R.drawable.glide_placeholder)
                    }
                ),
                contentDescription = "Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@ExperimentalPagerApi
@Preview
@Composable
fun previewBanner() {
    Banner()
}


suspend fun refresh(shoeModel: ShoeModel, success: (List<Shoe>) -> Unit, failed: (String) -> Unit) {
    try {
        val list = shoeModel.refresh()
        if (list == null) {
            success(emptyList())
        } else {
            success(list)
        }
    } catch (e: Exception) {
        failed(e.message ?: "发生错误了！")
    }
}