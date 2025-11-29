package com.calyrsoft.ucbp1.features.interview.domain.usecase

import com.calyrsoft.ucbp1.features.interview.domain.model.InterviewSession
import com.calyrsoft.ucbp1.features.interview.domain.repository.InterviewRepository

class StartInterviewUseCase(
    private val repository: InterviewRepository
) {
    suspend operator fun invoke(userId: String): Result<InterviewSession> {
        if (userId.isBlank()) {
            return Result.failure(Exception("Usuario inválido"))
        }
        return repository.startInterview(userId)
    }
}

