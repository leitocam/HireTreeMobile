package com.calyrsoft.ucbp1.features.interview.presentation

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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewScreen(
    viewModel: InterviewViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    // Scroll to the bottom when a new message appears
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Entrevista con IA") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                actions = {
                    // Botón para finalizar manualmente
                    TextButton(onClick = { viewModel.forceCompleteInterview() }) {
                        Text("Finalizar")
                    }
                }
            )
        },
        bottomBar = {
            MessageInput(viewModel)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading && uiState.messages.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.messages) { message ->
                        ChatMessageBubble(message)
                    }
                    if (uiState.isAiTyping) {
                        item {
                            ChatMessageBubble(isTyping = true)
                        }
                    }
                }
            }

            if (uiState.error != null) {
                Snackbar(modifier = Modifier.padding(16.dp)) {
                    Text(uiState.error!!)
                }
            }
        }
    }
}

@Composable
fun MessageInput(viewModel: InterviewViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(tonalElevation = 4.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.currentInput,
                onValueChange = { viewModel.updateInput(it) },
                placeholder = { Text("Escribe tu respuesta...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (uiState.currentInput.isNotBlank()) {
                            viewModel.sendMessage(uiState.currentInput)
                        }
                    }
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (uiState.currentInput.isNotBlank()) {
                        viewModel.sendMessage(uiState.currentInput)
                    }
                },
                enabled = uiState.currentInput.isNotBlank() && !uiState.isAiTyping
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    tint = if (uiState.currentInput.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun ChatMessageBubble(message: com.calyrsoft.ucbp1.features.interview.domain.model.ChatMessage? = null, isTyping: Boolean = false) {
    val bubbleColor =
        if (message?.isFromUser == true) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant

    val alignment = if (message?.isFromUser == true) Alignment.CenterEnd else Alignment.CenterStart

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .align(alignment)
                .widthIn(max = 300.dp),
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (isTyping) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Escribiendo...", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                Text(
                    text = message?.content ?: "",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
