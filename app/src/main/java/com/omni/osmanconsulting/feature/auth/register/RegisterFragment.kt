package com.omni.osmanconsulting.feature.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.omni.osmanconsulting.core.*
import com.omni.osmanconsulting.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    val registerViewModel: RegisterViewModel by lazy {
        ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentRegisterBinding.inflate(inflater).apply {
            this?.lifecycleOwner = this@RegisterFragment
            this?.registerViewModel = registerViewModel
        }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindRegisterViewModel()

        register_btn?.setOnClickListener { registerUser() }

        login_link_btn.setOnClickListener { findNavController().navigateUp() }

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

    private fun registerUser() {
        val email = inputValueString(email_input_edit_text_register)
        val password = inputValueString(password_edit_text_register)
        val confirmedPassword = inputValueString(confirm_password_edit_text_register)
        registerViewModel.registerWithEmail(email, password, confirmedPassword)
        hideSoftKeyboard()
    }

//    private fun registerNewUser(email: String, password: String) {
//        cleanErrorsState()
//        mAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    sendVerificationEMail()
//                    mAuth.signOut()
//                } else
//                   showToast(
//                        "In Successful Operation${task.exception?.message}")
//
//            }
//
//
//    }
//
//
//    private fun cleanErrorsState() {
//        confirm_password_text_input_register.error = null
//        password_text_input_register.error = null
//        email_layout_register.error = null
//    }
//
//    private fun sendVerificationEMail() {
//        mAuth.currentUser?.let {
//            it.sendEmailVerification()
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful)
//                        showToast("sent verification E-mail")
//                    else
//                        showToast("couldn't send verification E-mail")
//
//                }
//        }
//    }
//
//}
}