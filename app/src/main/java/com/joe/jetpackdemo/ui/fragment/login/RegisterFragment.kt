package com.joe.jetpackdemo.ui.fragment.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.databinding.FragmentRegisterBinding
import com.joe.jetpackdemo.viewmodel.LoginModel

/**
 * 注册的Fragment
 *
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    /*lateinit var mCancel: TextView
    lateinit var mRegister: Button
    lateinit var mEmailEt:EditText*/
    lateinit var loginModel: LoginModel
    var isEnable:Boolean = false
    lateinit var binding: FragmentRegisterBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRegisterBinding = DataBindingUtil.inflate(
            inflater
            , R.layout.fragment_register
            , container
            , false
        )
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
        mRegister = view.findViewById(R.id.btn_register)
        mEmailEt = view.findViewById(R.id.et_email)*/

        /*mRegister.setOnClickListener {
            Toast.makeText(context,"Register",Toast.LENGTH_SHORT).show()
        }

        mCancel.setOnClickListener {
            activity?.onBackPressed()
        }*/

        val safeArgs:RegisterFragmentArgs by navArgs()
        val email = safeArgs.email
        binding.model?.mail?.value = email

        loginModel.p.observe(viewLifecycleOwner, Observer {
            binding.isEnable = it.isNotEmpty()
                    && loginModel.n.value!!.isNotEmpty()
                    && loginModel.mail.value!!.isNotEmpty()
        })

        loginModel.n.observe(viewLifecycleOwner, Observer {
            binding.isEnable = it.isNotEmpty()
                    && loginModel.p.value!!.isNotEmpty()
                    && loginModel.mail.value!!.isNotEmpty()
        })

        loginModel.mail.observe(viewLifecycleOwner, Observer {
            binding.isEnable = it.isNotEmpty()
                    && loginModel.n.value!!.isNotEmpty()
                    && loginModel.p.value!!.isNotEmpty()
        })

        //mEmailEt.setText(email)

    }


}
