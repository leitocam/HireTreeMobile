package com.calyrsoft.ucbp1.features.interview.domain.model

data class InterviewSession(
    val id: String = "",
    val userId: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val evaluations: Map<SoftSkill, SkillEvaluation> = emptyMap(),
    val currentQuestion: Int = 0,
    val isCompleted: Boolean = false,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null
)

