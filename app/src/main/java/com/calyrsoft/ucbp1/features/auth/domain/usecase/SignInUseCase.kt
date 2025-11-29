package com.calyrsoft.ucbp1.features.auth.domain.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.AuthResult
import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.AuthRepository

class SignInUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): AuthResult<User> {
        // Validations
        if (email.isBlank()) {
            return AuthResult.Error("El email es requerido")
        }
        if (password.isBlank()) {
            return AuthResult.Error("La contraseña es requerida")
        }

        return repository.signIn(email, password)
    }
}

