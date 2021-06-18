package com.joe.jetpackdemo.widget

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import android.widget.ImageView
import com.github.chrisbanes.photoview.PhotoViewAttacher

class CustomAttacher(imageView: ImageView): PhotoViewAttacher(imageView) {

    private var listener: OnViewFingerUpListener? = null

    override fun setScaleType(scaleType: ImageView.ScaleType) {
        if (scaleType != getScaleType()) {
            val type = PhotoViewAttacher::class.java.getDeclaredField("mScaleType")
            type.isAccessible = true
            type.set(this, scaleType)
            update()
        }
    }



    override fun onTouch(v: View?, ev: MotionEvent?): Boolean {
        val result = super.onTouch(v, ev)
        if(ev?.action == MotionEvent.ACTION_UP){
            if(scale <= 1.0f){
                listener?.let {
                    val parent = v?.parent
                    parent?.let {p->
                        p.requestDisallowInterceptTouchEvent(false)
                    }
                    it.onViewFingerUp()
                }
            }
        }
        return result
    }

    fun setOnViewFingerUpListener(listener: OnViewFingerUpListener){
        this.listener = listener
    }

}

interface OnViewFingerUpListener{
    fun onViewFingerUp()
}