package com.example.composehoo.db.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composehoo.ui.common.view.refresh.LoadState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

abstract class BaseProvider<T> {
    var currentList = mutableListOf<T>()
    var stateLiveData: MutableLiveData<LoadState> = MutableLiveData(LoadState.NoneState())
    var sourceLiveData = MutableLiveData<MutableList<T>>(currentList)

    fun setState(state: LoadState){
        if(stateLiveData.value != null && stateLiveData.value!!::class == state::class){
            return
        }
        stateLiveData.value = state
    }

    suspend fun refresh(notifyRefresh: Boolean = false) {
        if (notifyRefresh) {
            setState {
                if (stateLiveData.value is LoadState.Refreshing)
                    return@setState
                stateLiveData.value = LoadState.Refreshing()
            }
        }
        try {
            val result = onRefresh()
            if (result) {
                setState {
                    sourceLiveData.value = currentList
                    if (stateLiveData.value is LoadState.NoneState)
                        return@setState
                    stateLiveData.value = LoadState.NoneState()
                }
            } else {
                setState {
                    sourceLiveData.value = currentList
                    if (stateLiveData.value is LoadState.NoneState)
                        return@setState
                    stateLiveData.value = LoadState.Empty()
                }
            }
        } catch (e: Exception) {
            setState {
                stateLiveData.value = LoadState.RefreshingError(e.message ?: "发生了一点小问题～")
            }
        }
    }

    /**
     * true -> 有值返回
     * false -> 返回空
     */
    abstract suspend fun onRefresh(): Boolean

    suspend fun loadMore() {
        setState {
            if (stateLiveData.value is LoadState.Loading)
                return@setState
            stateLiveData.value = LoadState.Loading()
        }
        try {
            val result = onLoadMore()
            if(result){
                setState {
                    sourceLiveData.value = currentList
                    if (stateLiveData.value is LoadState.NoneState)
                        return@setState
                    stateLiveData.value = LoadState.NoneState()
                }
            }else{
                setState {
                    if (stateLiveData.value is LoadState.Complete)
                        return@setState
                    stateLiveData.value = LoadState.Complete()
                }
            }
        } catch (e: Exception) {
            setState {
                stateLiveData.value = LoadState.RefreshingError(e.message ?: "发生了一点小问题～")
            }
        }
    }

    /**
     * true -> 有值返回
     * false -> 返回空
     */
    abstract suspend fun onLoadMore(): Boolean

    private suspend fun setState(block: suspend () -> Unit) {
        withContext(Dispatchers.Main) {
            block()
        }
    }
}