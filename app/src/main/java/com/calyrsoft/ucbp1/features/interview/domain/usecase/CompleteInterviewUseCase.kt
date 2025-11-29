package com.calyrsoft.ucbp1.features.interview.domain.usecase

import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import com.calyrsoft.ucbp1.features.interview.domain.repository.InterviewRepository

class CompleteInterviewUseCase(
    private val repository: InterviewRepository
) {
    suspend operator fun invoke(sessionId: String): Result<Map<SoftSkill, Int>> {
        return repository.completeInterview(sessionId)
    }
}

