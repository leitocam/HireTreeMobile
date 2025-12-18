package com.calyrsoft.ucbp1.core.config

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await

/**
 * Servicio para gestionar Firebase Remote Config
 * Permite configurar la app sin necesidad de actualizaciones
 */
class RemoteConfigService {

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    companion object {
        private const val TAG = "RemoteConfigService"

        // Keys para Remote Config
        const val KEY_GEMINI_API_KEY = "gemini_api_key"
        const val KEY_GEMINI_MODEL = "gemini_model"
        const val KEY_USE_REAL_AI = "use_real_ai"
        const val KEY_MIN_MESSAGES_TO_COMPLETE = "min_messages_to_complete"
        const val KEY_MAX_QUESTIONS = "max_questions"

        // Valores por defecto
        private const val DEFAULT_GEMINI_MODEL = "gemini-1.5-flash"
        private const val DEFAULT_USE_REAL_AI = false
        private const val DEFAULT_MIN_MESSAGES = 5
        private const val DEFAULT_MAX_QUESTIONS = 7
    }

    init {
        // Configurar Remote Config
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600) // 1 hora en producción
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)

        // Establecer valores por defecto
        val defaults = mapOf(
            KEY_GEMINI_API_KEY to "",
            KEY_GEMINI_MODEL to DEFAULT_GEMINI_MODEL,
            KEY_USE_REAL_AI to DEFAULT_USE_REAL_AI,
            KEY_MIN_MESSAGES_TO_COMPLETE to DEFAULT_MIN_MESSAGES,
            KEY_MAX_QUESTIONS to DEFAULT_MAX_QUESTIONS
        )

        remoteConfig.setDefaultsAsync(defaults)
    }

    /**
     * Obtiene la configuración desde Firebase Remote Config
     * @param fetchFromServer Si es true, obtiene la última configuración del servidor
     */
    suspend fun fetchConfig(fetchFromServer: Boolean = true): Result<Unit> {
        return try {
            if (fetchFromServer) {
                Log.d(TAG, "Obteniendo configuración desde Firebase...")
                remoteConfig.fetchAndActivate().await()
                Log.d(TAG, "✅ Configuración actualizada desde Firebase")
            } else {
                Log.d(TAG, "Usando configuración cacheada")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al obtener configuración: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Obtiene la API Key de Gemini
     */
    fun getGeminiApiKey(): String {
        val apiKey = remoteConfig.getString(KEY_GEMINI_API_KEY)
        Log.d(TAG, "Gemini API Key: ${if (apiKey.isNotEmpty()) "***${apiKey.takeLast(4)}" else "NO CONFIGURADA"}")
        return apiKey
    }

    /**
     * Obtiene el modelo de Gemini a usar
     */
    fun getGeminiModel(): String {
        return remoteConfig.getString(KEY_GEMINI_MODEL).ifEmpty { DEFAULT_GEMINI_MODEL }
    }

    /**
     * Indica si se debe usar IA real o el simulador
     */
    fun shouldUseRealAI(): Boolean {
        val useRealAI = remoteConfig.getBoolean(KEY_USE_REAL_AI)
        val hasApiKey = getGeminiApiKey().isNotEmpty()

        return useRealAI && hasApiKey
    }

    /**
     * Cantidad mínima de mensajes para completar entrevista
     */
    fun getMinMessagesToComplete(): Int {
        return remoteConfig.getLong(KEY_MIN_MESSAGES_TO_COMPLETE).toInt()
    }

    /**
     * Cantidad máxima de preguntas en la entrevista
     */
    fun getMaxQuestions(): Int {
        return remoteConfig.getLong(KEY_MAX_QUESTIONS).toInt()
    }

    /**
     * Obtiene toda la configuración actual
     */
    fun getCurrentConfig(): ConfigData {
        return ConfigData(
            geminiApiKey = getGeminiApiKey(),
            geminiModel = getGeminiModel(),
            useRealAI = shouldUseRealAI(),
            minMessagesToComplete = getMinMessagesToComplete(),
            maxQuestions = getMaxQuestions()
        )
    }

    /**
     * Imprime la configuración actual en los logs
     */
    fun logCurrentConfig() {
        val config = getCurrentConfig()
        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Log.d(TAG, "📋 CONFIGURACIÓN ACTUAL:")
        Log.d(TAG, "   Gemini Model: ${config.geminiModel}")
        Log.d(TAG, "   Use Real AI: ${config.useRealAI}")
        Log.d(TAG, "   API Key: ${if (config.geminiApiKey.isNotEmpty()) "✅ Configurada" else "❌ No configurada"}")
        Log.d(TAG, "   Min Messages: ${config.minMessagesToComplete}")
        Log.d(TAG, "   Max Questions: ${config.maxQuestions}")
        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    }
}

/**
 * Data class para representar la configuración
 */
data class ConfigData(
    val geminiApiKey: String,
    val geminiModel: String,
    val useRealAI: Boolean,
    val minMessagesToComplete: Int,
    val maxQuestions: Int
)

