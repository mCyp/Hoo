package com.joe.jetpackdemo.ui.fragment.login


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.common.BaseConstant
import com.joe.jetpackdemo.databinding.LoginFragmentBinding
import com.joe.jetpackdemo.ui.activity.MainActivity
import com.joe.jetpackdemo.utils.AppPrefsUtils
import com.joe.jetpackdemo.viewmodel.CustomViewModelProvider
import com.joe.jetpackdemo.viewmodel.LoginModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 登录的Fragment
 *
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private val loginModel: LoginModel by viewModels {
        CustomViewModelProvider.providerLoginModel(requireContext())
    }

    /*private val loginModel: LoginModel by lazy {
        ViewModelProviders.of(this).get(LoginModel::class.java)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO 研究DataBindComponent
        // 1.Binding生成的方式一
        val binding: LoginFragmentBinding = DataBindingUtil.inflate(
            inflater
            , R.layout.login_fragment
            , container
            , false
        )

        // 2.Binding生成的方式二
        /*val binding = FragmentLoginBinding.inflate(
            inflater
            , container
            , false
        )*/

        onSubscribeUi(binding)

        // 判断当前是否是第一次登陆
        val isFirstLaunch = AppPrefsUtils.getBoolean(BaseConstant.IS_FIRST_LAUNCH)
        if(isFirstLaunch){
            onFirstLaunch()
        }

        return binding.root
    }

    private fun onSubscribeUi(binding: LoginFragmentBinding) {
        binding.model = loginModel
        binding.activity = activity

        // 如果使用LiveData下面这句必须加上 ！！！
        binding.lifecycleOwner = this

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

        arguments?.getString(BaseConstant.ARGS_NAME)?.apply {
            loginModel.n.value = this
        }
    }

    // 第一次启动的时候调用
    private fun onFirstLaunch(){
        lifecycleScope.launch(Dispatchers.Main) {
            val str = withContext(Dispatchers.IO) {
                loginModel.onFirstLaunch()
            }
            Toast.makeText(context!!,str,Toast.LENGTH_SHORT).show()
            AppPrefsUtils.putBoolean(BaseConstant.IS_FIRST_LAUNCH,false)
        }
    }
}
