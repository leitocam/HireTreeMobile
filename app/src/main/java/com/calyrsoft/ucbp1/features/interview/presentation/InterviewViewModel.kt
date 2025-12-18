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
import kotlinx.coroutines.flow.catch
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
    val isAiTyping: Boolean = false,
    val showResultsDialog: Boolean = false
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
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            startInterviewUseCase(userId).fold(
                onSuccess = { session ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        sessionId = session.id,
                        messages = session.messages
                    )
                },
                onFailure = { error ->
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
            _uiState.value = _uiState.value.copy(error = "La sesión de entrevista no es válida.")
            return
        }

        viewModelScope.launch {
            val userMessage = ChatMessage(id = UUID.randomUUID().toString(), content = message, isFromUser = true, System.currentTimeMillis())
            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + userMessage,
                currentInput = "",
                isAiTyping = true
            )

            sendMessageUseCase(sessionId, message)
                .catch { e ->
                    Log.e("InterviewViewModel", "Error sending message", e)
                    _uiState.value = _uiState.value.copy(error = "Error de comunicación con el simulador.", isAiTyping = false)
                }
                .collect { responseChunk ->
                    val lastMessage = _uiState.value.messages.lastOrNull()
                    if (lastMessage != null && !lastMessage.isFromUser) {
                        val updatedMessage = lastMessage.copy(content = lastMessage.content + responseChunk)
                        _uiState.value = _uiState.value.copy(messages = _uiState.value.messages.dropLast(1) + updatedMessage)
                    } else {
                        val aiMessage = ChatMessage(id = UUID.randomUUID().toString(), content = responseChunk, isFromUser = false, System.currentTimeMillis())
                        _uiState.value = _uiState.value.copy(messages = _uiState.value.messages + aiMessage)
                    }

                    if (responseChunk.contains("ENTRENVISTA_COMPLETADA", ignoreCase = true)) {
                        _uiState.value = _uiState.value.copy(isAiTyping = false)
                        completeInterview()
                    } else {
                         _uiState.value = _uiState.value.copy(isAiTyping = false)
                    }
                }
        }
    }

    fun updateInput(input: String) {
        _uiState.value = _uiState.value.copy(currentInput = input)
    }

    private fun completeInterview() {
        val sessionId = _uiState.value.sessionId

        if (sessionId == null) {
            Log.w("InterviewViewModel", "No sessionId - using mock scores for testing")
            // Para testing: generar scores de ejemplo
            val mockScores = mapOf(
                SoftSkill.COMMUNICATION to 85,
                SoftSkill.LEADERSHIP to 78,
                SoftSkill.TEAMWORK to 92,
                SoftSkill.PROBLEM_SOLVING to 80,
                SoftSkill.ADAPTABILITY to 88
            )
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isCompleted = true,
                scores = mockScores,
                showResultsDialog = true
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            completeInterviewUseCase(sessionId).fold(
                onSuccess = { scores ->
                    Log.d("InterviewViewModel", "Interview completed successfully with scores: $scores")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isCompleted = true,
                        scores = scores,
                        showResultsDialog = true // Mostrar diálogo
                    )
                },
                onFailure = { error ->
                    Log.e("InterviewViewModel", "Error completing interview: ${error.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al completar entrevista"
                    )
                }
            )
        }
    }

    fun forceCompleteInterview() {
        Log.d("InterviewViewModel", "forceCompleteInterview called - sessionId: ${_uiState.value.sessionId}")
        completeInterview()
    }

    fun dismissResultsDialog() {
        _uiState.value = _uiState.value.copy(showResultsDialog = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
