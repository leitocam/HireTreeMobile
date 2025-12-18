package com.hiretree.mobile.data.repository

import com.calyrsoft.ucbp1.features.interview.data.api.GeminiService
import com.calyrsoft.ucbp1.features.interview.data.repository.InterviewRepositoryImpl
import com.calyrsoft.ucbp1.features.interview.domain.model.ChatMessage
import com.calyrsoft.ucbp1.features.interview.domain.model.InterviewSession
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Pruebas de integración para InterviewRepository
 */
class InterviewRepositoryTest {

    private lateinit var repository: InterviewRepositoryImpl
    private lateinit var mockGeminiService: GeminiService
    private lateinit var mockFirestore: FirebaseFirestore

    @Before
    fun setup() {
        mockGeminiService = mockk(relaxed = true)
        mockFirestore = mockk(relaxed = true)

        repository = InterviewRepositoryImpl(
            geminiService = mockGeminiService,
            firestore = mockFirestore
        )
    }

    @Test
    fun `startInterview creates new session with welcome message`() = runTest {
        // Arrange
        val userId = "test-user-123"
        val welcomeMessage = "¡Hola! Bienvenido a tu entrevista"

        coEvery { mockGeminiService.startInterview() } returns welcomeMessage

        // Act
        val result = repository.startInterview(userId)

        // Assert
        assertTrue("Result should be success", result.isSuccess)

        result.onSuccess { session ->
            assertEquals("User ID should match", userId, session.userId)
            assertFalse("Session ID should not be empty", session.id.isEmpty())
            assertEquals("Should have 1 message", 1, session.messages.size)
            assertEquals("Message should be welcome message",
                welcomeMessage,
                session.messages.first().content
            )
            assertFalse("Message should be from AI", session.messages.first().isFromUser)
            assertFalse("Interview should not be completed", session.isCompleted)
        }

        coVerify(exactly = 1) { mockGeminiService.startInterview() }
    }

    @Test
    fun `startInterview returns failure when GeminiService fails`() = runTest {
        // Arrange
        val userId = "test-user-123"
        val errorMessage = "API Error"

        coEvery { mockGeminiService.startInterview() } throws Exception(errorMessage)

        // Act
        val result = repository.startInterview(userId)

        // Assert
        assertTrue("Result should be failure", result.isFailure)
        result.onFailure { error ->
            assertEquals("Error message should match", errorMessage, error.message)
        }
    }

    @Test
    fun `sendMessage returns flow with AI response`() = runTest {
        // Arrange
        val sessionId = "session-123"
        val userMessage = "Hola, ¿cómo estás?"
        val aiResponse = "¡Hola! Estoy bien, gracias por preguntar."

        coEvery { mockGeminiService.sendMessage(userMessage) } returns flow {
            emit(aiResponse)
        }

        // Act
        val responseFlow = repository.sendMessage(sessionId, userMessage)
        val responses = responseFlow.toList()

        // Assert
        assertEquals("Should have 1 response", 1, responses.size)
        assertEquals("Response should match", aiResponse, responses.first())

        coVerify(exactly = 1) { mockGeminiService.sendMessage(userMessage) }
    }

    @Test
    fun `saveMessage saves to Firestore successfully`() = runTest {
        // Arrange
        val sessionId = "session-123"
        val message = ChatMessage(
            id = "msg-1",
            content = "Test message",
            isFromUser = true
        )

        // Act
        val result = repository.saveMessage(sessionId, message)

        // Assert
        assertTrue("Result should be success", result.isSuccess)
    }

