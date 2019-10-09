package com.omni.osmanconsulting.feature.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthException
import com.omni.domain.AuthenticationDataRepository
import com.omni.domain.authenticationDataRepository
import com.omni.osmanconsulting.feature.auth.AuthState
import kotlinx.coroutines.*

class LoginViewModel(
    private val _authState: MutableLiveData<AuthState> = MutableLiveData(),
    private val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
    private val authRepository: AuthenticationDataRepository = authenticationDataRepository
) :
    ViewModel() {
    val authState: LiveData<AuthState>
        get() = _authState


    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.postValue(AuthState.LOADING)
                authRepository.login(email, password)?.let {
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
    }


    fun signOutUser() {
        authRepository.logout()
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}