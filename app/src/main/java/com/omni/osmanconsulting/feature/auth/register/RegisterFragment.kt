package com.omni.osmanconsulting.feature.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.omni.osmanconsulting.R
import com.omni.osmanconsulting.core.*
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_btn?.setOnClickListener {
            registerButtonClicked()
        }

        login_link_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        password_edit_text_register.setOnKeyListener { _, _, _ ->
            if (password_edit_text_register.passwordState() == STATE.VALID) {
                password_text_input_register.error = null
            }
            false
        }
        confirm_password_edit_text_register.setOnKeyListener { _, _, _ ->
            if (confirm_password_edit_text_register.passwordState() == STATE.VALID) {
                confirm_password_text_input_register.error = null
            }
            false
        }
        email_input_edit_text_register.setOnKeyListener { _, _, _ ->
            if (email_input_edit_text_register.emailState() == STATE.VALID) {
                email_layout_register.error = null
            }

            false
        }

    }

    private fun registerButtonClicked() {

        val emailState = email_input_edit_text_register.emailState()
        val passwordState = password_edit_text_register.passwordState()
        val confirmedPasswordState = password_edit_text_register.passwordState()

        val password = inputValueString(password_edit_text_register)
        val confirmedPassword = inputValueString(confirm_password_edit_text_register)

        when {
            emailState == STATE.VALID && passwordState == STATE.VALID && confirmedPasswordState == STATE.VALID -> {
                val email = inputValueString(email_input_edit_text_register)

                if (password == confirmedPassword)
                    registerNewUser(email, password)
                else
                    confirm_password_text_input_register.error =
                        getString(R.string.password_error_dont_match)
            }
            else -> {
                if (emailState == STATE.EMPTY)
                    email_layout_register.error = getString(R.string.required)
                else
                    email_layout_register.error = getString(R.string.register_with_company_email)

                if (emailState == STATE.EMPTY)
                    password_text_input_register.error = getString(R.string.required)
                else
                    password_text_input_register.error = getString(R.string.password_error_msg)

                if (confirmedPasswordState == STATE.EMPTY)
                    confirm_password_text_input_register.error = getString(R.string.required)
                else
                    confirm_password_text_input_register.error =
                        getString(R.string.password_error_dont_match)


            }
        }

        hideSoftKeyboard()
    }


    private fun registerNewUser(email: String, password: String) {
        cleanErrorsState()
        register_progress_bar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendVerificationEMail()
                    mAuth.signOut()
                    findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                } else
                   showToast(
                        "In Successful Operation${task.exception?.message}")
                register_progress_bar.visibility = View.GONE

            }


    }


    private fun cleanErrorsState() {
        confirm_password_text_input_register.error = null
        password_text_input_register.error = null
        email_layout_register.error = null
    }

    private fun sendVerificationEMail() {
        mAuth.currentUser?.let {
            it.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        showToast("sent verification E-mail")
                    else
                        showToast("couldn't send verification E-mail")

                }
        }
    }
// todo add binding data ...  ViewModel to manage state
}