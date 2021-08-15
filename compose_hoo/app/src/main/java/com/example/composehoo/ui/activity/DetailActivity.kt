package com.example.composehoo.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import com.example.composehoo.base.BaseConstant
import com.example.composehoo.ui.page.login.DetailPage
import com.example.composehoo.ui.theme.ComposeHooTheme


class DetailActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context, shoeId: Long) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(BaseConstant.SHOE_ID, shoeId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shoeId = intent.getLongExtra(BaseConstant.SHOE_ID, 0L)
        setContent {
            ComposeHooTheme() {
                window?.statusBarColor = MaterialTheme.colors.onBackground.toArgb()
                DetailPage(
                    shoeId = shoeId,
                    onBack = {
                        onBackPressed()
                    },
                    onImageClick = {
                        DetailImageActivity.startActivity(this@DetailActivity, it, "图片")
                    }
                )
            }
        }
    }
}