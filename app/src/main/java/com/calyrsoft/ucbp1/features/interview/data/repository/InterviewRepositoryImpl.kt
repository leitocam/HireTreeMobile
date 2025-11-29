package com.calyrsoft.ucbp1.features.interview.data.repository

import com.calyrsoft.ucbp1.features.interview.data.api.GeminiService
import com.calyrsoft.ucbp1.features.interview.domain.model.ChatMessage
import com.calyrsoft.ucbp1.features.interview.domain.model.InterviewSession
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import com.calyrsoft.ucbp1.features.interview.domain.repository.InterviewRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class InterviewRepositoryImpl(
    private val geminiService: GeminiService,
    private val firestore: FirebaseFirestore
) : InterviewRepository {

    private val sessionsCollection = firestore.collection("interview_sessions")

    override suspend fun startInterview(userId: String): Result<InterviewSession> {
        return try {
            val sessionId = UUID.randomUUID().toString()
            val welcomeMessage = geminiService.startInterview()

            val initialMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                content = welcomeMessage,
                isFromUser = false,
                timestamp = System.currentTimeMillis()
            )

            val session = InterviewSession(
                id = sessionId,
                userId = userId,
                messages = listOf(initialMessage),
                startTime = System.currentTimeMillis()
            )

            // TODO: Save to Firestore cuando las reglas de seguridad estén configuradas
            // Por ahora trabajamos solo en memoria
            try {
                sessionsCollection.document(sessionId)
                    .set(session)
                    .await()
            } catch (e: Exception) {
                // Ignorar error de Firestore por ahora
                android.util.Log.w("InterviewRepository", "Could not save to Firestore (permissions), continuing in memory mode", e)
            }

            Result.success(session)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendMessage(sessionId: String, message: String): Flow<String> {
        return geminiService.sendMessage(message)
    }

    override suspend fun saveMessage(sessionId: String, message: ChatMessage): Result<Unit> {
        return try {
            // Intentar guardar en Firestore pero no fallar si no se puede
            try {
                val sessionDoc = sessionsCollection.document(sessionId)
                val session = sessionDoc.get().await().toObject(InterviewSession::class.java)

                if (session != null) {
                    val updatedMessages = session.messages + message
                    sessionDoc.update("messages", updatedMessages).await()
                }
            } catch (e: Exception) {
                // Ignorar error de Firestore - trabajar en memoria
                android.util.Log.w("InterviewRepository", "Could not save message to Firestore", e)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun completeInterview(sessionId: String): Result<Map<SoftSkill, Int>> {
        return try {
            // Evaluar con Gemini sin necesidad de leer de Firestore
            // El historial está en el chat de Gemini
            val scores = geminiService.evaluateSkills(emptyList())

            // Intentar guardar en Firestore pero no fallar si no se puede
            try {
                val sessionDoc = sessionsCollection.document(sessionId)
                sessionDoc.update(
                    mapOf(
                        "isCompleted" to true,
                        "endTime" to System.currentTimeMillis(),
                        "evaluations" to scores.mapKeys { it.key.name }
                    )
                ).await()
            } catch (e: Exception) {
                android.util.Log.w("InterviewRepository", "Could not save completion to Firestore", e)
            }

            Result.success(scores)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentSession(userId: String): Flow<InterviewSession?> = callbackFlow {
        val listener = sessionsCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("isCompleted", false)
            .orderBy("startTime", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                    return@addSnapshotListener
                }

                val session = snapshot?.documents?.firstOrNull()?.toObject(InterviewSession::class.java)
                trySend(session)
            }

        awaitClose { listener.remove() }
    }
}

