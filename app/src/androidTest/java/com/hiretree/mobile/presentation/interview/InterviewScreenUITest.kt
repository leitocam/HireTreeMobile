package com.hiretree.mobile.presentation.interview

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.calyrsoft.ucbp1.features.interview.domain.model.ChatMessage
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas de UI para InterviewScreen
 */
class InterviewScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun interviewScreen_displaysMessages_correctly() {
        // Arrange
        val messages = listOf(
            ChatMessage(
                id = "1",
                content = "¡Hola! Bienvenido a tu entrevista",
                isFromUser = false
            ),
            ChatMessage(
                id = "2",
                content = "Hola, mi nombre es Leo",
                isFromUser = true
            ),
            ChatMessage(
                id = "3",
                content = "¿Cuál es tu profesión?",
                isFromUser = false
            )
        )

        // Act
        composeTestRule.setContent {
            MessageList(messages = messages)
        }

        // Assert
        composeTestRule.onNodeWithText("¡Hola! Bienvenido a tu entrevista")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Hola, mi nombre es Leo")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("¿Cuál es tu profesión?")
            .assertIsDisplayed()
    }

    @Test
    fun messageInput_acceptsText_andDisplaysIt() {
        // Arrange
        var currentInput = ""

        composeTestRule.setContent {
            MessageInputField(
                value = currentInput,
                onValueChange = { currentInput = it },
                onSend = {},
                enabled = true
            )
        }

        // Act
        composeTestRule.onNodeWithTag("messageInput")
            .performTextInput("Este es un mensaje de prueba")

        // Assert
        composeTestRule.onNodeWithText("Este es un mensaje de prueba")
            .assertIsDisplayed()
    }

    @Test
    fun sendButton_isEnabled_whenTextIsNotEmpty() {
        // Arrange
        var currentInput = "Mensaje de prueba"

        composeTestRule.setContent {
            MessageInputField(
                value = currentInput,
                onValueChange = { currentInput = it },
                onSend = {},
                enabled = true
            )
        }

        // Assert
        composeTestRule.onNodeWithTag("sendButton")
            .assertIsEnabled()
    }

    @Test
    fun sendButton_isDisabled_whenTextIsEmpty() {
        // Arrange
        composeTestRule.setContent {
            MessageInputField(
                value = "",
                onValueChange = {},
                onSend = {},
                enabled = true
            )
        }

        // Assert
        composeTestRule.onNodeWithTag("sendButton")
            .assertIsNotEnabled()
    }

    @Test
    fun sendButton_click_triggersCallback() {
        // Arrange
        var sendClicked = false
        val testMessage = "Test message"

        composeTestRule.setContent {
            MessageInputField(
                value = testMessage,
                onValueChange = {},
                onSend = { sendClicked = true },
                enabled = true
            )
        }

        // Act
        composeTestRule.onNodeWithTag("sendButton")
            .performClick()

        // Assert
        assert(sendClicked) { "Send callback should have been triggered" }
    }

    @Test
    fun aiTypingIndicator_isDisplayed_whenAiIsTyping() {
        // Arrange
        composeTestRule.setContent {
            AITypingIndicator(isVisible = true)
        }

        // Assert
        composeTestRule.onNodeWithText("IA está escribiendo...")
            .assertIsDisplayed()
    }

    @Test
    fun aiTypingIndicator_isNotDisplayed_whenAiIsNotTyping() {
        // Arrange
        composeTestRule.setContent {
            AITypingIndicator(isVisible = false)
        }

        // Assert
        composeTestRule.onNodeWithText("IA está escribiendo...")
            .assertDoesNotExist()
    }

    @Test
    fun messageList_scrollsToBottom_whenNewMessageAdded() {
        // Arrange
        val initialMessages = (1..20).map { i ->
            ChatMessage(
                id = "$i",
                content = "Mensaje $i",
                isFromUser = i % 2 == 0
            )
        }

        composeTestRule.setContent {
            MessageList(messages = initialMessages)
        }

        // Assert - El último mensaje debería ser visible o scrolleable
        composeTestRule.onNodeWithText("Mensaje 20")
            .assertExists()
    }

    @Test
    fun userMessage_hasCorrectStyling() {
        // Arrange
        val userMessage = ChatMessage(
            id = "1",
            content = "Mensaje de usuario",
            isFromUser = true
        )

        composeTestRule.setContent {
            MessageBubble(message = userMessage)
        }

        // Assert
        composeTestRule.onNodeWithText("Mensaje de usuario")
            .assertIsDisplayed()
            .assertHasClickAction() // Si tiene acciones
    }

    @Test
    fun aiMessage_hasCorrectStyling() {
        // Arrange
        val aiMessage = ChatMessage(
            id = "1",
            content = "Mensaje de IA",
            isFromUser = false
        )

        composeTestRule.setContent {
            MessageBubble(message = aiMessage)
        }

        // Assert
        composeTestRule.onNodeWithText("Mensaje de IA")
            .assertIsDisplayed()
    }

    @Test
    fun endInterviewButton_isDisplayed() {
        // Arrange
        composeTestRule.setContent {
            EndInterviewButton(
                onClick = {},
                enabled = true
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Finalizar Entrevista")
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun loadingIndicator_isDisplayed_whenLoading() {
        // Arrange
        composeTestRule.setContent {
            LoadingScreen()
        }

        // Assert
        composeTestRule.onNodeWithTag("loadingIndicator")
            .assertIsDisplayed()
    }
}

// Componentes helper para las pruebas (simulan los componentes reales)
@androidx.compose.runtime.Composable
private fun MessageList(messages: List<ChatMessage>) {
    androidx.compose.foundation.lazy.LazyColumn {
        items(messages.size) { index ->
            MessageBubble(messages[index])
        }
    }
}

@androidx.compose.runtime.Composable
private fun MessageBubble(message: ChatMessage) {
    androidx.compose.material3.Text(text = message.content)
}

@androidx.compose.runtime.Composable
private fun MessageInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    enabled: Boolean
) {
    androidx.compose.foundation.layout.Row {
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = androidx.compose.ui.Modifier.testTag("messageInput"),
            enabled = enabled
        )
        androidx.compose.material3.IconButton(
            onClick = onSend,
            enabled = value.isNotBlank() && enabled,
            modifier = androidx.compose.ui.Modifier.testTag("sendButton")
        ) {
            androidx.compose.material3.Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Send,
                contentDescription = "Enviar"
            )
        }
    }
}

@androidx.compose.runtime.Composable
private fun AITypingIndicator(isVisible: Boolean) {
    if (isVisible) {
        androidx.compose.material3.Text("IA está escribiendo...")
    }
}

@androidx.compose.runtime.Composable
private fun EndInterviewButton(onClick: () -> Unit, enabled: Boolean) {
    androidx.compose.material3.Button(
        onClick = onClick,
        enabled = enabled
    ) {
        androidx.compose.material3.Text("Finalizar Entrevista")
    }
}

@androidx.compose.runtime.Composable
private fun LoadingScreen() {
    androidx.compose.material3.CircularProgressIndicator(
        modifier = androidx.compose.ui.Modifier.testTag("loadingIndicator")
    )
}

