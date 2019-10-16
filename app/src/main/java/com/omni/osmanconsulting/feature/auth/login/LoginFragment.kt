package com.omni.osmanconsulting.feature.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.omni.osmanconsulting.R
import com.omni.osmanconsulting.core.*
import com.omni.osmanconsulting.databinding.FragmentLoginBinding
import com.omni.osmanconsulting.feature.auth.login.verify.ResendVerificationMailDialog
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    val loginViewModel: LoginViewModel
            by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }

//    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentLoginBinding.inflate(inflater).apply {
            this?.lifecycleOwner = this@LoginFragment
            this?.loginViewModel = loginViewModel
        }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindLoginViewModel()
        register_link_btn.setOnClickListener {
            navigateTo(R.id.action_loginFragment_to_signUpFragment)
        }

        login_btn.setOnClickListener {
            doLogin()
        }

        resend_verification_email.setOnClickListener {
            showResendVerificationDialog()
        }

        password_edit_text.setOnKeyListener { _, _, _ ->
            if (password_edit_text.passwordState() == STATE.VALID) {
                password_text_input.clear()
            }
            false
        }

        email_input_edit_text.setOnKeyListener { _, _, _ ->
            if (email_input_edit_text.emailState() == STATE.VALID) {
                email_layout.clear()
            }

            false
        }


    }

    private fun showResendVerificationDialog() {
        val dialog =
            ResendVerificationMailDialog()
        fragmentManager?.let {
            dialog.show(it, "dialog_resend_email_verification")
        }
    }

    private fun doLogin() {
        val password = inputValueString(password_edit_text)
        val email = inputValueString(email_input_edit_text)
        loginViewModel.signIn(email, password)
        hideSoftKeyboard()
    }

//    override fun onStart() {
//        super.onStart()
//        mAuthListener.let {
//            mAuth.addAuthStateListener(it)
//            Log.d("Login", "set Listener")
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        mAuthListener.let {
//            mAuth.removeAuthStateListener(it)
//            Log.d("Login", "remove Listener")
//        }
//    }

}