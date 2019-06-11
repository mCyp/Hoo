package com.joe.jetpackdemo.ui.fragment.login


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.databinding.FragmentLoginBinding
import com.joe.jetpackdemo.viewmodel.LoginModel

/**
 * 登录的Fragment
 *
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    /*lateinit var mCancel: TextView
    lateinit var mLogin: Button
    lateinit var mAccount: EditText*/
    lateinit var loginModel:LoginModel
    var isEnable:Boolean = false
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
        loginModel = ViewModelProviders.of(this).get(LoginModel::class.java)
        loginModel.context = context!!
        binding.model = loginModel
        binding.isEnable = isEnable
        binding.activity = activity
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*mCancel = view.findViewById(R.id.txt_cancel)
        mLogin = view.findViewById(R.id.btn_login)
        mAccount = view.findViewById(R.id.et_account)

        mLogin.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            context!!.startActivity(intent)
        }

        mCancel.setOnClickListener {
            activity?.onBackPressed()
        }*/

        loginModel.p.observe(viewLifecycleOwner, Observer {
            binding.isEnable = it.isNotEmpty() && loginModel.n.value!!.isNotEmpty()
        })

        val name = arguments?.getString("name")
        if(!TextUtils.isEmpty(name))
            loginModel.n.value = name!!
        //mAccount.setText(name)
    }


}
