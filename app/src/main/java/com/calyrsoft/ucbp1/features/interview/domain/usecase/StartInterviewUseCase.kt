package com.calyrsoft.ucbp1.features.interview.domain.usecase

import com.calyrsoft.ucbp1.features.interview.domain.model.InterviewSession
import com.calyrsoft.ucbp1.features.interview.domain.repository.InterviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StartInterviewUseCase(
    private val repository: InterviewRepository
) {
    suspend operator fun invoke(userId: String): Result<InterviewSession> {
        if (userId.isBlank()) {
            return Result.failure(Exception("Usuario inválido"))
        }
        // Ensure this network call runs on an IO thread
        return withContext(Dispatchers.IO) {
            repository.startInterview(userId)
        }
    }
}
