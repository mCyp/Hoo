package com.joe.jetpackdemo.ui.fragment.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.common.BaseConstant
import com.joe.jetpackdemo.databinding.RegisterFragmentBinding
import com.joe.jetpackdemo.viewmodel.CustomViewModelProvider
import com.joe.jetpackdemo.viewmodel.RegisterModel

/**
 * 注册的Fragment
 *
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    private val registerModel:RegisterModel by viewModels{
        CustomViewModelProvider.providerRegisterModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: RegisterFragmentBinding = DataBindingUtil.inflate(
            inflater
            , R.layout.register_fragment
            , container
            , false
        )

        initData(binding)
        onSubscribeUi(binding)
        return binding.root
    }

    private fun initData(binding: RegisterFragmentBinding) {
        // SafeArgs的使用
        val safeArgs:RegisterFragmentArgs by navArgs()
        val email = safeArgs.email
        binding.model?.mail?.value = email

        binding.lifecycleOwner = this
        binding.model = registerModel
        binding.activity = activity
    }

    private fun onSubscribeUi(binding: RegisterFragmentBinding) {
        binding.btnRegister.setOnClickListener {
            registerModel.register()
            val bundle = Bundle()
            bundle.putString(BaseConstant.ARGS_NAME, registerModel.n.value)
            findNavController().navigate(R.id.login, bundle, null)
        }
    }
}
