package com.calyrsoft.ucbp1.features.auth.domain.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.AuthResult
import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.AuthRepository

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, displayName: String): AuthResult<User> {
        // Validations
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return AuthResult.Error("Email inválido")
        }
        if (password.length < 6) {
            return AuthResult.Error("La contraseña debe tener al menos 6 caracteres")
        }
        if (displayName.isBlank()) {
            return AuthResult.Error("El nombre es requerido")
        }

        return repository.signUp(email, password, displayName)
    }
}

