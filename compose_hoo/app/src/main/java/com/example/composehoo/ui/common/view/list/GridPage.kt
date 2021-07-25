package com.example.composehoo.ui.common.view.list

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.example.composehoo.db.provider.BaseProvider
import com.example.composehoo.ui.common.view.refresh.LoadState
import com.example.composehoo.ui.common.view.refresh.LoadStateView
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun <T> GridPage(
    provider: BaseProvider<T>,
    gridNum: Int = 2,
    space: Dp = 0.dp,
    modifier: Modifier = Modifier,
    headerCount: Int = 0,
    header: @Composable (Int) -> Unit = {},
    footerCount: Int = 0,
    footer: @Composable (Int) -> Unit = {},
    dataItem: @Composable RowScope.(Int, T, Dp) -> Unit
) {
    val refreshState = rememberSwipeRefreshState(isRefreshing = false)
    val lifecycleOwner = LocalLifecycleOwner.current
    // 数据监听
    val currentDataList = remember { mutableStateListOf<T>() }
    DisposableEffect(key1 = provider.sourceLiveData, lifecycleOwner) {
        val observer = Observer<MutableList<T>> { vs ->
            if(refreshState.isRefreshing){
                refreshState.isRefreshing = false
            }
            currentDataList.addAll(vs)
        }
        provider.sourceLiveData.observe(lifecycleOwner, observer)
        onDispose { provider.sourceLiveData.removeObserver(observer) }
    }
    // 刷新状态
    val loadingState = provider.stateLiveData.observeAsState(LoadState.NoneState())
    val lazyListState = rememberLazyListState()

    // 刷新数据
    val scope = rememberCoroutineScope()
    var job: Job? = null
    DisposableEffect(key1 = scope, lifecycleOwner) {
        job = scope.launch {
            provider.refresh()
        }
        onDispose {
            job?.cancel()
        }
    }
    val size = remember {
        mutableStateOf<IntSize>(IntSize(0, 0))
    }
    SwipeRefresh(
        state = refreshState,
        onRefresh = {
            currentDataList.clear()
            job?.cancel()
            job = scope.launch {
                refreshState.isRefreshing = true
                provider.refresh()
            }
        },
        modifier = modifier
    )
    {
        var len = currentDataList.size / gridNum
        if (currentDataList.size % gridNum != 0)
            len++
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged {
                    size.value = it
                },
            state = lazyListState
        ) {
            for (i in 0 until headerCount) {
                item {
                    header(i)
                }
            }
            items(count = len) { rowIndex ->
                val state = rememberSaveable(rowIndex){
                    mutableStateOf(rowIndex)
                }
                val contentWith =
                    (with(LocalDensity.current) { (size.value.width).toDp() - space * (gridNum - 1) }) / gridNum
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .padding(top = if (state.value == 0) 10.dp else 0.dp, bottom = 8.dp)
                ) {
                    for (index in 0 until gridNum) {
                        val pos = state.value * gridNum + index
                        val data = currentDataList.getOrNull(pos)
                        if (data != null) {
                            dataItem(pos, data, contentWith)
                            if (index != gridNum - 1 && currentDataList.getOrNull(pos + 1) != null)
                                Spacer(modifier = Modifier.width(space))
                            if (pos == currentDataList.size - 1) {
                                Log.d("wangjie", "set Loading Statee")
                                provider.setState(LoadState.Loading())
                            }
                        }
                    }
                }
            }
            for (i in 0 until footerCount) {
                item {
                    footer(i)
                }
            }
            item {
                LoadStateView(loadingState.value, loadMore = {
                    job?.cancel()
                    job = scope.launch {
                        provider.loadMore()
                    }
                }, refreshRetry = {
                    job?.cancel()
                    job = scope.launch {
                        provider.refresh()
                    }
                }, loadRetry = {
                    job?.cancel()
                    job = scope.launch {
                        provider.loadMore()
                    }
                })
            }
        }
    }
}