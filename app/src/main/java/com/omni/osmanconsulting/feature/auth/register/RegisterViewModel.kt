package com.omni.osmanconsulting.feature.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omni.domain.AuthState
import com.omni.domain.entity.Status
import com.omni.domain.repositories.AuthenticationDataRepository
import com.omni.domain.repositories.repository
import com.omni.domain.useCase.RegisterWithEmailUseCase
import kotlinx.coroutines.*

class RegisterViewModel(
    private val _authState: MutableLiveData<AuthState> = MutableLiveData(),
    private val _emailState: MutableLiveData<Status> = MutableLiveData(),
    private val _passwordState: MutableLiveData<Status> = MutableLiveData(),
    private val _confirmPasswordState: MutableLiveData<Boolean> = MutableLiveData(),
    private val _errorMsg: MutableLiveData<String> = MutableLiveData(),
    private val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
    private val authRepository: AuthenticationDataRepository = repository,
    private val registerWithEmailUseCase: RegisterWithEmailUseCase = RegisterWithEmailUseCase(
        _authState, _emailState, _passwordState, _confirmPasswordState, _errorMsg, authRepository
    )
) :
    ViewModel() {
    val authState: LiveData<AuthState>
        get() = _authState
    val passwordState: LiveData<Status>
        get() = _passwordState
    val emailState: LiveData<Status>
        get() = _emailState
    val confirmPasswordState: LiveData<Boolean>
        get() = _confirmPasswordState
    val errorMsg: LiveData<String>
        get() = _errorMsg


    fun registerWithEmail(email: String, password: String, confirmedPassword: String) {
        viewModelScope.launch {
            registerWithEmailUseCase(email, password, confirmedPassword)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}