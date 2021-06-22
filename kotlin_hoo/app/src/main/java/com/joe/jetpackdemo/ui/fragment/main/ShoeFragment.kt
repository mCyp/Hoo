package com.joe.jetpackdemo.ui.fragment.main


import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.Explode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.joe.jetpackdemo.common.listener.SimpleAnimation
import com.joe.jetpackdemo.databinding.ShoeFragmentBinding
import com.joe.jetpackdemo.ui.adapter.ShoeAdapter
import com.joe.jetpackdemo.utils.UiUtils
import com.joe.jetpackdemo.viewmodel.CustomViewModelProvider
import com.joe.jetpackdemo.viewmodel.ShoeModel
import com.joe.jetpackdemo.widget.smartrefresh.DropBoxHeader
import com.scwang.smart.refresh.footer.ClassicsFooter
import kotlinx.android.synthetic.main.shoe_fragment.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 鞋子页面
 */
class ShoeFragment : Fragment() {

    private val TAG: String by lazy {
        ShoeFragment::class.java.simpleName
    }

    private lateinit var mShoe: FloatingActionButton
    private lateinit var mNike: FloatingActionButton
    private lateinit var mAdi: FloatingActionButton
    private lateinit var mOther: FloatingActionButton
    private lateinit var nikeGroup: Group
    private lateinit var adiGroup: Group
    private lateinit var otherGroup: Group

    // 动画集合，用来控制动画的有序播放
    private var animatorSet: AnimatorSet? = null

    // 圆的半径
    private var radius: Int = 0

    // FloatingActionButton宽度和高度，宽高一样
    private var width: Int = 0

    private var currentStates: LoadStates? = null


    // by viewModels 需要依赖 "androidx.navigation:navigation-ui-ktx:$rootProject.navigationVersion"
    private val viewModel: ShoeModel by viewModels {
        CustomViewModelProvider.providerShoeModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ShoeFragmentBinding = ShoeFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        onSubscribeUi(binding)
        return binding.root
    }

    private fun stateToStr(state: LoadState): String {
        return when (state) {
            is LoadState.NotLoading -> "NotLoading"
            is LoadState.Loading -> "Loading"
            else -> "Error"
        }
    }

