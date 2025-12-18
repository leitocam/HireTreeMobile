package com.calyrsoft.ucbp1.features.interview.data.api

import android.util.Log
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * This service is now a 100% offline simulator.
 * It does not connect to any external API or use any AI libraries.
 */
class GeminiService {

    private var questionIndex = 0
    private val simulatedQuestions = listOf(
        "Gracias por la información. Ahora, cuéntame sobre un desafío importante que hayas enfrentado en un proyecto y cómo lo superaste.",
        "Interesante. ¿Puedes describir una situación en la que tuviste que trabajar con alguien con una personalidad muy diferente a la tuya? ¿Cómo lo manejaste?",
        "Entiendo. Ahora, imagina que se te asigna un proyecto con un plazo de entrega muy ajustado. ¿Cuáles serían tus primeros tres pasos?",
        "Muy bien. ¿Qué harías si un miembro de tu equipo no está cumpliendo con su parte del trabajo?",
        "Hablemos de cambios. Describe una situación en la que tuviste que adaptarte rápidamente a un cambio inesperado en un proyecto o en tus responsabilidades.",
        "Ya casi terminamos. ¿Puedes darme un ejemplo de una vez que tuviste que comunicar una idea compleja a una audiencia que no sabía mucho sobre el tema?",
        "Perfecto. Muchas gracias por tu tiempo. Hemos concluido la entrevista. ENTRENVISTA_COMPLETADA"
    )

    fun startNewInterview(): String {
        Log.d("GeminiService (Simulator)", "Starting new SIMULATED interview session...")
        questionIndex = 0
        return "¡Hola! Bienvenido a la entrevista simulada. Para comenzar, por favor, dime tu nombre, profesión y edad."
    }

    fun sendMessage(userMessage: String): Flow<String> = flow {
        // Simulate network delay to feel more realistic
        delay(1200)

        if (questionIndex < simulatedQuestions.size) {
            Log.d("GeminiService (Simulator)", "Sending question number $questionIndex")
            emit(simulatedQuestions[questionIndex])
            questionIndex++
        } else {
            Log.d("GeminiService (Simulator)", "End of simulated questions.")
            emit("Gracias, eso es todo por ahora. ENTRENVISTA_COMPLETADA")
        }
    }

    suspend fun evaluateSkills(): Map<SoftSkill, Int> {
        Log.d("GeminiService (Simulator)", "Returning SIMULATED skills evaluation.")
        delay(800) // Simulate evaluation processing time
        return mapOf(
            SoftSkill.COMMUNICATION to 85,
            SoftSkill.LEADERSHIP to 78,
            SoftSkill.TEAMWORK to 92,
            SoftSkill.PROBLEM_SOLVING to 81,
            SoftSkill.ADAPTABILITY to 88
        )
    }
}
