package com.joe.jetpackdemo.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController

import com.joe.jetpackdemo.R


/**
 * A simple [Fragment] subclass.
 *
 */
class NavHostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnStart: Button = view.findViewById(R.id.btn_next)
        btnStart.setOnClickListener {
            findNavController(this).navigate(R.id.firstFragment, null)
        }


        view.findViewById<Button>(R.id.btn_change)?.setOnClickListener {
            val action = NavHostFragmentDirections.nextAction().setFlowStepNum(5)
            findNavController().navigate(action)
        }


        /*view.findViewById<Button>(R.id.btn_change)?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.next_action, null)
        )*/


    }


}
