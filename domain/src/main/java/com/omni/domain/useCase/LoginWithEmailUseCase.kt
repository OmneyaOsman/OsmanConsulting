package com.omni.domain.useCase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuthException
import com.omni.domain.AuthState
import com.omni.domain.AuthenticationDataRepository
import com.omni.domain.engine.Status
import java.util.*


class LoginWithEmailUseCase(
    private val _authState: MutableLiveData<AuthState>,
    private val _emailState: MutableLiveData<Status>,
    private val _passwordState: MutableLiveData<Status>,
    private val authRepository: AuthenticationDataRepository
) {

    suspend operator fun invoke(email: String, password: String) {
        val emailState = email.emailState()
        val passwordState = password.passwordState()

        _emailState.postValue(emailState)
        _passwordState.postValue(passwordState)
        Log.d("Login", " ${_emailState.value}")
        Log.d("Login", " ${_passwordState.value}")
        try {
            emailState.takeIf {
                it == Status.VALID && passwordState == Status.VALID
            }?.also {
                _authState.postValue(AuthState.LOADING)
            }?.let {
                authRepository.login(email, password)
            }?.let {
                if (it.isEmailVerified)
                    _authState.postValue(AuthState.AUTHENTICATED)
                else
                    _authState.postValue(AuthState.NOT_VERIFIED)
            } ?: kotlin.run {
                _authState.postValue(AuthState.FAILED)
            }
        } catch (ex: FirebaseAuthException) {
            _authState.postValue(AuthState.FAILED)
        }
    }

    private fun String.isValidDomain() =
        substring(indexOf("@") + 1).toLowerCase(Locale.ENGLISH) == DOMAIN_NAME

    private fun String.emailState(): Status {
        return when {
            isBlank() || isNullOrEmpty() -> Status.EMPTY
            isValidDomain() -> Status.VALID
            else -> Status.INVALID
        }
    }

    private fun String.passwordState(): Status {
        return when {
            isBlank() || isNullOrEmpty() -> Status.EMPTY
            length >= 8 -> Status.VALID
            else -> Status.INVALID
        }
    }

    companion object {
        const val DOMAIN_NAME = "gmail.com"
    }
}