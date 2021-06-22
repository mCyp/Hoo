package com.joe.jetpackdemo.ui.fragment.main


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.transition.Explode
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cn.pedant.SweetAlert.SweetAlertDialog
import com.joe.jetpackdemo.common.BaseConstant.KEY_IMAGE_URI
import com.joe.jetpackdemo.databinding.MeFragmentBinding
import com.joe.jetpackdemo.ui.activity.StoreLocalDataActivity
import com.joe.jetpackdemo.viewmodel.CustomViewModelProvider
import com.joe.jetpackdemo.viewmodel.MeModel


/**
 * 我的界面
 */
class MeFragment : Fragment() {
    private val TAG by lazy { MeFragment::class.java.simpleName }
    // MeModel懒加载
    private val model: MeModel by viewModels {
        CustomViewModelProvider.providerMeModel(requireContext())
    }

    // 选择图片的标识
    private val REQUEST_CODE_IMAGE = 100
    // 加载框
    private val sweetAlertDialog: SweetAlertDialog by lazy {
        SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
            .setTitleText("头像")
            .setContentText("更新中...")
            /*
            .setCancelButton("取消") {
                model.cancelWork()
                sweetAlertDialog.dismiss()
            }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enterTransition = Explode()
        exitTransition = Explode()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Data Binding
        val binding: MeFragmentBinding = MeFragmentBinding.inflate(inflater, container, false)
        initListener(binding)
        onSubscribeUi(binding)
        return binding.root
    }

    /**
     * 初始化监听器
     */
    private fun initListener(binding: MeFragmentBinding) {
        binding.ivHead.setOnClickListener {
            // 选择处理的图片
            val chooseIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(chooseIntent, REQUEST_CODE_IMAGE)
        }

        binding.layCheckData.setOnClickListener {
            val intent = Intent(activity, StoreLocalDataActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Binding绑定
     */
    private fun onSubscribeUi(binding: MeFragmentBinding) {
        binding.lifecycleOwner = this
        binding.model = model

        // 任务状态的观测
        model.outPutWorkInfos.observe(this, Observer {
            if (it.isNullOrEmpty())
                return@Observer

            val state = it[0]
            if (state.state.isFinished) {
                // 更新头像
                val outputImageUri = state.outputData.getString(KEY_IMAGE_URI)
                if (!outputImageUri.isNullOrEmpty()) {
                    model.setOutputUri(outputImageUri)
                }
                sweetAlertDialog.dismiss()
            }
        })
    }

    /**
     * 图片选择完成的回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> data?.let { handleImageRequestResult(data) }
                else -> Log.d(TAG, "Unknown request code.")
            }
        } else {
            Log.e(TAG, String.format("Unexpected Result code %s", resultCode))
        }
    }

    /**
     * 图片选择完成的处理
     */
    private fun handleImageRequestResult(intent: Intent) {
        // If clipdata is available, we use it, otherwise we use data
        val imageUri: Uri? = intent.clipData?.let {
            it.getItemAt(0).uri
        } ?: intent.data

        if (imageUri == null) {
            Log.e(TAG, "Invalid input image Uri.")
            return
        }

        sweetAlertDialog.show()
        // 图片模糊处理
        model.setImageUri(imageUri.toString())
        model.applyBlur(3)
    }
}
