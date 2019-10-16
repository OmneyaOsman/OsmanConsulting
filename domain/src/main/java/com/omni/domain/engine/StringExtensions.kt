package com.omni.domain.engine

import com.omni.domain.entity.Status
import java.util.*


private const val DOMAIN_NAME = "gmail.com"
fun String.isValidDomain() =
    substring(indexOf("@") + 1).toLowerCase(Locale.ENGLISH) == DOMAIN_NAME

fun String.emailState(): Status {
    return when {
        isBlank() || isNullOrEmpty() -> Status.EMPTY
        isValidDomain() -> Status.VALID
        else -> Status.INVALID
    }
}

fun String.passwordState(): Status {
    return when {
        isBlank() || isNullOrEmpty() -> Status.EMPTY
        length >= 8 -> Status.VALID
        else -> Status.INVALID
    }
}

fun String.isMatch(password: String) = this == password

