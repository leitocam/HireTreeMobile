package com.calyrsoft.ucbp1.features.auth.domain.repository

import com.calyrsoft.ucbp1.features.auth.domain.model.AuthResult
import com.calyrsoft.ucbp1.features.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(email: String, password: String, displayName: String): AuthResult<User>
    suspend fun signIn(email: String, password: String): AuthResult<User>
    suspend fun signOut()
    fun getCurrentUser(): Flow<User?>
    fun isUserLoggedIn(): Boolean
}
