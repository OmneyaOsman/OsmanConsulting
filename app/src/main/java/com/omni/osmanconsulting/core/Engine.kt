package com.omni.osmanconsulting.core

import android.text.Editable
import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.Fragment

enum class STATE {
    VALID,
    EMPTY,
    INVALID
}

const val DOMAIN_NAME = "osman.ca"

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
        text.isValidDomain()  -> return STATE.VALID
        else -> STATE.INVALID
    }
}

fun Editable.isValidDomain()= substring(indexOf("@") + 1).toLowerCase() == DOMAIN_NAME

fun Fragment.hideSoftKeyboard() {
    requireNotNull(activity)
        .window
        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}