package com.example.composehoo.ui.common.view.refresh

sealed class LoadState(msg: String = "") {
    class Refreshing : LoadState()
    class RefreshingError(str: String) : LoadState()
    class Loading : LoadState()
    class NoneState : LoadState()
    class Complete : LoadState()
    class Empty: LoadState()
    class Error(str: String) : LoadState()
}