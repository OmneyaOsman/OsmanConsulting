package com.omni.osmanconsulting.feature.auth.login

import android.util.Log
import androidx.lifecycle.Observer
import com.omni.domain.AuthState
import com.omni.domain.engine.Status
import com.omni.osmanconsulting.R
import com.omni.osmanconsulting.core.navigateTo
import com.omni.osmanconsulting.core.showToast
import kotlinx.android.synthetic.main.fragment_login.*


fun LoginFragment.bindLoginViewModel() {

    with(loginViewModel) {
        passwordState.observe(this@bindLoginViewModel, Observer { updatePasswordErrorState(it) })
        emailState.observe(this@bindLoginViewModel, Observer { updateEmailErrorState(it) })

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
                else ->
                    Log.d("Login", it.name)
            }
        })
    }
}

private fun LoginFragment.updateEmailErrorState(emailState: Status) {
    email_layout.error = when (emailState) {
        Status.VALID -> null
        Status.INVALID -> getString(R.string.required_valid_email)
        Status.EMPTY -> getString(R.string.required)
    }
}

private fun LoginFragment.updatePasswordErrorState(passwordState: Status) {
    password_text_input.error = when (passwordState) {
        Status.VALID -> null
        Status.INVALID -> getString(R.string.password_error_msg)
        Status.EMPTY -> getString(R.string.required)
    }
}


