package com.omni.osmanconsulting.feature.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omni.domain.AuthState
import com.omni.domain.AuthenticationDataRepository
import com.omni.domain.engine.Status
import com.omni.domain.repository
import com.omni.domain.useCase.LoginWithEmailUseCase
import kotlinx.coroutines.*

class LoginViewModel(
    private val _authState: MutableLiveData<AuthState> = MutableLiveData(),
    private val _emailState: MutableLiveData<Status> = MutableLiveData(),
    private val _passwordState: MutableLiveData<Status> = MutableLiveData(),
    private val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
    private val authRepository: AuthenticationDataRepository = repository,
    private val loginWithEmail: LoginWithEmailUseCase = LoginWithEmailUseCase(
        _authState, _emailState, _passwordState, authRepository
    )
) :
    ViewModel() {
    val authState: LiveData<AuthState>
        get() = _authState

    val passwordState: LiveData<Status>
        get() = _passwordState
    val emailState: LiveData<Status>
        get() = _emailState


    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            loginWithEmail(email, password)
        }
    }

//    fun signIn(email: String, password: String) {
//        viewModelScope.launch {
//            try {
//                _authState.postValue(AuthState.LOADING)
//                authRepository.login(email, password)?.let {
//                    if (it.isEmailVerified)
//                        _authState.postValue(AuthState.AUTHENTICATED)
//                    else
//                        _authState.postValue(AuthState.NOT_VERIFIED)
//                } ?: kotlin.run {
//                    _authState.postValue(AuthState.FAILED)
//                }
//            } catch (ex: FirebaseAuthException) {
//                _authState.postValue(AuthState.FAILED)
//            }
//        }
//    }


    fun signOutUser() {
        authRepository.logout()
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}