package com.joe.jetpackdemo.ui.fragment.login


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.databinding.FragmentLoginBinding
import com.joe.jetpackdemo.viewmodel.CustomViewModelProvider
import com.joe.jetpackdemo.viewmodel.LoginModel

/**
 * 登录的Fragment
 *
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private val loginModel: LoginModel by viewModels{
        CustomViewModelProvider.providerLoginModel(requireContext())
    }
    var isEnable: Boolean = false
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO 研究DataBindComponent
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater
            , R.layout.fragment_login
            , container
            , false
        )
        /*val binding = FragmentLoginBinding.inflate(
            inflater
            , container
            , false
        )*/
        loginModel.lifecycleOwner = viewLifecycleOwner
        binding.model = loginModel
        binding.isEnable = isEnable
        binding.activity = activity
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginModel.p.observe(viewLifecycleOwner, Observer {
            binding.isEnable = it.isNotEmpty() && loginModel.n.value!!.isNotEmpty()
        })

        val name = arguments?.getString("name")
        if (!TextUtils.isEmpty(name))
            loginModel.n.value = name!!

        //mAccount.setText(name)
    }


}
