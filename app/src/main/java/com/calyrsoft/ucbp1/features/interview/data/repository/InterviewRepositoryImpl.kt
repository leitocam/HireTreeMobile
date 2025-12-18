package com.calyrsoft.ucbp1.features.interview.data.repository

import android.util.Log
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
            // This now calls the simulator, which is instantaneous.
            val welcomeMessage = geminiService.startNewInterview()

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

            // NO LONGER SAVING TO FIRESTORE AT THE START FOR INSTANT SPEED
            Log.d("InterviewRepository", "Started session in memory. Firestore save is skipped.")

            Result.success(session)
        } catch (e: Exception) {
            Log.e("InterviewRepository", "Error in startInterview", e)
            Result.failure(e)
        }
    }

    override fun sendMessage(sessionId: String, message: String): Flow<String> {
        return geminiService.sendMessage(message)
    }

    override suspend fun saveMessage(sessionId: String, message: ChatMessage): Result<Unit> {
        // The logic to save individual messages can remain, as it does not block the UI.
        // It will silently fail if offline, which is acceptable for the guest mode.
        return try {
            try {
                val sessionDoc = sessionsCollection.document(sessionId)
                val session = sessionDoc.get().await().toObject(InterviewSession::class.java)

                if (session != null) {
                    val updatedMessages = session.messages + message
                    sessionDoc.update("messages", updatedMessages).await()
                }
            } catch (e: Exception) {
                Log.w("InterviewRepository", "Could not save message to Firestore", e)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun completeInterview(sessionId: String): Result<Map<SoftSkill, Int>> {
        return try {
            val scores = geminiService.evaluateSkills()
            // The logic to save the final score can also remain.
            try {
                sessionsCollection.document(sessionId).update(
                    mapOf(
                        "isCompleted" to true,
                        "endTime" to System.currentTimeMillis(),
                        "evaluations" to scores.mapKeys { it.key.name }
                    )
                ).await()
            } catch (e: Exception) {
                Log.w("InterviewRepository", "Could not save completion to Firestore", e)
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
