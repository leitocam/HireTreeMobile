package com.calyrsoft.ucbp1.features.interview.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class SoftSkill(val displayName: String, val description: String) {
    COMMUNICATION(
        displayName = "Comunicación",
        description = "Capacidad de expresarse claramente y escuchar activamente"
    ),
    LEADERSHIP(
        displayName = "Liderazgo",
        description = "Habilidad para guiar, motivar e inspirar a otros"
    ),
    TEAMWORK(
        displayName = "Trabajo en Equipo",
        description = "Capacidad de colaborar efectivamente con otros"
    ),
    PROBLEM_SOLVING(
        displayName = "Resolución de Problemas",
        description = "Habilidad para analizar situaciones y encontrar soluciones"
    ),
    ADAPTABILITY(
        displayName = "Adaptabilidad",
        description = "Flexibilidad para ajustarse a cambios y nuevas situaciones"
    )
}
