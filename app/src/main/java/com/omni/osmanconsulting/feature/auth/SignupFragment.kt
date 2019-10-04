package com.omni.osmanconsulting.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.omni.osmanconsulting.R
import com.omni.osmanconsulting.core.*
import kotlinx.android.synthetic.main.fragment_register.*

class SignupFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_register, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_btn?.setOnClickListener {
            onRegisterButtonClicked()
        }

        password_edit_text_register.setOnKeyListener { v, keyCode, event ->
            if (password_edit_text_register.passwordState() == STATE.VALID) {
                password_text_input_register.error = null
            }
            false
        }
        confirm_password_edit_text_register.setOnKeyListener { v, keyCode, event ->
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

    private fun onRegisterButtonClicked() {

        val emailState = email_input_edit_text_register.emailState()
        val passwordState = password_edit_text_register.passwordState()
        val confirmedPasswordState = password_edit_text_register.passwordState()

        val password = inputValueString(password_edit_text_register)
        val confirmedPassword = inputValueString(confirm_password_edit_text_register)

        if (emailState == STATE.VALID && passwordState == STATE.VALID && confirmedPasswordState == STATE.VALID) {
            val email = inputValueString(email_input_edit_text_register)

            if (password == confirmedPassword)
                registerNewUser(email, password)
            else
                confirm_password_text_input_register.error = getString(R.string.password_error_dont_match)
        } else {
            if (emailState == STATE.EMPTY || emailState == STATE.INVALID)
                if (emailState == STATE.EMPTY)
                    email_layout_register.error = getString(R.string.required)
                else
                    email_layout_register.error = getString(R.string.register_with_company_email)
            if (passwordState == STATE.EMPTY || passwordState == STATE.INVALID)
                if (emailState == STATE.EMPTY)
                    password_text_input_register.error = getString(R.string.required)
                else
                    password_text_input_register.error = getString(R.string.password_error_msg)
            if (confirmedPasswordState == STATE.EMPTY || confirmedPassword != password)
                if (confirmedPasswordState == STATE.EMPTY)
                    confirm_password_text_input_register.error = getString(R.string.required)
                else
                    confirm_password_text_input_register.error = getString(R.string.password_error_dont_match)


        }

        hideSoftKeyboard()
    }

    private fun registerNewUser(email: String, password: String) {
        updateErrorsState()
        Toast.makeText(activity, "new user", Toast.LENGTH_LONG).show()
    }


    private fun updateErrorsState() {
        confirm_password_text_input_register.error = null
        password_text_input_register.error = null
        email_layout_register.error = null
    }
}