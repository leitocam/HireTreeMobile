package com.calyrsoft.ucbp1.features.auth.data.repository

import android.util.Log
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

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }

    override suspend fun signUp(email: String, password: String, displayName: String): AuthResult<User> {
        return try {
            Log.d(TAG, "Iniciando registro para: $email")
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return AuthResult.Error("Error al crear usuario")

            Log.d(TAG, "✅ Usuario creado en Firebase Auth: ${firebaseUser.uid}")

            // Update display name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            firebaseUser.updateProfile(profileUpdates).await()

            Log.d(TAG, "✅ Nombre actualizado: $displayName")

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

            Log.d(TAG, "✅ Usuario guardado en Firestore")
            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            Log.d(TAG, "🎉 Registro completado exitosamente")
            Log.d(TAG, "   Email: $email")
            Log.d(TAG, "   UID: ${user.uid}")
            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

            AuthResult.Success(user)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en registro: ${e.message}", e)
            AuthResult.Error(e.message ?: "Error desconocido al registrarse")
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult<User> {
        return try {
            Log.d(TAG, "Iniciando sesión para: $email")
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return AuthResult.Error("Error al iniciar sesión")

            Log.d(TAG, "✅ Autenticación exitosa: ${firebaseUser.uid}")

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

            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            Log.d(TAG, "🎉 Inicio de sesión completado")
            Log.d(TAG, "   Email: ${user.email}")
            Log.d(TAG, "   Nombre: ${user.displayName}")
            Log.d(TAG, "   UID: ${user.uid}")
            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

            AuthResult.Success(user)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en inicio de sesión: ${e.message}", e)
            AuthResult.Error(e.message ?: "Error desconocido al iniciar sesión")
        }
    }

    override suspend fun signOut() {
        Log.d(TAG, "Cerrando sesión...")
        firebaseAuth.signOut()
        Log.d(TAG, "✅ Sesión cerrada")
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

