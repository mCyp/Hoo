package com.joe.jetpackdemo.ui.activity

import android.animation.Animator
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hw.ycshareelement.YcShareElement
import com.hw.ycshareelement.transition.IShareElements
import com.hw.ycshareelement.transition.ShareElementInfo
import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.common.BaseConstant
import com.joe.jetpackdemo.databinding.DetailActivityBinding
import com.joe.jetpackdemo.utils.AppPrefsUtils
import com.joe.jetpackdemo.viewmodel.CustomViewModelProvider
import com.joe.jetpackdemo.viewmodel.DetailModel

/**
 * 展示鞋子细节的界面
 */
class DetailActivity : AppCompatActivity() {

    private val detailModel: DetailModel by viewModels {
        CustomViewModelProvider.providerDetailModel(
            this
            , intent.getLongExtra(BaseConstant.DETAIL_SHOE_ID, 1L)
            , AppPrefsUtils.getLong(BaseConstant.SP_USER_ID)
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        // setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = true
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.detail_activity)

        // val detailModel: DetailModel by viewModels()

        val binding = DataBindingUtil.setContentView<DetailActivityBinding>(this, R.layout.detail_activity)
        binding.model = detailModel
        initListener(binding, detailModel)
    }

    private fun initListener(binding: DetailActivityBinding, detailModel: DetailModel) {
        binding.lifecycleOwner = this

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        val transitionName = "${detailModel.shoe.value?.id}${detailModel.shoe.value?.imageUrl}"
        binding.ivShoe.transitionName = transitionName
        binding.ivShoe.setOnClickListener {
            detailModel.shoe.value?.imageUrl?.let {
                val options = ActivityOptions.makeSceneTransitionAnimation(this, binding.ivShoe, transitionName)
                ImageGalleryActivity.start(this, it, options.toBundle(), transitionName)
            }
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

                })
                .setDuration(200)
                .start()
        }
    }
}
