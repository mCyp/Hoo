package com.example.composehoo.ui.page.main

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.example.composehoo.R
import com.example.composehoo.base.BaseConstant
import com.example.composehoo.db.data.Shoe
import com.example.composehoo.ui.activity.DetailActivity
import com.example.composehoo.ui.theme.*

@Composable
fun ShoePageItem(shoe: Shoe, widthDp: Dp, heightDp: Dp) {
    val url = shoe.imageUrl
    val cornerSize = with(LocalDensity.current) { 4.dp.toPx() }
    val heightPx = with(LocalDensity.current) { widthDp.toPx() }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .width(widthDp)
            .height(heightDp)
            .clickable {
                DetailActivity.startActivity(context, shoe.id)
            }
            .clip(RoundedCornerShape(4.dp))
    ) {
        Image(
            painter = rememberImagePainter(
                data = url,
                builder = {
                    crossfade(true)
                    transformations(RoundedCornersTransformation(cornerSize))
                    placeholder(R.drawable.glide_placeholder)
                }
            ),
            contentDescription = "Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = BlackAlpha10)
        ) {}
        Text(
            text = "¥${shoe.id}",
            style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onBackground),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Brush.verticalGradient(listOf(BlackAlpha32, Color.Transparent)))
                .padding(0.dp)
                .padding(vertical = 4.dp)
        )

    }


}