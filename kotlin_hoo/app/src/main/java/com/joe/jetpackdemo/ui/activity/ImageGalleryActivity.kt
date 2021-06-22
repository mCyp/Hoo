package com.joe.jetpackdemo.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.OnViewDragListener
import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.ui.CusChangeOnlineImageTransition

const val GALLERY_URL = "GALLERY_URL"
const val TRANSITION_NAME = "TRANSITION_NAME"
class ImageGalleryActivity : AppCompatActivity() {

    private lateinit var mPhotoView: com.joe.jetpackdemo.widget.PhotoView
    private lateinit var mContainer: ConstraintLayout
    private var alpha: Float = 1f
    private var intAlpha: Int = 255
    private var url: String? = null

    companion object{
        fun start(context: Context, url: String, bundle: Bundle, tName: String){
            val intent = Intent(context, ImageGalleryActivity::class.java)
            intent.putExtra(GALLERY_URL, url)
            intent.putExtra(TRANSITION_NAME, tName)
            context.startActivity(intent, bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        postponeEnterTransition()
        val transitionSet = TransitionSet()
        transitionSet.addTransition(ChangeBounds())
        transitionSet.addTransition(ChangeImageTransform())
        transitionSet.addTransition(CusChangeOnlineImageTransition())

        /*setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(R.id.photoView)
            duration = 300L
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(R.id.photoView)
            duration = 250L
        }*/

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_gallery)

        initView()
        transitionSet.addTarget(mPhotoView)
        window.sharedElementEnterTransition = transitionSet
        window.sharedElementExitTransition = transitionSet

        initData()

    }

    override fun onResume() {
        super.onResume()

        startPostponedEnterTransition()
    }



    
    private fun initView(){
        mPhotoView = findViewById(R.id.photoView)
        mContainer = findViewById(R.id.container)

        mPhotoView.setExitListener(object : com.joe.jetpackdemo.widget.PhotoView.OnExitListener {
            override fun exit() {
                // mPhotoView.scrollBy(0, -mPhotoView.scrollY)
                this@ImageGalleryActivity.onBackPressed()
            }

            override fun exitBefore() {

            }
        })

        mPhotoView.setOnViewDragListener(object : OnViewDragListener {

            override fun onDrag(dx: Float, dy: Float) {
                var rectf = mPhotoView.displayRect

                if(mPhotoView.scale > 1)
                    return

                mPhotoView.scrollBy(0, (-dy).toInt())
                alpha -= dy * 0.001f
                intAlpha -= (dy * 0.5).toInt()

                if (alpha > 1)
                    alpha = 1f
                else if (alpha < 0) {
                    alpha = 0f
                }

                if (intAlpha > 255) {
                    intAlpha = 255
                } else if (intAlpha < 0) {
                    intAlpha = 0
                }

                mContainer.background.mutate().alpha = intAlpha
                /*if( alpha >= 0.6){
                    mPhotoView.scale = alpha
                }*/
            }
        })
    }

    private fun initData(){
        intent.getStringExtra(TRANSITION_NAME)?.let {
            mPhotoView.transitionName = it
        }
        intent.getStringExtra(GALLERY_URL)?.let {
            Glide.with(this)
                .asBitmap()
                .load(it)
                .placeholder(R.drawable.glide_placeholder)
                .centerInside()
                .into(mPhotoView)
        }
    }

}