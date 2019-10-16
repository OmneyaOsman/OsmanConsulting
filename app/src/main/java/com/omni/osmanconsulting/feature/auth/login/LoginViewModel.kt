package com.omni.osmanconsulting.feature.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omni.domain.AuthState
import com.omni.domain.entity.Status
import com.omni.domain.repositories.AuthenticationDataRepository
import com.omni.domain.repositories.repository
import com.omni.domain.useCase.LoginWithEmailUseCase
import kotlinx.coroutines.*

class LoginViewModel(
    private val _authState: MutableLiveData<AuthState> = MutableLiveData(),
    private val _emailState: MutableLiveData<Status> = MutableLiveData(),
    private val _passwordState: MutableLiveData<Status> = MutableLiveData(),
    private val _errorMsg: MutableLiveData<String> = MutableLiveData(),
    private val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
    private val authRepository: AuthenticationDataRepository = repository,
    private val loginWithEmail: LoginWithEmailUseCase = LoginWithEmailUseCase(
        _authState, _emailState, _passwordState, _errorMsg, authRepository
    )
) :
    ViewModel() {
    val authState: LiveData<AuthState>
        get() = _authState
    val passwordState: LiveData<Status>
        get() = _passwordState
    val emailState: LiveData<Status>
        get() = _emailState
    val errorMsg: LiveData<String>
        get() = _errorMsg


    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            loginWithEmail(email, password)
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