    @Test
    fun `completeInterview returns skill scores`() = runTest {
        // Arrange
        val sessionId = "session-123"
        val expectedScores = mapOf(
            SoftSkill.COMMUNICATION to 85,
            SoftSkill.LEADERSHIP to 75,
            SoftSkill.TEAMWORK to 90,
            SoftSkill.PROBLEM_SOLVING to 80,
            SoftSkill.ADAPTABILITY to 70
        )

        // Mock conversation history for evaluation
        val conversationHistory = listOf(
            "User: Mi nombre es Leo",
            "AI: ¡Hola Leo!",
            "User: Soy desarrollador",
            "AI: Cuéntame más"
        )

        coEvery {
            mockGeminiService.evaluateSkills(any())
        } returns expectedScores

        // Act
        val result = repository.completeInterview(sessionId)

        // Assert
        assertTrue("Result should be success", result.isSuccess)
        result.onSuccess { scores ->
            assertEquals("Scores should match", expectedScores, scores)
            assertTrue("Should have all 5 skills", scores.size == 5)
            assertTrue("Should contain COMMUNICATION", scores.containsKey(SoftSkill.COMMUNICATION))
            assertTrue("Should contain LEADERSHIP", scores.containsKey(SoftSkill.LEADERSHIP))
            assertTrue("Should contain TEAMWORK", scores.containsKey(SoftSkill.TEAMWORK))
            assertTrue("Should contain PROBLEM_SOLVING", scores.containsKey(SoftSkill.PROBLEM_SOLVING))
            assertTrue("Should contain ADAPTABILITY", scores.containsKey(SoftSkill.ADAPTABILITY))
        }
    }

    @Test
    fun `completeInterview returns failure when evaluation fails`() = runTest {
        // Arrange
        val sessionId = "session-123"
        val errorMessage = "Evaluation failed"

        coEvery {
            mockGeminiService.evaluateSkills(any())
        } throws Exception(errorMessage)

        // Act
        val result = repository.completeInterview(sessionId)

        // Assert
        assertTrue("Result should be failure", result.isFailure)
    }

    @Test
    fun `multiple messages can be sent in sequence`() = runTest {
        // Arrange
        val sessionId = "session-123"
        val messages = listOf(
            "Hola" to "¡Hola!",
            "¿Cómo estás?" to "Bien, gracias",
            "Cuéntame sobre el trabajo" to "Claro, te explico..."
        )

        messages.forEach { (userMsg, aiResponse) ->
            coEvery { mockGeminiService.sendMessage(userMsg) } returns flow {
                emit(aiResponse)
            }
        }

        // Act & Assert
        messages.forEach { (userMsg, expectedResponse) ->
            val responseFlow = repository.sendMessage(sessionId, userMsg)
            val responses = responseFlow.toList()

            assertEquals("Should get correct response", expectedResponse, responses.first())
        }

        coVerify(exactly = messages.size) { mockGeminiService.sendMessage(any()) }
    }

    @Test
    fun `session IDs are unique`() = runTest {
        // Arrange
        val userId = "test-user"
        val welcomeMessage = "Welcome"

        coEvery { mockGeminiService.startInterview() } returns welcomeMessage

        // Act
        val session1 = repository.startInterview(userId).getOrNull()
        val session2 = repository.startInterview(userId).getOrNull()

        // Assert
        assertNotNull("Session 1 should not be null", session1)
        assertNotNull("Session 2 should not be null", session2)
        assertNotEquals("Session IDs should be different", session1?.id, session2?.id)
    }

    @Test
    fun `interview session has correct timestamp`() = runTest {
        // Arrange
        val userId = "test-user"
        val beforeTime = System.currentTimeMillis()

        coEvery { mockGeminiService.startInterview() } returns "Welcome"

        // Act
        Thread.sleep(10) // Small delay to ensure time difference
        val result = repository.startInterview(userId)
        val afterTime = System.currentTimeMillis()

        // Assert
        result.onSuccess { session ->
            assertTrue("Start time should be after beforeTime",
                session.startTime >= beforeTime
            )
            assertTrue("Start time should be before afterTime",
                session.startTime <= afterTime
            )
            assertNull("End time should be null for new session", session.endTime)
        }
    }
}

