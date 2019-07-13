package com.joe.jetpackdemo.ui.activity

import android.animation.Animator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.common.BaseConstant
import com.joe.jetpackdemo.databinding.DetailActivityBinding
import com.joe.jetpackdemo.utils.AppPrefsUtils
import com.joe.jetpackdemo.viewmodel.CustomViewModelProvider
import com.joe.jetpackdemo.viewmodel.DetailModel

class DetailActivity : AppCompatActivity() {

    private val detailModel: DetailModel by viewModels<DetailModel> {
        CustomViewModelProvider.providerDetailModel(
            this
            , intent.getLongExtra(BaseConstant.DETAIL_SHOE_ID, 1L)
            , AppPrefsUtils.getLong(BaseConstant.SP_USER_ID)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.detail_activity)

        val binding = DataBindingUtil.setContentView<DetailActivityBinding>(this, R.layout.detail_activity)
        onSubscribeUi(binding)
        initListener(binding)
    }

    private fun initListener(binding: DetailActivityBinding) {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        // 设置点击动画
        binding.fbFavourite.setOnClickListener {
            binding.fbFavourite.animate()
                .rotation(360.0f)
                .scaleX(0.0f)
                .scaleY(0.0f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        detailModel.favourite()
                    }

                }).setDuration(200)
                .start()
        }
    }

    private fun onSubscribeUi(binding: DetailActivityBinding) {
        detailModel.shoe.observe(this, Observer {
            binding.shoe = it
            binding.price = it.price.toString()
        })

        detailModel.favouriteShoe.observe(this, Observer {
            binding.v = if (it == null) View.VISIBLE else View.GONE
        })
    }
}
