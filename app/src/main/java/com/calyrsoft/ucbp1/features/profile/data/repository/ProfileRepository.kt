package com.calyrsoft.ucbp1.features.profile.data.repository

import com.calyrsoft.ucbp1.features.login.domain.model.Email
import com.calyrsoft.ucbp1.features.profile.domain.model.ProfileModel
import com.calyrsoft.ucbp1.features.profile.domain.model.UrlPath
import com.calyrsoft.ucbp1.features.profile.domain.model.vo.Cellphone
import com.calyrsoft.ucbp1.features.profile.domain.model.vo.Name
import com.calyrsoft.ucbp1.features.profile.domain.model.vo.Summary
import com.calyrsoft.ucbp1.features.profile.domain.repository.IProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): IProfileRepository {

    override suspend fun fetchData(): Result<ProfileModel> {
        return try {
            // Obtener usuario actual de Firebase Auth
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("No hay usuario autenticado"))

            // Obtener datos adicionales de Firestore
            val userDoc = firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .await()

            val displayName = userDoc.getString("displayName") ?: currentUser.displayName ?: "Usuario"
            val email = currentUser.email ?: "sin-email@ejemplo.com"

            // Generar avatar automático con UI Avatars si no tiene foto
            val photoUrl = if (currentUser.photoUrl != null) {
                currentUser.photoUrl.toString()
            } else {
                "https://ui-avatars.com/api/?name=${displayName.replace(" ", "+")}&size=200&background=random"
            }

            Result.success(
                ProfileModel(
                    name = Name.create(displayName),
                    email = Email.create(email),
                    cellphone = Cellphone(userDoc.getString("phone") ?: "No disponible"),
                    pathUrl = UrlPath(photoUrl),
                    summary = Summary.create(
                        userDoc.getString("bio") ?: "Miembro de Hire Tree"
                    )
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}