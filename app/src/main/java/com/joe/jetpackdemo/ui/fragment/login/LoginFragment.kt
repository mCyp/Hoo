package com.joe.jetpackdemo.ui.fragment.login


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.databinding.FragmentLoginBinding
import com.joe.jetpackdemo.viewmodel.LoginModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : androidx.fragment.app.Fragment() {

    /*lateinit var mCancel: TextView
    lateinit var mLogin: Button
    lateinit var mAccount: EditText*/
    lateinit var loginModel:LoginModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO 研究DataBindComponent
        /*val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater
            , R.layout.fragment_login
            , container
            , false
        )*/
        val binding = FragmentLoginBinding.inflate(
            inflater
            , container
            , false
        )
        loginModel = LoginModel("","",context!!)
        binding.model = loginModel
        binding.activity = activity
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

        val name = arguments?.getString("name")
        if(!TextUtils.isEmpty(name))
            loginModel.n.set(name!!)
        //mAccount.setText(name)
    }


}
