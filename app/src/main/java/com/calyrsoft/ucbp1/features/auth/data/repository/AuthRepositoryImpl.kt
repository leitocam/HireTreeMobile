package com.calyrsoft.ucbp1.features.auth.data.repository

import com.calyrsoft.ucbp1.features.auth.domain.model.AuthResult
import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun signUp(email: String, password: String, displayName: String): AuthResult<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return AuthResult.Error("Error al crear usuario")

            // Update display name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            firebaseUser.updateProfile(profileUpdates).await()

            // Create user document in Firestore
            val user = User(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: email,
                displayName = displayName,
                createdAt = System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(user.uid)
                .set(user)
                .await()

            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error desconocido al registrarse")
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return AuthResult.Error("Error al iniciar sesión")

            // Get user from Firestore
            val userDoc = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()

            val user = userDoc.toObject(User::class.java)
                ?: User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: email,
                    displayName = firebaseUser.displayName ?: ""
                )

            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error desconocido al iniciar sesión")
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                val user = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: ""
                )
                trySend(user)
            } else {
                trySend(null)
            }
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}

