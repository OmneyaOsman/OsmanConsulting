package com.omni.osmanconsulting.feature.auth.login

import android.util.Log
import androidx.lifecycle.Observer
import com.omni.domain.AuthState
import com.omni.osmanconsulting.R
import com.omni.osmanconsulting.core.navigateTo
import com.omni.osmanconsulting.core.showToast
import com.omni.osmanconsulting.core.updateEmailErrorState
import com.omni.osmanconsulting.core.updatePasswordErrorState
import kotlinx.android.synthetic.main.fragment_login.*


fun LoginFragment.bindLoginViewModel() {

    with(loginViewModel) {
        passwordState.observe(this@bindLoginViewModel, Observer {
            password_text_input.error = updatePasswordErrorState(it)
        })
        emailState.observe(this@bindLoginViewModel, Observer {
            email_layout.error = updateEmailErrorState(it)
        })

        authState.observe(this@bindLoginViewModel, Observer {
            when (it) {
                AuthState.AUTHENTICATED -> {
                    navigateTo(R.id.action_loginFragment_to_mainActivity)
                    requireActivity().finish()
                }
                AuthState.NOT_VERIFIED -> {
                    showToast("check your Email inbox for a verification link")
                    loginViewModel.signOutUser()
                }
                AuthState.FAILED -> showToast("${loginViewModel.errorMsg.value}")
                else ->
                    Log.d("Login", it.name)
            }
        })
    }
}




