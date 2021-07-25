package com.example.composehoo.ui.common.ext

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

@Composable
inline fun <reified VM : ViewModel> viewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    noinline factory: () -> VM
): VM = viewModelStoreOwner.vm(factory = factory)

inline fun <reified VM : ViewModel> ViewModelStoreOwner.vm(
    noinline factory: () -> VM
): VM {
    val provider = ViewModelProvider(this, TypeSafeViewModelFactory(factory))
    return provider.get()
}

inline fun <reified VM: ViewModel> AppCompatActivity.viewModel(
    noinline factory: () -> VM
): Lazy<VM> = viewModels { TypeSafeViewModelFactory(factory) }

inline fun <reified VM: ViewModel> Fragment.viewModel(
    noinline factory: () -> VM
): Lazy<VM> = viewModels { TypeSafeViewModelFactory(factory) }

@Suppress("UNCHECKED_CAST")
class TypeSafeViewModelFactory<VM: ViewModel>(
    private val factory: () -> VM
): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T  = factory() as T
}