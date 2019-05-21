package com.joe.jetpackdemo.ui.fragment.login


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.joe.jetpackdemo.MainActivity

import com.joe.jetpackdemo.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RegisterFragment : androidx.fragment.app.Fragment() {

    lateinit var mCancel: TextView
    lateinit var mRegister: Button
    lateinit var mEmailEt:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCancel = view.findViewById(R.id.txt_cancel)
        mRegister = view.findViewById(R.id.btn_register)
        mEmailEt = view.findViewById(R.id.et_email)

        mRegister.setOnClickListener {
            Toast.makeText(context,"Register",Toast.LENGTH_SHORT).show()
        }

        mCancel.setOnClickListener {
            activity?.onBackPressed()
        }

        val safeArgs:RegisterFragmentArgs by navArgs()
        val email = safeArgs.email
        mEmailEt.setText(email)

    }


}
