package com.calyrsoft.ucbp1.features.interview.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.calyrsoft.ucbp1.features.auth.presentation.AuthViewModel
import com.calyrsoft.ucbp1.features.interview.domain.model.ChatMessage
import org.koin.androidx.compose.koinViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewScreen(
    onNavigateBack: () -> Unit,
    onInterviewComplete: (Map<com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill, Int>) -> Unit,
    viewModel: InterviewViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val authState by authViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Iniciar entrevista cuando el usuario esté disponible
    LaunchedEffect(authState.user) {
        Log.d("InterviewScreen", "LaunchedEffect triggered (user changed)")
        Log.d("InterviewScreen", "Auth user: ${authState.user?.uid}")
        Log.d("InterviewScreen", "Current sessionId: ${uiState.sessionId}")

        authState.user?.uid?.let { userId ->
            Log.d("InterviewScreen", "User ID found: $userId")
            if (uiState.sessionId == null) {
                Log.d("InterviewScreen", "Starting interview...")
                viewModel.startInterview(userId)
            } else {
                Log.d("InterviewScreen", "Session already exists: ${uiState.sessionId}")
            }
        } ?: Log.e("InterviewScreen", "User ID is still null, waiting...")
    }

    // Auto-scroll al último mensaje
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(uiState.messages.size - 1)
            }
        }
    }

    // Navegar cuando la entrevista se completa
    LaunchedEffect(uiState.isCompleted) {
        if (uiState.isCompleted && uiState.scores != null) {
            onInterviewComplete(uiState.scores!!)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Entrevista de Soft Skills")
                        if (!uiState.isCompleted) {
                            Text(
                                text = "${uiState.messages.count { it.isFromUser }} respuestas",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                actions = {
                    if (uiState.messages.count { it.isFromUser } >= 5 && !uiState.isCompleted) {
                        TextButton(
                            onClick = { viewModel.forceCompleteInterview() }
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Finalizar",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Finalizar")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Área de mensajes
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(uiState.messages) { message ->
                    MessageBubble(message = message)
                }

                // Indicador de escritura
                if (uiState.isAiTyping) {
                    item {
                        TypingIndicator()
                    }
                }
            }

            // Área de input
            if (!uiState.isCompleted) {
                MessageInput(
                    value = uiState.currentInput,
                    onValueChange = {
                        Log.d("InterviewScreen", "Input changed: $it")
                        viewModel.updateInput(it)
                    },
                    onSend = {
                        Log.d("InterviewScreen", "Send button clicked. Input: ${uiState.currentInput}")
                        if (uiState.currentInput.isNotBlank()) {
                            Log.d("InterviewScreen", "Sending message...")
                            viewModel.sendMessage(uiState.currentInput)
                        } else {
                            Log.w("InterviewScreen", "Input is blank, not sending")
                        }
                    },
                    enabled = !uiState.isLoading && !uiState.isAiTyping
                )
            }

            // Mensaje de error
            if (uiState.error != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.error ?: "",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // Indicador de carga inicial
            if (uiState.isLoading && uiState.messages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator()
                        Text("Iniciando entrevista...")
                    }
                }
            }

            // Indicador cuando no hay sesión pero tampoco está cargando
            if (!uiState.isLoading && uiState.sessionId == null && uiState.messages.isEmpty() && authState.user == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(24.dp)
                    ) {
                        CircularProgressIndicator()
                        Text(
                            "Cargando sesión de usuario...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            "Si esto tarda mucho, vuelve atrás e intenta de nuevo",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isFromUser) 16.dp else 4.dp,
                bottomEnd = if (message.isFromUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isFromUser)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(12.dp),
                color = if (message.isFromUser)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 80.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    enabled: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe tu respuesta...") },
                enabled = enabled,
                maxLines = 4,
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (value.isNotBlank()) {
                            onSend()
                        }
                    }
                )
            )

            IconButton(
                onClick = {
                    if (enabled && value.isNotBlank()) {
                        onSend()
                    }
                },
                enabled = enabled && value.isNotBlank(),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    tint = if (enabled && value.isNotBlank())
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            }
        }
    }
}

