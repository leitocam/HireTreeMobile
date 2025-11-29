package com.calyrsoft.ucbp1.features.interview.domain.usecase

import com.calyrsoft.ucbp1.features.interview.domain.repository.InterviewRepository
import kotlinx.coroutines.flow.Flow

class SendMessageUseCase(
    private val repository: InterviewRepository
) {
    suspend operator fun invoke(sessionId: String, message: String): Flow<String> {
        return repository.sendMessage(sessionId, message)
    }
}

