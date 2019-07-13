package com.joe.jetpackdemo.ui.fragment.login


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.joe.jetpackdemo.ui.activity.MainActivity
import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.common.BaseConstant
import com.joe.jetpackdemo.databinding.LoginFragmentBinding
import com.joe.jetpackdemo.utils.AppPrefsUtils
import com.joe.jetpackdemo.viewmodel.CustomViewModelProvider
import com.joe.jetpackdemo.viewmodel.LoginModel

/**
 * 登录的Fragment
 *
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private val loginModel: LoginModel by viewModels {
        CustomViewModelProvider.providerLoginModel(requireContext())
    }
    private var isEnable: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO 研究DataBindComponent
        val binding: LoginFragmentBinding = DataBindingUtil.inflate(
            inflater
            , R.layout.login_fragment
            , container
            , false
        )
        // 生成Binding的另外一种方式
        /*val binding = FragmentLoginBinding.inflate(
            inflater
            , container
            , false
        )*/
        onSubscribeUi(binding)
        return binding.root
    }

    private fun onSubscribeUi(binding: LoginFragmentBinding) {
        binding.model = loginModel
        binding.isEnable = isEnable
        binding.activity = activity

        binding.btnLogin.setOnClickListener {
            loginModel.login()?.observe(this, Observer { user ->
                user?.let {
                    AppPrefsUtils.putLong(BaseConstant.SP_USER_ID, it.id)
                    AppPrefsUtils.putString(BaseConstant.SP_USER_NAME, it.account)
                    val intent = Intent(context, MainActivity::class.java)
                    context!!.startActivity(intent)
                    Toast.makeText(context, "登录成功！", Toast.LENGTH_SHORT).show()
                }
            })
        }

        loginModel.p.observe(viewLifecycleOwner, Observer {
            // 保证账号和密码不为空的时候才可以登录
            binding.isEnable = it.isNotEmpty() && loginModel.n.value!!.isNotEmpty()
        })

        val name = arguments?.getString(BaseConstant.ARGS_NAME)
        if (!TextUtils.isEmpty(name))
            loginModel.n.value = name!!
    }
}
