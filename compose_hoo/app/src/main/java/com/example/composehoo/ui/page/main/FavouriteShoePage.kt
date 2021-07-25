package com.example.composehoo.ui.page.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.example.composehoo.R
import com.example.composehoo.base.BaseConstant
import com.example.composehoo.db.RepositoryProvider
import com.example.composehoo.db.data.Shoe
import com.example.composehoo.db.paging.FavPagingSource
import com.example.composehoo.ui.activity.DetailActivity
import com.example.composehoo.ui.common.view.refresh.LoadingComplete
import com.example.composehoo.ui.common.view.refresh.LoadingMore
import com.example.composehoo.ui.theme.*
import com.example.composehoo.utils.AppPrefsUtils
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun FavouriteShoePage() {
    val context = LocalContext.current
    val shoeRepo = RepositoryProvider.providerShoeRepository(context)
    val userId = AppPrefsUtils.getLong(BaseConstant.USER_ID)
    // Paging 相关
    val page = remember {
        Pager(PagingConfig(pageSize = BaseConstant.PAGE_SIZE), initialKey = 0) {
            FavPagingSource(shoeRepo, userId)
        }
    }
    val lazyPagingItems = page.flow.collectAsLazyPagingItems()
    // Refresh相关
    val refreshState =
        rememberSwipeRefreshState(isRefreshing = false)
    SwipeRefresh(
        state = refreshState,
        onRefresh = {
            lazyPagingItems.refresh()
        },
        modifier = Modifier
            .fillMaxSize()
            .background(color = if (MaterialTheme.colors.isLight) BgGrayColor else BgGrayColorDark)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(lazyPagingItems) { index, item ->
                Log.d("wangjie", index.toString())
                item?.let {
                    FavouriteShoePageItem(shoe = it)
                }
                if (index != lazyPagingItems.itemCount - 1) {
                    Spacer(
                        modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth()
                    )
                }
            }
            if (lazyPagingItems.loadState.append == LoadState.Loading) {
                item {
                    LoadingMore()
                }
            } else if (lazyPagingItems.loadState.append.endOfPaginationReached) {
                item {
                    LoadingComplete()
                }
            }
        }

    }
}

@Composable
fun FavouriteShoePageItem(shoe: Shoe) {
    val context = LocalContext.current
    val cornerSize = with(LocalDensity.current) { 2.dp.toPx() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                DetailActivity.startActivity(context, shoe.id)
            }
    ) {
        Image(
            painter = rememberImagePainter(data = shoe.imageUrl, builder = {
                crossfade(true)
                placeholder(R.drawable.glide_placeholder)
                transformations(RoundedCornersTransformation(cornerSize))
            }),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(2.dp)),
            contentDescription = "content",
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier
            .height(40.dp)
            .align(Alignment.TopStart)) {
            Text(
                text = shoe.name,
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.Center)
                    .padding(start = 14.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "¥${shoe.price}",
            modifier = Modifier
                .wrapContentHeight()
                .padding(start = 10.dp, top = 40.dp)
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(2.dp),
                    color = if (MaterialTheme.colors.isLight) GRAY50 else GRAY400DARK
                )
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .align(Alignment.TopStart),
            style = MaterialTheme.typography.caption
        )
        Icon(
            painter = painterResource(id = R.drawable.detail_ic_favorite_white),
            contentDescription = "icon",
            modifier = Modifier
                .height(24.dp)
                .width(24.dp)
                .padding(end = 10.dp, bottom = 10.dp)
                .align(Alignment.BottomEnd),
            tint = MaterialTheme.colors.primaryVariant
        )
    }
}