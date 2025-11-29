package com.calyrsoft.ucbp1.features.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.auth.domain.model.AuthResult
import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.AuthRepository
import com.calyrsoft.ucbp1.features.auth.domain.usecase.SignInUseCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.SignOutUseCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        // Cargar usuario actual si existe
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            Log.d("AuthViewModel", "Loading current user...")

            // Primero verificar si hay usuario logueado actualmente
            val isLoggedIn = authRepository.isUserLoggedIn()
            Log.d("AuthViewModel", "Is user logged in: $isLoggedIn")

            // Luego escuchar cambios en el estado de autenticación
            authRepository.getCurrentUser().collect { user ->
                Log.d("AuthViewModel", "Current user changed: ${user?.uid}")
                if (user != null) {
                    _uiState.value = _uiState.value.copy(
                        user = user,
                        isAuthenticated = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        user = null,
                        isAuthenticated = false
                    )
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = signInUseCase(email, password)) {
                is AuthResult.Success -> {
                    _uiState.value = AuthUiState(
                        isLoading = false,
                        user = result.data,
                        isAuthenticated = true
                    )
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                AuthResult.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun signUp(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = signUpUseCase(email, password, displayName)) {
                is AuthResult.Success -> {
                    _uiState.value = AuthUiState(
                        isLoading = false,
                        user = result.data,
                        isAuthenticated = true
                    )
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                AuthResult.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
            _uiState.value = AuthUiState()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

