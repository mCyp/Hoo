package com.example.composehoo.ui.page.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import com.example.composehoo.R
import com.example.composehoo.base.BaseConstant
import com.example.composehoo.db.RepositoryProvider
import com.example.composehoo.ui.common.ext.gray400
import com.example.composehoo.ui.common.ext.grayBackgroundColor
import com.example.composehoo.ui.common.ext.textMainColor
import com.example.composehoo.ui.common.ext.textSecondColor
import com.example.composehoo.ui.theme.*
import com.example.composehoo.ui.viewmodel.main.MeModel
import com.example.composehoo.utils.AppPrefsUtils

@Composable
fun MePage(followSysDark: (Boolean) -> Unit) {
    val userId = AppPrefsUtils.getLong(BaseConstant.USER_ID)
    val context = LocalContext.current
    val userRepository = RepositoryProvider.providerUserRepository(context = context)
    val meModel: MeModel =
        com.example.composehoo.ui.common.ext.viewModel { MeModel(userRepository, userId) }
    val user = meModel.user.observeAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.grayBackgroundColor)
    ) {
        val (portrait, topLay, tvName, tvAccount, bottomListCard) = createRefs()
        val guideLineFourPer = createGuidelineFromTop(0.3f)
        val guideLine16DP = createGuidelineFromStart(16.dp)
        Box(modifier = Modifier
            .constrainAs(topLay) {
                linkTo(top = parent.top, bottom = guideLineFourPer)
                linkTo(start = parent.start, end = parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            .background(color = MaterialTheme.colors.primary)) {
        }
        Image(
            painter = rememberImagePainter(
                data = R.drawable.default_header, builder = {
                    crossfade(true)
                    placeholder(R.drawable.glide_placeholder)
                }),
            contentDescription = "头像",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(portrait) {
                    start.linkTo(guideLine16DP)
                    linkTo(top = guideLineFourPer, bottom = guideLineFourPer)
                }
                .height(100.dp)
                .width(100.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Text(
            text = user.value?.name ?: "",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .constrainAs(tvName) {
                    bottom.linkTo(guideLineFourPer)
                    start.linkTo(portrait.absoluteRight)
                }
                .padding(start = 10.dp)
        )
        Text(
            text = user.value?.account ?: "",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.gray400,
            modifier = Modifier
                .constrainAs(tvAccount) {
                    top.linkTo(guideLineFourPer)
                    start.linkTo(portrait.absoluteRight)
                }
                .padding(start = 10.dp)
        )
        FunListCard(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomListCard) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(portrait.bottom)
                }
                .padding(16.dp)
                .background(color = MaterialTheme.colors.background)
                .clip(RoundedCornerShape(4.dp)),
            dataClick = {

            },
            followSysDark = followSysDark
        )
    }
}

@Composable
fun FunListCard(modifier: Modifier, dataClick: () -> Unit = {}, followSysDark: (Boolean) -> Unit) {
    val followSystem = remember { mutableStateOf(false) }
    followSystem.value = AppPrefsUtils.getBoolean(BaseConstant.USE_SYSTEM_DARK, false)
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 16.dp)
                .clickable { dataClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.common_ic_data),
                contentDescription = "icon",
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp),
                tint = MaterialTheme.colors.textMainColor
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.me_data_store),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.textMainColor,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = R.drawable.common_ic_arrow_right),
                contentDescription = "back",
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp),
                tint = MaterialTheme.colors.textSecondColor
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 16.dp)
                .clickable {  },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.common_ic_sunny),
                contentDescription = "icon",
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp),
                tint = MaterialTheme.colors.textMainColor
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.me_follow_dark),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.textMainColor,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = followSystem.value,
                onCheckedChange = {
                    followSystem.value = it
                    followSysDark(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TextMainWhiteColorDark,
                    uncheckedThumbColor = TextMainWhiteColor
                )
            )
        }
    }
}

