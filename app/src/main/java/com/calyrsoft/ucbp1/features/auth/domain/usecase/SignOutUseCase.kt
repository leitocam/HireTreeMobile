package com.calyrsoft.ucbp1.features.auth.domain.usecase

import com.calyrsoft.ucbp1.features.auth.domain.repository.AuthRepository

class SignOutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() {
        repository.signOut()
    }
}

