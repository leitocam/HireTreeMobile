package com.calyrsoft.ucbp1.features.interview.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.interview.domain.model.ChatMessage
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import com.calyrsoft.ucbp1.features.interview.domain.usecase.CompleteInterviewUseCase
import com.calyrsoft.ucbp1.features.interview.domain.usecase.SendMessageUseCase
import com.calyrsoft.ucbp1.features.interview.domain.usecase.StartInterviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class InterviewUiState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val currentInput: String = "",
    val error: String? = null,
    val sessionId: String? = null,
    val isCompleted: Boolean = false,
    val scores: Map<SoftSkill, Int>? = null,
    val isAiTyping: Boolean = false
)

class InterviewViewModel(
    private val startInterviewUseCase: StartInterviewUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val completeInterviewUseCase: CompleteInterviewUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InterviewUiState())
    val uiState: StateFlow<InterviewUiState> = _uiState.asStateFlow()

    fun startInterview(userId: String) {
        viewModelScope.launch {
            Log.d("InterviewViewModel", "startInterview called with userId: $userId")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = startInterviewUseCase(userId)
            result.fold(
                onSuccess = { session ->
                    Log.d("InterviewViewModel", "Interview started successfully. Session: ${session.id}, Messages: ${session.messages.size}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        sessionId = session.id,
                        messages = session.messages
                    )
                },
                onFailure = { error ->
                    Log.e("InterviewViewModel", "Error starting interview", error)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al iniciar entrevista"
                    )
                }
            )
        }
    }

    fun sendMessage(message: String) {
        val sessionId = _uiState.value.sessionId

        if (sessionId == null) {
            Log.e("InterviewViewModel", "sendMessage called but sessionId is NULL!")
            Log.e("InterviewViewModel", "Current state: isLoading=${_uiState.value.isLoading}, messages=${_uiState.value.messages.size}")
            _uiState.value = _uiState.value.copy(
                error = "La entrevista no se ha iniciado correctamente. Por favor, vuelve e intenta de nuevo."
            )
            return
        }

        Log.d("InterviewViewModel", "sendMessage called with message: $message, sessionId: $sessionId")

        viewModelScope.launch {
            // Agregar mensaje del usuario
            val userMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                content = message,
                isFromUser = true,
                timestamp = System.currentTimeMillis()
            )

            val currentMessages = _uiState.value.messages + userMessage
            _uiState.value = _uiState.value.copy(
                messages = currentMessages,
                currentInput = "",
                isAiTyping = true
            )

            Log.d("InterviewViewModel", "User message added. Total messages: ${currentMessages.size}")

            // Enviar a Gemini y recibir respuesta
            try {
                sendMessageUseCase(sessionId, message).collect { aiResponse ->
                    Log.d("InterviewViewModel", "AI response received: $aiResponse")

                    val aiMessage = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        content = aiResponse,
                        isFromUser = false,
                        timestamp = System.currentTimeMillis()
                    )

                    val updatedMessages = _uiState.value.messages + aiMessage
                    _uiState.value = _uiState.value.copy(
                        messages = updatedMessages,
                        isAiTyping = false
                    )

                    // Verificar si la entrevista está completa
                    if (aiResponse.contains("ENTREVISTA_COMPLETADA", ignoreCase = true)) {
                        Log.d("InterviewViewModel", "Interview completed detected")
                        completeInterview()
                    }
                }
            } catch (e: Exception) {
                Log.e("InterviewViewModel", "Error sending message", e)
                _uiState.value = _uiState.value.copy(
                    error = "Error al enviar mensaje: ${e.message}",
                    isAiTyping = false
                )
            }
        }
    }

    fun updateInput(input: String) {
        _uiState.value = _uiState.value.copy(currentInput = input)
    }

    private fun completeInterview() {
        val sessionId = _uiState.value.sessionId ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = completeInterviewUseCase(sessionId)
            result.fold(
                onSuccess = { scores ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isCompleted = true,
                        scores = scores
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al completar entrevista"
                    )
                }
            )
        }
    }

    fun forceCompleteInterview() {
        // Función para finalizar manualmente si el usuario lo desea
        val messageCount = _uiState.value.messages.count { it.isFromUser }

        if (messageCount >= 5) { // Al menos 5 respuestas
            completeInterview()
        } else {
            _uiState.value = _uiState.value.copy(
                error = "Necesitas responder al menos 5 preguntas para completar la entrevista"
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

