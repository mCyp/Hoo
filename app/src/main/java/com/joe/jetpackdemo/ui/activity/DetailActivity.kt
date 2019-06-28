package com.joe.jetpackdemo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.common.BaseConstant
import com.joe.jetpackdemo.databinding.ActivityDetailBinding
import com.joe.jetpackdemo.utils.AppPrefsUtils
import com.joe.jetpackdemo.viewmodel.CustomViewModelProvider
import com.joe.jetpackdemo.viewmodel.DetailModel
import com.joe.jetpackdemo.viewmodel.factory.FavouriteShoeModelFactory

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
        //setContentView(R.layout.activity_detail)

        val binding = DataBindingUtil.setContentView<ActivityDetailBinding>(this, R.layout.activity_detail)
        observer(binding)
    }

    private fun observer(binding: ActivityDetailBinding) {
        detailModel.shoe.observe(this, Observer {
            binding.shoe = it
            binding.price = it.price.toString()
        })

        detailModel.favouriteShoe.observe(this, Observer {
            binding.v = if (it == null) View.VISIBLE else View.GONE
        })
    }
}
