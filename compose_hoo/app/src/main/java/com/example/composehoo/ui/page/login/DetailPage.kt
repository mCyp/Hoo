package com.example.composehoo.ui.page.login

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.imageLoader
import coil.size.Precision
import com.airbnb.lottie.compose.*
import com.example.composehoo.R
import com.example.composehoo.base.BaseConstant
import com.example.composehoo.db.RepositoryProvider
import com.example.composehoo.ui.common.ext.viewModel
import com.example.composehoo.ui.common.view.HooLoadingView
import com.example.composehoo.ui.theme.*
import com.example.composehoo.ui.viewmodel.detail.DetailModel
import com.example.composehoo.utils.AppPrefsUtils
import kotlinx.coroutines.Job

@Composable
fun DetailPage(shoeId: Long, onBack: () -> Unit) {
    val context = LocalContext.current
    val shoeRepo = RepositoryProvider.providerShoeRepository(context)
    val favRepo = RepositoryProvider.providerFavouriteShoeRepository(context)
    val detailModel = viewModel { DetailModel(shoeRepo, favRepo) }
    var shoe = detailModel.queryUser(shoeId).observeAsState()
    val userId = AppPrefsUtils.getLong(BaseConstant.USER_ID)
    var favShoe = detailModel.queryFavourite(shoeId, userId).observeAsState()
    val backgroundColor = BgColorDark
    val isLight = MaterialTheme.colors.isLight
    val isShowLottie = remember { mutableStateOf(false) }

    if (shoe.value != null) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (image, floatingButton, lay, desc, back, lottie) = createRefs()
            val guideLineHalf = createGuidelineFromTop(0.5f)
            Image(
                painter = rememberImagePainter(
                    data = shoe.value!!.imageUrl,
                    builder = {
                        crossfade(true)
                        placeholder(R.drawable.glide_placeholder)
                        precision(Precision.EXACT)
                    }
                ),
                contentDescription = "图片",
                modifier = Modifier
                    .constrainAs(image) {
                        linkTo(start = parent.start, end = parent.end)
                        linkTo(top = parent.top, bottom = guideLineHalf)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .constrainAs(lay) {
                        linkTo(start = parent.start, end = parent.end)
                        top.linkTo(image.bottom)
                        height = Dimension.percent(0.2f)
                        width = Dimension.fillToConstraints
                    }
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(color = backgroundColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = shoe.value!!.name,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                )
                Text(
                    text = shoe.value!!.brand,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                )
                Text(
                    text = "¥${shoe.value!!.price}",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                )
            }
            Text(
                text = shoe.value!!.description,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Start,
                color = if (isLight) GRAY400 else GRAY400DARK,
                modifier = Modifier
                    .background(color = backgroundColor)
                    .padding(horizontal = 16.dp)
                    .constrainAs(desc) {
                        linkTo(start = parent.start, end = parent.end)
                        top.linkTo(lay.bottom)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.percent(0.3f)
                    })
            if (isShowLottie.value) {
                FavAnim(
                    modifier = Modifier.constrainAs(lottie) {
                        bottom.linkTo(floatingButton.top)
                        linkTo(start = floatingButton.start, end = floatingButton.end)
                    }
                )
            }
            FavFloatingIcon(
                favClick = {
                    if (favShoe.value == null) {
                        isShowLottie.value = true
                        detailModel.favShoe(shoeId, userId)
                    } else {
                        isShowLottie.value = false
                        detailModel.disFavShoe(favouriteShoe = favShoe.value!!)
                    }
                },
                modifier = Modifier
                    .constrainAs(floatingButton) {
                        top.linkTo(guideLineHalf)
                        bottom.linkTo(guideLineHalf)
                        end.linkTo(parent.end, margin = 30.dp)
                    }
                    .width(52.dp)
                    .height(52.dp),
                isFollow = favShoe.value == null
            )
            Image(
                painter = painterResource(id = R.drawable.common_ic_back),
                contentDescription = "return",
                modifier = Modifier
                    .constrainAs(back) {
                        start.linkTo(parent.start, margin = 10.dp)
                        top.linkTo(parent.top, margin = 10.dp)
                    }
                    .width(52.dp)
                    .height(52.dp)
                    .clickable(onClick = onBack)
                    .padding(0.dp)
                    .padding(10.dp)
            )

        }
    } else {
        HooLoadingView()
    }
}

@Composable
fun FavFloatingIcon(favClick: () -> Unit, modifier: Modifier, isFollow: Boolean) {
    val degree: Float by animateFloatAsState(
        targetValue = if (isFollow) 360f else 0f,
        animationSpec = tween(durationMillis = 500)
    )
    val color: Color by animateColorAsState(
        targetValue = if (isFollow) MaterialTheme.colors.primaryVariant else if (MaterialTheme.colors.isLight) TextDisabled else TextDisabledDark,
        animationSpec = tween(durationMillis = 500)
    )
    FloatingActionButton(
        onClick = favClick,
        modifier = modifier.rotate(degree),
        backgroundColor = color
    ) {
        Icon(
            painter = painterResource(id = R.drawable.detail_ic_favorite_white),
            contentDescription = "favourite",
            tint = Color.White,
            modifier = Modifier
                .height(24.dp)
                .width(24.dp)
        )
    }
}


@Composable
fun FavAnim(modifier: Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.favour))
    val progress by animateLottieCompositionAsState(
        composition = composition
    )
    LottieAnimation(
        composition = composition, progress = progress,
        modifier
            .width(72.dp)
            .height(171.dp)
    )
}