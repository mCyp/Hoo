package com.joe.jetpackdemo.widget

import androidx.annotation.StringRes
import android.content.Context
import com.joe.jetpackdemo.widget.EmptyView
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.View.inflate
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.joe.jetpackdemo.R


/**
 * 来自于Qiujuer
 */
class EmptyView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs), PlaceHolderView {
    private var mEmptyImg: ImageView? = null
    private var mStatusText: TextView? = null
    //private Loading mLoading;
    private lateinit var mLottieAnimationView: LottieAnimationView


    private val mDrawableIds = intArrayOf(0, 0)
    private val mTextIds = intArrayOf(0, 0, 0)

    private lateinit var mBindViews: Array<View>

    init {
        inflate(getContext(), R.layout.lay_empty, this)
        mEmptyImg = findViewById(R.id.im_empty) as ImageView
        mStatusText = findViewById(R.id.txt_empty) as TextView
        //mLoading = (Loading) findViewById(R.id.loading);
        mLottieAnimationView = findViewById(R.id.loading)

        // Load attributes
        val a = getContext().obtainStyledAttributes(
            attrs, R.styleable.EmptyView, 0, 0
        )

        mDrawableIds[0] = a.getInt(R.styleable.EmptyView_comEmptyDrawable, R.drawable.status_empty)
        mDrawableIds[1] = a.getInt(R.styleable.EmptyView_comErrorDrawable, R.drawable.status_empty)
        mTextIds[0] = a.getInt(R.styleable.EmptyView_comEmptyText, R.string.prompt_empty)
        mTextIds[1] = a.getInt(R.styleable.EmptyView_comErrorText, R.string.prompt_error)
        mTextIds[2] = a.getInt(R.styleable.EmptyView_comLoadingText, R.string.prompt_loading)

        a.recycle()
    }


    /**
     * 绑定一系列数据显示的布局
     * 当前布局隐藏时（有数据时）自动显示绑定的数据布局
     * 而当数据加载时，自动显示Loading，并隐藏数据布局
     *
     * @param views 数据显示的布局
     */
    fun bind(views: Array<View>) {
        this.mBindViews = views
    }

    /**
     * 更改绑定布局的显示状态
     *
     * @param visible 显示的状态
     */
    private fun changeBindViewVisibility(visible: Int) {
        val views = mBindViews
        if (views == null || views.size == 0)
            return

        for (view in views) {
            view.setVisibility(visible)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun triggerEmpty() {
        mLottieAnimationView!!.setVisibility(GONE)
        mLottieAnimationView!!.pauseAnimation()
        mEmptyImg!!.setImageResource(mDrawableIds[0])
        mStatusText!!.setText(mTextIds[0])
        mEmptyImg!!.setVisibility(VISIBLE)
        setVisibility(VISIBLE)
        changeBindViewVisibility(GONE)
    }

    /**
     * {@inheritDoc}
     */
    override fun triggerNetError() {
        mLottieAnimationView!!.setVisibility(GONE)
        mLottieAnimationView!!.pauseAnimation()
        mEmptyImg!!.setImageResource(mDrawableIds[1])
        mStatusText!!.setText(mTextIds[1])
        mEmptyImg!!.setVisibility(VISIBLE)
        setVisibility(VISIBLE)
        changeBindViewVisibility(GONE)
    }

    /**
     * {@inheritDoc}
     */
    override fun triggerError(@StringRes strRes: Int) {
        setVisibility(VISIBLE)
        changeBindViewVisibility(GONE)
    }

    /**
     * {@inheritDoc}
     */
    override fun triggerLoading() {
        mEmptyImg!!.setVisibility(GONE)
        mStatusText!!.setText(mTextIds[2])
        setVisibility(VISIBLE)
        mLottieAnimationView!!.setVisibility(VISIBLE)
        mLottieAnimationView!!.playAnimation()
        changeBindViewVisibility(GONE)
    }

    /**
     * {@inheritDoc}
     */
    override fun triggerOk() {
        setVisibility(GONE)
        changeBindViewVisibility(VISIBLE)
    }

    /**
     * {@inheritDoc}
     */
    override fun triggerOkOrEmpty(isOk: Boolean) {
        if (isOk)
            triggerOk()
        else
            triggerEmpty()
    }
}