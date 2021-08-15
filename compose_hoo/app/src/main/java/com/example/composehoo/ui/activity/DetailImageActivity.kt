package com.example.composehoo.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.toArgb
import com.example.composehoo.base.BaseConstant
import com.example.composehoo.ui.page.detail.DetailImagePage
import com.example.composehoo.ui.page.detail.DetailImagePagePhotoView
import com.example.composehoo.ui.theme.ComposeHooTheme


class DetailImageActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context, url: String, title: String) {
            val intent = Intent(context, DetailImageActivity::class.java)
            intent.putExtra(BaseConstant.IMAGE_URL, url)
            intent.putExtra(BaseConstant.IMAGE_TITLE, title)
            context.startActivity(intent)
        }
    }

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageUrl = intent.getStringExtra(BaseConstant.IMAGE_URL) ?: ""
        val imageTitle = intent.getStringExtra(BaseConstant.IMAGE_TITLE) ?: ""
        setContent {
            ComposeHooTheme {
                window?.statusBarColor = MaterialTheme.colors.onBackground.toArgb()
                DetailImagePagePhotoView(url = imageUrl, imageTitle, onBack = { finish() })
                // DetailImagePage(url = imageUrl)
            }
        }
    }
}