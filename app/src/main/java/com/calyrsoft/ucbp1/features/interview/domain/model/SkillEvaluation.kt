package com.calyrsoft.ucbp1.features.interview.domain.model

data class SkillEvaluation(
    val skill: SoftSkill,
    val score: Int = 0, // 0-100
    val feedback: String = ""
)