    /**
     * 鞋子数据更新的通知
     */
    private fun onSubscribeUi(binding: ShoeFragmentBinding) {
        binding.lifecycleOwner = this


        // 初始化RecyclerView部分
        val adapter = ShoeAdapter(context!!)
        // 数据加载状态的回调
        adapter.addLoadStateListener { state: CombinedLoadStates ->
            currentStates = state.source
            // 如果append没有处于加载状态，但是refreshLayout出于加载状态，refreshLayout停止加载状态
            if (state.append is LoadState.NotLoading && binding.refreshLayout.isLoading) {
                refreshLayout.finishLoadMore()
            }
            // 如果refresh没有出于加载状态，但是refreshLayout出于刷新状态，refreshLayout停止刷新
            if (state.source.refresh is LoadState.NotLoading && binding.refreshLayout.isRefreshing) {
                refreshLayout.finishRefresh()
            }
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastPos = getLastVisiblePosition(binding.recyclerView)
                if (!(lastPos == adapter.itemCount - 1 && currentStates?.append is LoadState.Loading)) {
                    binding.refreshLayout.finishLoadMore()
                }
            }
        })
        lifecycleScope.launch {
            // 注意：这里需要用 collectLatest，只用 collect 的话筛选会不生效
            viewModel.shoes.collectLatest {
                adapter.submitData(it)
            }
        }
        binding.refreshLayout.setRefreshHeader(DropBoxHeader(context))
        binding.refreshLayout.setRefreshFooter(ClassicsFooter(context))
        binding.refreshLayout.setOnLoadMoreListener {
            // 如果当前数据已经全部加载完，就不再加载
            if (currentStates?.append?.endOfPaginationReached == true)
                binding.refreshLayout.finishLoadMoreWithNoMoreData()
        }

        mShoe = binding.fabShoe
        mNike = binding.fabNike
        mAdi = binding.fabAdidas
        mOther = binding.fabOther
        nikeGroup = binding.gpLike
        adiGroup = binding.gpWrite
        otherGroup = binding.gpTop

        mNike.setOnClickListener {
            viewModel.setBrand(ShoeModel.NIKE)
            shoeAnimation()
        }

        mAdi.setOnClickListener {
            viewModel.setBrand(ShoeModel.ADIDAS)
            shoeAnimation()
        }

        mOther.setOnClickListener {
            viewModel.setBrand(ShoeModel.OTHER)
            shoeAnimation()
        }

        initListener()
        setViewVisible(false)
    }

    fun getFirstVisiblePosition(recyclerView: RecyclerView): Int {
        val layoutManager: RecyclerView.LayoutManager? = recyclerView.layoutManager
        return if (layoutManager is LinearLayoutManager) {
            layoutManager.findFirstVisibleItemPosition()
        } else -1
    }

    fun getLastVisiblePosition(recyclerView: RecyclerView): Int {
        val layoutManager: RecyclerView.LayoutManager? = recyclerView.layoutManager
        return if (layoutManager is LinearLayoutManager) {
            layoutManager.findLastVisibleItemPosition()
        } else -1
    }

    override fun onResume() {
        super.onResume()

        mShoe.post {
            width = mShoe.measuredWidth
        }
        radius = UiUtils.dp2px(context!!, 80f)
    }

    /**
     * 初始化监听器
     */
    private fun initListener() {
        mShoe.setOnClickListener {
            shoeAnimation()
        }
    }

    private fun shoeAnimation() {
        // 播放动画的时候不可以点击
        if (animatorSet != null && animatorSet!!.isRunning)
            return

        // 判断播放显示还是隐藏动画
        if (nikeGroup.visibility != View.VISIBLE) {
            animatorSet = AnimatorSet()
            val likeAnimator = getValueAnimator(mNike, false, nikeGroup, 0)
            val writeAnimator = getValueAnimator(mAdi, false, adiGroup, 45)
            val topAnimator = getValueAnimator(mOther, false, otherGroup, 90)
            animatorSet!!.playSequentially(likeAnimator, writeAnimator, topAnimator)
            animatorSet!!.start()
        } else {
            animatorSet = AnimatorSet()
            val likeAnimator = getValueAnimator(mNike, true, nikeGroup, 0)
            val writeAnimator = getValueAnimator(mAdi, true, adiGroup, 45)
            val topAnimator = getValueAnimator(mOther, true, otherGroup, 90)
            animatorSet!!.playSequentially(topAnimator, writeAnimator, likeAnimator)
            animatorSet!!.start()
        }
    }

    private fun setViewVisible(isShow: Boolean) {
        nikeGroup.visibility = if (isShow) View.VISIBLE else View.GONE
        adiGroup.visibility = if (isShow) View.VISIBLE else View.GONE
        otherGroup.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun getValueAnimator(
        button: FloatingActionButton,
        reverse: Boolean,
        group: Group,
        angle: Int
    ): ValueAnimator {
        val animator: ValueAnimator
        if (reverse)
            animator = ValueAnimator.ofFloat(1f, 0f)
        else
            animator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener { animation ->
            val v = animation.animatedValue as Float
            val params = button.layoutParams as ConstraintLayout.LayoutParams
            params.circleRadius = (radius.toFloat() * v).toInt()
            params.circleAngle = 270f + angle * v
            params.width = (width.toFloat() * v).toInt()
            params.height = (width.toFloat() * v).toInt()
            button.layoutParams = params

            if (group == nikeGroup) {
                Log.e(
                    TAG,
                    "cirRadius:${params.circleRadius},angle:${params.circleAngle},width:${params.width}"
                )
            }
        }
        animator.addListener(object : SimpleAnimation() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)

                group.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)

                if (group === nikeGroup && reverse) {
                    setViewVisible(false)
                }
            }
        })

        animator.duration = 300
        animator.interpolator = DecelerateInterpolator() as TimeInterpolator?
        return animator
    }
}
