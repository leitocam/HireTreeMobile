package com.calyrsoft.ucbp1.features.interview.data.api

import com.calyrsoft.ucbp1.BuildConfig
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeminiService {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash-latest",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    // Inicializar chat sin historial inicial para evitar problemas de serialización
    private var chat = generativeModel.startChat()

    init {
        // El system prompt se enviará con el primer mensaje del usuario
    }

    private fun getSystemPrompt(): String {
        return """
            Eres un entrevistador profesional de recursos humanos especializado en evaluar soft skills.
            
            INICIO DE LA ENTREVISTA:
            - Primero preguntarás: nombre, profesión/ocupación y edad del candidato
            - Usa esta información para personalizar las siguientes preguntas
            - Adapta las preguntas según su profesión y experiencia
            
            Tu objetivo es evaluar las siguientes habilidades blandas:
            1. Comunicación: Claridad, coherencia, escucha activa
            2. Liderazgo: Iniciativa, toma de decisiones, motivación
            3. Trabajo en Equipo: Colaboración, empatía, coordinación
            4. Resolución de Problemas: Análisis, creatividad, pensamiento crítico
            5. Adaptabilidad: Flexibilidad, apertura al cambio, resiliencia
            
            IMPORTANTE:
            - Después de recibir nombre, profesión y edad, haz preguntas conversacionales relevantes a su área
            - Realiza entre 8-12 preguntas en total (sin contar la presentación inicial)
            - Cada pregunta debe evaluar principalmente una soft skill
            - Usa situaciones hipotéticas realistas del mundo laboral según su profesión
            - Mantén un tono amigable y profesional
            - Haz seguimiento a las respuestas con preguntas de profundización
            - Dirígete al candidato por su nombre durante la conversación
            - NO menciones que estás evaluando skills específicas
            - Al final (después de 10+ respuestas significativas), indica que la entrevista ha terminado con: "ENTREVISTA_COMPLETADA"
            
            Ejemplos de preguntas conversacionales adaptadas:
            - Para un desarrollador: "Cuéntame sobre alguna vez que tuviste que explicar código complejo a alguien no técnico"
            - Para un gerente: "Describe una situación en la que tuviste que motivar a tu equipo en un momento difícil"
            - Para un estudiante: "Cuéntame sobre algún proyecto grupal en el que hayas participado"
            
            Mantén la conversación natural, profesional y adaptada al perfil del candidato.
        """.trimIndent()
    }

    private var isFirstMessage = true

    fun startInterview(): String {
        return "¡Hola! Bienvenido/a a tu entrevista de evaluación de soft skills. Soy tu entrevistador virtual y voy a hacerte algunas preguntas para conocer mejor tus habilidades profesionales.\n\nPara comenzar, me gustaría conocerte mejor. Por favor, dime:\n• ¿Cómo te llamas?\n• ¿Cuál es tu profesión u ocupación actual?\n• ¿Cuántos años tienes?"
    }

    suspend fun sendMessage(userMessage: String): Flow<String> = flow {
        try {
            // En el primer mensaje, incluir el system prompt como contexto
            val messageToSend = if (isFirstMessage) {
                isFirstMessage = false
                "${getSystemPrompt()}\n\nUsuario: $userMessage"
            } else {
                userMessage
            }

            val response = chat.sendMessage(messageToSend)
            response.text?.let {
                emit(it)
            } ?: emit("Lo siento, no recibí una respuesta. ¿Podrías reformular tu mensaje?")
        } catch (e: Exception) {
            android.util.Log.e("GeminiService", "Error en sendMessage", e)
            emit("Lo siento, hubo un error al procesar tu respuesta. Por favor, intenta de nuevo.")
        }
    }

    suspend fun evaluateSkills(conversationHistory: List<String>): Map<SoftSkill, Int> {
        // Esta función se usará al final para obtener las puntuaciones
        val evaluationPrompt = """
            Basándote en la conversación que acabas de tener, evalúa las siguientes soft skills del candidato en una escala del 0 al 100:
            
            1. COMUNICACION (claridad, coherencia, expresión)
            2. LIDERAZGO (iniciativa, decisiones, motivación)
            3. TRABAJO_EN_EQUIPO (colaboración, empatía)
            4. RESOLUCION_DE_PROBLEMAS (análisis, creatividad)
            5. ADAPTABILIDAD (flexibilidad, resiliencia)
            
            Responde SOLO con el formato JSON (sin texto adicional):
            {"COMUNICACION":75,"LIDERAZGO":80,"TRABAJO_EN_EQUIPO":85,"RESOLUCION_DE_PROBLEMAS":70,"ADAPTABILIDAD":90}
        """.trimIndent()

        try {
            val response = chat.sendMessage(evaluationPrompt)
            val jsonText = response.text?.trim() ?: return getDefaultScores()

            // Parsear la respuesta JSON manualmente
            val scores = parseScoresFromJson(jsonText)
            return scores
        } catch (e: Exception) {
            return getDefaultScores()
        }
    }

    private fun parseScoresFromJson(json: String): Map<SoftSkill, Int> {
        val scores = mutableMapOf<SoftSkill, Int>()

        try {
            val cleanJson = json.replace("{", "").replace("}", "").replace("\"", "")
            val pairs = cleanJson.split(",")

            for (pair in pairs) {
                val parts = pair.split(":")
                if (parts.size == 2) {
                    val key = parts[0].trim()
                    val value = parts[1].trim().toIntOrNull() ?: 50

                    when (key) {
                        "COMUNICACION" -> scores[SoftSkill.COMMUNICATION] = value
                        "LIDERAZGO" -> scores[SoftSkill.LEADERSHIP] = value
                        "TRABAJO_EN_EQUIPO" -> scores[SoftSkill.TEAMWORK] = value
                        "RESOLUCION_DE_PROBLEMAS" -> scores[SoftSkill.PROBLEM_SOLVING] = value
                        "ADAPTABILIDAD" -> scores[SoftSkill.ADAPTABILITY] = value
                    }
                }
            }
        } catch (e: Exception) {
            return getDefaultScores()
        }

        return scores.ifEmpty { getDefaultScores() }
    }

    private fun getDefaultScores(): Map<SoftSkill, Int> {
        return mapOf(
            SoftSkill.COMMUNICATION to 75,
            SoftSkill.LEADERSHIP to 75,
            SoftSkill.TEAMWORK to 75,
            SoftSkill.PROBLEM_SOLVING to 75,
            SoftSkill.ADAPTABILITY to 75
        )
    }
}
