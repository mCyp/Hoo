package com.joe.jetpackdemo.ui.fragment.main


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.joe.jetpackdemo.common.BaseConstant.KEY_IMAGE_URI
import com.joe.jetpackdemo.databinding.FragmentMeBinding
import com.joe.jetpackdemo.viewmodel.CustomViewModelProvider
import com.joe.jetpackdemo.viewmodel.MeModel


/**
 * A simple [Fragment] subclass.
 *
 */
class MeFragment : Fragment() {
    private val REQUEST_CODE_IMAGE = 100

    private val TAG by lazy { MeFragment::class.java.simpleName }

    private val model: MeModel by viewModels {
        CustomViewModelProvider.providerMeModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMeBinding = FragmentMeBinding.inflate(inflater, container, false)
        initListener(binding)
        onSubscribeUi(binding)
        return binding.root
    }

    /**
     * 初始化监听器
     */
    private fun initListener(binding: FragmentMeBinding){
        binding.ivHead.setOnClickListener {
            val chooseIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(chooseIntent, REQUEST_CODE_IMAGE)
        }
    }

    /**
     * Binding绑定
     */
    private fun onSubscribeUi(binding: FragmentMeBinding) {
        model.user.observe(this, Observer {
            binding.user = it
        })

        model.outPutWorkInfos.observe(this, Observer {
            if (it.isNullOrEmpty())
                return@Observer

            val state = it[0]
            if (state.state.isFinished) {
                // 更新头像
                val outputImageUri = state.outputData.getString(KEY_IMAGE_URI)
                if(!outputImageUri.isNullOrEmpty()){
                    model.setOutputUri(outputImageUri)
                }
            } else {
                // TODO 为完成情况
            }
        })
    }

    /** Image Selection  */
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

    private fun handleImageRequestResult(intent: Intent) {
        // If clipdata is available, we use it, otherwise we use data
        val imageUri: Uri? = intent.clipData?.let {
            it.getItemAt(0).uri
        } ?: intent.data

        if (imageUri == null) {
            Log.e(TAG, "Invalid input image Uri.")
            return
        }

        model.setImageUri(imageUri.toString())
        model.applyBlur(3)
    }


}
