package com.omni.osmanconsulting.core

import android.text.Editable
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.omni.domain.entity.Status
import com.omni.osmanconsulting.R
import java.util.*

enum class STATE {
    VALID,
    EMPTY,
    INVALID
}

const val DOMAIN_NAME = "gmail.com"

val inputValueString = { editText: EditText -> editText.text.toString().trim() }

fun EditText.passwordState(): STATE {
    return when {
        text == null || text.isEmpty() -> return STATE.EMPTY
        text.length >= 8 -> return STATE.VALID
        else -> STATE.INVALID
    }
}

fun EditText.emailState(): STATE {
    return when {
        text == null || text.isEmpty() -> return STATE.EMPTY
        text.isValidDomain() -> return STATE.VALID
        else -> STATE.INVALID
    }
}


fun Editable.isValidDomain() =
    substring(indexOf("@") + 1).toLowerCase(Locale.ENGLISH) == DOMAIN_NAME

fun TextInputLayout.clear() {
    error = null
}

fun Fragment.hideSoftKeyboard() {
    requireNotNull(activity)
        .window
        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}

fun Fragment.showToast(msg: String) {
    Toast.makeText(requireNotNull(activity), msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.navigateTo(@IdRes action: Int) {
    findNavController().navigate(action)
}

fun Fragment.updateEmailErrorState(emailState: Status) = when (emailState) {
    Status.VALID -> null
    Status.INVALID -> getString(R.string.required_valid_email)
    Status.EMPTY -> getString(R.string.required)
}

fun Fragment.updatePasswordErrorState(passwordState: Status) = when (passwordState) {
    Status.VALID -> null
    Status.INVALID -> getString(R.string.password_error_msg)
    Status.EMPTY -> getString(R.string.required)
}

