package com.hiretree.mobile.presentation.interview

import app.cash.turbine.test
import com.calyrsoft.ucbp1.features.interview.domain.model.ChatMessage
import com.calyrsoft.ucbp1.features.interview.domain.model.InterviewSession
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import com.calyrsoft.ucbp1.features.interview.domain.usecase.CompleteInterviewUseCase
import com.calyrsoft.ucbp1.features.interview.domain.usecase.SendMessageUseCase
import com.calyrsoft.ucbp1.features.interview.domain.usecase.StartInterviewUseCase
import com.calyrsoft.ucbp1.features.interview.presentation.InterviewViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para InterviewViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class InterviewViewModelTest {

    private lateinit var viewModel: InterviewViewModel
    private lateinit var startInterviewUseCase: StartInterviewUseCase
    private lateinit var sendMessageUseCase: SendMessageUseCase
    private lateinit var completeInterviewUseCase: CompleteInterviewUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        startInterviewUseCase = mockk()
        sendMessageUseCase = mockk()
        completeInterviewUseCase = mockk()

        viewModel = InterviewViewModel(
            startInterviewUseCase,
            sendMessageUseCase,
            completeInterviewUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `startInterview success updates state correctly`() = runTest {
        // Arrange
        val userId = "test-user-123"
        val mockSession = InterviewSession(
            id = "session-123",
            userId = userId,
            messages = listOf(
                ChatMessage(
                    id = "msg-1",
                    content = "¡Hola! Bienvenido a tu entrevista",
                    isFromUser = false
                )
            )
        )

        coEvery { startInterviewUseCase(userId) } returns Result.success(mockSession)

        // Act
        viewModel.startInterview(userId)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse("Loading should be false", state.isLoading)
        assertNull("Error should be null", state.error)
        assertEquals("Session ID should match", "session-123", state.sessionId)
        assertEquals("Should have 1 message", 1, state.messages.size)
        assertEquals("Message content should match",
            "¡Hola! Bienvenido a tu entrevista",
            state.messages.first().content
        )

        coVerify(exactly = 1) { startInterviewUseCase(userId) }
    }

    @Test
    fun `startInterview failure sets error state`() = runTest {
        // Arrange
        val userId = "test-user-123"
        val errorMessage = "Network error"

        coEvery { startInterviewUseCase(userId) } returns Result.failure(Exception(errorMessage))

        // Act
        viewModel.startInterview(userId)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse("Loading should be false", state.isLoading)
        assertNotNull("Error should not be null", state.error)
        assertTrue("Error should contain message",
            state.error?.contains(errorMessage) == true
        )

        coVerify(exactly = 1) { startInterviewUseCase(userId) }
    }

    @Test
    fun `sendMessage updates input and adds user message`() = runTest {
        // Arrange
        val sessionId = "session-123"
        val userMessage = "Mi nombre es Leo"
        val aiResponse = "¡Encantado de conocerte, Leo!"

        // Setup initial state
        viewModel.startInterview("user-123")
        every { sendMessageUseCase(sessionId, userMessage) } returns flow {
            emit(aiResponse)
        }

        // Act
        viewModel.updateInput(userMessage)
        advanceUntilIdle()

        // Assert input is updated
        assertEquals("Input should match", userMessage, viewModel.uiState.value.currentInput)

        // Act - send message
        viewModel.sendMessage(sessionId)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals("Input should be cleared", "", state.currentInput)
        assertTrue("Should have user message",
            state.messages.any { it.content == userMessage && it.isFromUser }
        )
    }

    @Test
    fun `sendMessage receives AI response and updates messages`() = runTest {
        // Arrange
        val sessionId = "session-123"
        val userMessage = "Hola"
        val aiResponse = "¡Hola! ¿Cómo estás?"

        coEvery { sendMessageUseCase(sessionId, userMessage) } returns flow {
            emit(aiResponse)
        }

        // Act
        viewModel.updateInput(userMessage)
        viewModel.sendMessage(sessionId)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse("AI typing indicator should be off", state.isAiTyping)
        assertTrue("Should have AI response",
            state.messages.any { it.content == aiResponse && !it.isFromUser }
        )

        coVerify(exactly = 1) { sendMessageUseCase(sessionId, userMessage) }
    }

    @Test
    fun `completeInterview success returns scores`() = runTest {
        // Arrange
        val sessionId = "session-123"
        val mockScores = mapOf(
            SoftSkill.COMMUNICATION to 85,
            SoftSkill.LEADERSHIP to 75,
            SoftSkill.TEAMWORK to 90,
            SoftSkill.PROBLEM_SOLVING to 80,
            SoftSkill.ADAPTABILITY to 70
        )

        coEvery { completeInterviewUseCase(sessionId) } returns Result.success(mockScores)

        // Act
        viewModel.completeInterview(sessionId)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue("Interview should be completed", state.isCompleted)
        assertNotNull("Scores should not be null", state.scores)
        assertEquals("Scores should match", mockScores, state.scores)

        coVerify(exactly = 1) { completeInterviewUseCase(sessionId) }
    }

    @Test
    fun `completeInterview failure sets error`() = runTest {
        // Arrange
        val sessionId = "session-123"
        val errorMessage = "Failed to complete interview"

        coEvery { completeInterviewUseCase(sessionId) } returns Result.failure(Exception(errorMessage))

        // Act
        viewModel.completeInterview(sessionId)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse("Interview should not be completed", state.isCompleted)
        assertNotNull("Error should be set", state.error)

        coVerify(exactly = 1) { completeInterviewUseCase(sessionId) }
    }

    @Test
    fun `updateInput updates current input state`() = runTest {
        // Arrange
        val input1 = "Hello"
        val input2 = "World"

        // Act & Assert
        viewModel.updateInput(input1)
        advanceUntilIdle()
        assertEquals(input1, viewModel.uiState.value.currentInput)

        viewModel.updateInput(input2)
        advanceUntilIdle()
        assertEquals(input2, viewModel.uiState.value.currentInput)
    }

    @Test
    fun `clearError clears error state`() = runTest {
        // Arrange - Force an error
        val userId = "test-user"
        coEvery { startInterviewUseCase(userId) } returns Result.failure(Exception("Error"))
        viewModel.startInterview(userId)
        advanceUntilIdle()

        // Verify error is set
        assertNotNull("Error should be set", viewModel.uiState.value.error)

        // Act
        viewModel.clearError()
        advanceUntilIdle()

        // Assert
        assertNull("Error should be cleared", viewModel.uiState.value.error)
    }

    @Test
    fun `AI typing indicator is shown while waiting for response`() = runTest {
        // Arrange
        val sessionId = "session-123"
        val userMessage = "Test"

        coEvery { sendMessageUseCase(sessionId, userMessage) } returns flow {
            kotlinx.coroutines.delay(100) // Simulate delay
            emit("Response")
        }

        // Act
        viewModel.updateInput(userMessage)
        viewModel.sendMessage(sessionId)

        // Check during processing
        advanceTimeBy(50)
        assertTrue("AI should be typing", viewModel.uiState.value.isAiTyping)

        // Wait for completion
        advanceUntilIdle()
        assertFalse("AI should stop typing", viewModel.uiState.value.isAiTyping)
    }
}

