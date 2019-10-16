package com.omni.domain.useCase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuthException
import com.omni.domain.AuthState
import com.omni.domain.engine.emailState
import com.omni.domain.engine.isMatch
import com.omni.domain.engine.passwordState
import com.omni.domain.entity.Status
import com.omni.domain.repositories.AuthenticationDataRepository


class RegisterWithEmailUseCase(
    private val _authState: MutableLiveData<AuthState>,
    private val _emailState: MutableLiveData<Status>,
    private val _passwordState: MutableLiveData<Status>,
    private val _confirmPasswordState: MutableLiveData<Boolean>,
    private val _errorMsg: MutableLiveData<String>,
    private val authRepository: AuthenticationDataRepository
) {

    suspend operator fun invoke(email: String, password: String, confirmedPassword: String) {
        val emailState = email.emailState()
        val passwordState = password.passwordState()
        val isMatchedPassword = confirmedPassword.isNotBlank() &&
                confirmedPassword.isMatch(password)

        _emailState.postValue(emailState)
        _passwordState.postValue(passwordState)
        _confirmPasswordState.postValue(isMatchedPassword)
        _errorMsg.postValue(null)

        try {
            emailState.takeIf {
                it == Status.VALID && passwordState == Status.VALID && isMatchedPassword
            }?.also {
                _authState.postValue(AuthState.LOADING)
            }?.let {
                authRepository.register(email, password)
            }?.let {
                _authState.postValue(AuthState.AUTHENTICATED)
                authRepository.sendVerificationEMail(it)
                authRepository.logout()
            } ?: kotlin.run {
                _authState.postValue(AuthState.FAILED)
            }
        } catch (ex: FirebaseAuthException) {
            _authState.postValue(AuthState.FAILED)
            _errorMsg.postValue(ex.message)
        }
    }

}