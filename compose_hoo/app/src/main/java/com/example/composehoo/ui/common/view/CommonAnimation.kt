package com.example.composehoo.ui.common.view

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.animation.core.MutableTransitionState

@ExperimentalAnimationApi
@Composable
fun createEnterAnimation(isCurrent: Boolean, content: @Composable () -> Unit){
    AnimatedVisibility(
        visibleState = remember { MutableTransitionState(!isCurrent) }.apply { targetState = isCurrent },
        enter = slideInHorizontally(initialOffsetX = {fullWidth ->  return@slideInHorizontally fullWidth }) + fadeIn(initialAlpha = 0.5f),
        exit = slideOutHorizontally(targetOffsetX = {fullWidth -> -fullWidth }) + fadeOut(targetAlpha = 0.5f),
    ){
        content()
    }
}