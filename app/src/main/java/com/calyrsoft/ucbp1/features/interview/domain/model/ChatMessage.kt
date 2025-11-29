package com.calyrsoft.ucbp1.features.interview.domain.model

data class ChatMessage(
    val id: String = "",
    val content: String = "",
    val isFromUser: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

