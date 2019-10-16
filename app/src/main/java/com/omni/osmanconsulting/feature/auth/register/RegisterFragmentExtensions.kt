package com.omni.osmanconsulting.feature.auth.register

import android.util.Log
import androidx.lifecycle.Observer
import com.omni.domain.AuthState
import com.omni.osmanconsulting.R
import com.omni.osmanconsulting.core.navigateTo
import com.omni.osmanconsulting.core.showToast
import com.omni.osmanconsulting.core.updateEmailErrorState
import com.omni.osmanconsulting.core.updatePasswordErrorState
import kotlinx.android.synthetic.main.fragment_register.*

fun RegisterFragment.bindRegisterViewModel() {

    with(registerViewModel) {

        passwordState.observe(this@bindRegisterViewModel, Observer {
            password_text_input_register.error = updatePasswordErrorState(it)
        })
        emailState.observe(this@bindRegisterViewModel, Observer {
            email_layout_register.error = updateEmailErrorState(it)
        })
        confirmPasswordState.observe(this@bindRegisterViewModel, Observer {
            confirm_password_text_input_register.error =
                if (it) null else getString(R.string.password_error_dont_match)
        })
        authState.observe(this@bindRegisterViewModel, Observer {
            when (it) {
                AuthState.AUTHENTICATED -> {
                    showToast("sent verification E-mail")
                    navigateTo(R.id.action_signUpFragment_to_loginFragment)
                }
                AuthState.FAILED ->
                    showToast("${registerViewModel.errorMsg.value}")
                else ->
                    Log.d("register", it.name)

            }
        })
    }
}