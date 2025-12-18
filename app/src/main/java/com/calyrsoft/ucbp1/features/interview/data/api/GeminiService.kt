package com.calyrsoft.ucbp1.features.interview.data.api

import android.util.Log
import com.calyrsoft.ucbp1.core.config.RemoteConfigService
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Servicio que maneja la comunicación con el simulador de entrevista
 * IA REAL DESACTIVADA - Solo funciona con simulador
 */
class GeminiService(
    private val remoteConfig: RemoteConfigService
) {


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
        Log.d("GeminiService", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Log.d("GeminiService", "🚀 Iniciando nueva entrevista")
        Log.d("GeminiService", "   Modo: SIMULADOR (IA desactivada)")
        Log.d("GeminiService", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        questionIndex = 0
        return startSimulatedInterview()
    }

    fun sendMessage(userMessage: String): Flow<String> = flow {
        sendMessageToSimulator(userMessage).collect { emit(it) }
    }

    suspend fun evaluateSkills(): Map<SoftSkill, Int> {
        return evaluateSkillsSimulated()
    }

    // ========================================
    // MÉTODOS DEL SIMULADOR
    // ========================================

    private fun startSimulatedInterview(): String {
        Log.d("GeminiService", "📝 Usando SIMULADOR de entrevista")
        questionIndex = 0
        return "¡Hola! Bienvenido a la entrevista simulada. Para comenzar, por favor, dime tu nombre, profesión y edad."
    }

    private fun sendMessageToSimulator(userMessage: String): Flow<String> = flow {
        Log.d("GeminiService", "💬 Simulador procesando mensaje: ${userMessage.take(50)}...")
        delay(1200) // Simular delay de red

        val maxQuestions = remoteConfig.getMaxQuestions()

        if (questionIndex < simulatedQuestions.size && questionIndex < maxQuestions) {
            Log.d("GeminiService", "📤 Enviando pregunta ${questionIndex + 1}/$maxQuestions")
            emit(simulatedQuestions[questionIndex])
            questionIndex++
        } else {
            Log.d("GeminiService", "✅ Fin de la entrevista simulada")
            emit("Gracias, eso es todo por ahora. ENTRENVISTA_COMPLETADA")
        }
    }

    private suspend fun evaluateSkillsSimulated(): Map<SoftSkill, Int> {
        Log.d("GeminiService", "📊 Generando evaluación simulada...")
        delay(800)

        // Generar scores aleatorios pero realistas
        return mapOf(
            SoftSkill.COMMUNICATION to (75..95).random(),
            SoftSkill.LEADERSHIP to (70..90).random(),
            SoftSkill.TEAMWORK to (80..95).random(),
            SoftSkill.PROBLEM_SOLVING to (75..90).random(),
            SoftSkill.ADAPTABILITY to (80..95).random()
        ).also {
            Log.d("GeminiService", "✅ Evaluación generada: $it")
        }
    }
}
