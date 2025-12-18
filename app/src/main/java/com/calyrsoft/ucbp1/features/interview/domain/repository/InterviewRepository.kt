package com.calyrsoft.ucbp1.features.interview.domain.repository

import com.calyrsoft.ucbp1.features.interview.domain.model.ChatMessage
import com.calyrsoft.ucbp1.features.interview.domain.model.InterviewSession
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import kotlinx.coroutines.flow.Flow

interface InterviewRepository {
    suspend fun startInterview(userId: String): Result<InterviewSession>
    fun sendMessage(sessionId: String, message: String): Flow<String>
    suspend fun saveMessage(sessionId: String, message: ChatMessage): Result<Unit>
    suspend fun completeInterview(sessionId: String): Result<Map<SoftSkill, Int>>
    suspend fun getCurrentSession(userId: String): Flow<InterviewSession?>
}
