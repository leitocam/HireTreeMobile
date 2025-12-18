package com.hiretree.mobile.data.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

/**
 * Gestor de configuración remota de Firebase
 * Permite actualizar valores de la app sin necesidad de actualizar la versión
 */
class RemoteConfigManager {

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        // Configurar intervalo de fetch (1 hora en producción)
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600 // 1 hora
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Establecer valores por defecto
        remoteConfig.setDefaultsAsync(
            mapOf(
                KEY_MIN_QUESTIONS to DEFAULT_MIN_QUESTIONS,
                KEY_MAX_QUESTIONS to DEFAULT_MAX_QUESTIONS,
                KEY_ENABLE_CERTIFICATES to true,
                KEY_GEMINI_MODEL to DEFAULT_GEMINI_MODEL,
                KEY_WELCOME_MESSAGE to DEFAULT_WELCOME_MESSAGE,
                KEY_APP_VERSION_REQUIRED to DEFAULT_APP_VERSION
            )
        )
    }

    /**
     * Obtiene y activa la configuración desde Firebase
     * @return true si se obtuvieron nuevos valores, false si se usaron valores en caché
     */
    suspend fun fetchConfig(): Boolean {
        return try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: Exception) {
            // En caso de error, usar valores por defecto/caché
            false
        }
    }

    /**
     * Número mínimo de preguntas en una entrevista
     */
    fun getMinQuestions(): Int = remoteConfig.getLong(KEY_MIN_QUESTIONS).toInt()

    /**
     * Número máximo de preguntas en una entrevista
     */
    fun getMaxQuestions(): Int = remoteConfig.getLong(KEY_MAX_QUESTIONS).toInt()

    /**
     * Indica si la generación de certificados está habilitada
     */
    fun isCertificatesEnabled(): Boolean = remoteConfig.getBoolean(KEY_ENABLE_CERTIFICATES)

    /**
     * Nombre del modelo de Gemini a utilizar
     */
    fun getGeminiModel(): String = remoteConfig.getString(KEY_GEMINI_MODEL)

    /**
     * Mensaje de bienvenida en la pantalla principal
     */
    fun getWelcomeMessage(): String = remoteConfig.getString(KEY_WELCOME_MESSAGE)

    /**
     * Versión mínima requerida de la app
     */
    fun getAppVersionRequired(): String = remoteConfig.getString(KEY_APP_VERSION_REQUIRED)

    companion object {
        // Claves de configuración
        private const val KEY_MIN_QUESTIONS = "min_interview_questions"
        private const val KEY_MAX_QUESTIONS = "max_interview_questions"
        private const val KEY_ENABLE_CERTIFICATES = "enable_certificates"
        private const val KEY_GEMINI_MODEL = "gemini_model_name"
        private const val KEY_WELCOME_MESSAGE = "welcome_message"
        private const val KEY_APP_VERSION_REQUIRED = "app_version_required"

        // Valores por defecto
        private const val DEFAULT_MIN_QUESTIONS = 8
        private const val DEFAULT_MAX_QUESTIONS = 12
        private const val DEFAULT_GEMINI_MODEL = "gemini-2.0-flash-exp"
        private const val DEFAULT_WELCOME_MESSAGE = "¡Bienvenido a HireTree Mobile!"
        private const val DEFAULT_APP_VERSION = "1.0"
    }
}

