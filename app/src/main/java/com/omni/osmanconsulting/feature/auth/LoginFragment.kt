package com.omni.osmanconsulting.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.omni.osmanconsulting.R


class LoginFragment :Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_login , container , false)
        return rootView
    }

    // todo 2- try login
    private fun performLogin(){

    }
}