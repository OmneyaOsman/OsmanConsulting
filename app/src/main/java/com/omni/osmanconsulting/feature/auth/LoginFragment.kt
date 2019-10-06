package com.omni.osmanconsulting.feature.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.omni.osmanconsulting.R
import com.omni.osmanconsulting.core.*
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFirebaseAuth()

        register_link_btn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        login_btn.setOnClickListener {
            loginButtonClicked()
        }

        resend_verification_email.setOnClickListener {
            val dialog = ResendVerificationMailDialog()
            fragmentManager?.let {
                dialog.show(it, "dialog_resend_email_verification")
            }
        }

        password_edit_text.setOnKeyListener { _, _, _ ->
            if (password_edit_text.passwordState() == STATE.VALID) {
                password_text_input.error = null
            }
            false
        }

        email_input_edit_text.setOnKeyListener { _, _, _ ->
            if (email_input_edit_text.emailState() == STATE.VALID) {
                email_layout.error = null
            }

            false
        }
    }

    private fun setUpFirebaseAuth() {
        mAuthListener = FirebaseAuth.AuthStateListener { auth ->
            val currentUser = auth.currentUser
            if (currentUser != null) {
                if (currentUser.isEmailVerified) {
                    findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
                    requireActivity().finish()
                } else {
                    showToast("check your Email inbox for a verification link")
                    mAuth.signOut()
                }
            } else
                Log.d("Login", "user signed out")
        }
    }

    private fun loginButtonClicked() {
        cleanErrorState()
        val emailState = email_input_edit_text.emailState()
        val passwordState = password_edit_text.passwordState()

        when {
            emailState == STATE.VALID && passwordState == STATE.VALID -> doLogin()
            else -> {
                if (emailState == STATE.INVALID) email_layout.error =
                    getString(R.string.required_valid_email)
                else if (emailState == STATE.EMPTY) email_layout.error =
                    getString(R.string.required)
                if (passwordState == STATE.INVALID) password_text_input.error =
                    getString(R.string.password_error_msg)
                else if (passwordState == STATE.EMPTY) password_text_input.error =
                    getString(R.string.required)

            }
        }
    }

    private fun cleanErrorState() {
        email_layout.error = null
        password_text_input.error = null
    }

    private fun doLogin() {
        login_progress_bar.visibility = View.VISIBLE
        val password = inputValueString(password_edit_text)
        val email = inputValueString(email_input_edit_text)
        Log.d("Login", "$email $password")
        mAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                login_progress_bar.visibility = View.GONE
            }.addOnFailureListener {
                Toast.makeText(
                    activity,
                    "Authentication Failed : ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    override fun onStart() {
        super.onStart()
        mAuthListener.let {
            mAuth.addAuthStateListener(it)
            Log.d("Login", "set Listener")
        }
    }

    override fun onStop() {
        super.onStop()
        mAuthListener.let {
            mAuth.removeAuthStateListener(it)
            Log.d("Login", "remove Listener")
        }
    }

}