package com.calyrsoft.ucbp1.features.auth.domain.model

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

