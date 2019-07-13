package com.joe.jetpackdemo.ui.fragment.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.databinding.FavouriteFragmentBinding
import com.joe.jetpackdemo.ui.adapter.FavouriteAdapter
import com.joe.jetpackdemo.ui.adapter.ShoeAdapter
import com.joe.jetpackdemo.viewmodel.CustomViewModelProvider
import com.joe.jetpackdemo.viewmodel.FavouriteModel


/**
 * 收藏页面
 */
class FavouriteFragment : Fragment() {

    private val viewModel: FavouriteModel by viewModels {
        CustomViewModelProvider.providerFavouriteModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FavouriteFragmentBinding = FavouriteFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root
        val adapter = FavouriteAdapter(context!!)
        binding.recycler.adapter = adapter
        onSubscribeUi(adapter, binding)
        return binding.root
    }

    /**
     * 鞋子数据更新的通知
     */
    private fun onSubscribeUi(adapter: FavouriteAdapter, binding: FavouriteFragmentBinding) {
        binding.empty.bind(arrayOf(binding.recycler))
        binding.empty.triggerLoading()

        viewModel.shoes.observe(viewLifecycleOwner, Observer {
            if (it != null && it.isNotEmpty()) {
                adapter.submitList(it)
            }
            binding.empty.triggerOkOrEmpty(adapter.itemCount > 0)
        })
    }



}
