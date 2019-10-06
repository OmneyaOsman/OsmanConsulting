package com.omni.osmanconsulting.feature.auth

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.omni.osmanconsulting.R
import com.omni.osmanconsulting.core.*
import kotlinx.android.synthetic.main.dialog_resend_verify_mail.*
import kotlinx.android.synthetic.main.dialog_resend_verify_mail.view.*


class ResendVerificationMailDialog : DialogFragment() {

private lateinit var mContext :Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.dialog_resend_verify_mail,
            container,
            false
        )
mContext = requireNotNull(context)
        view.cancel_sending_btn.setOnClickListener {
            dialog?.dismiss()
        }

        view.confirm_sending_btn.setOnClickListener {
           confirmSending()
        }

        with(requireNotNull(dialog?.window)) {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        view.password_edit_text_verify.setOnKeyListener { _, _, _ ->
            if (view.password_edit_text_verify.passwordState() == STATE.VALID) {
                view.password_text_input_verify.error = null
            }
            false
        }

        view.email_input_edit_text_verify.setOnKeyListener { _, _, _ ->
            if (view.email_input_edit_text_verify.emailState() == STATE.VALID) {
                view.email_layout_verify.error = null
            }

            false
        }
        return view
    }

    private fun confirmSending(){
        cleanErrorState()
        val emailState = email_input_edit_text_verify?.emailState()
        val passwordState = password_edit_text_verify?.passwordState()

        val email = inputValueString(email_input_edit_text_verify)
        val password = inputValueString(password_edit_text_verify)
        when {
            emailState == STATE.VALID && passwordState == STATE.VALID -> authenticateAndResendEmail(email, password)
            else -> {
                if (emailState == STATE.INVALID) email_layout_verify.error =
                    getString(R.string.required_valid_email)
                else if (emailState == STATE.EMPTY) email_layout_verify.error =
                    getString(R.string.required)
                if (passwordState == STATE.INVALID) password_text_input_verify.error =
                    getString(R.string.password_error_msg)
                else if (passwordState == STATE.EMPTY) password_text_input_verify.error =
                    getString(R.string.required)

            }
        }
     hideSoftKeyboard()
    }
    /**
     * re-authenticate so we can send a verification email again
     * @param email
     * @param password
     */
    private fun authenticateAndResendEmail(email: String, password: String) {
        verify_progress_bar?.visibility = View.VISIBLE
        val credential = EmailAuthProvider
            .getCredential(email, password)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                verify_progress_bar?.visibility = View.GONE
                if (task.isSuccessful) {
                    Log.d("Verify", "onComplete: re-authenticate success.")
                    sendVerificationEmail()
                    FirebaseAuth.getInstance().signOut()
                    dialog?.dismiss()
                }
            }.addOnFailureListener {
                verify_progress_bar?.visibility = View.GONE
                showToast("Invalid Credentials. \nReset your password and try again")
                dialog?.dismiss()
            }

    }

    /**
     * sends an email verification link to the user
     */
    private fun sendVerificationEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful)
                showToast("Sent Verification Email")
            else
                showToast("couldn't send email")

        }
    }

    private fun cleanErrorState() {
        email_layout_verify.error = null
        password_text_input_verify.error = null
    }

    private fun showToast(string: String){
        Toast.makeText(mContext , string , Toast.LENGTH_SHORT).show()
    }
}

