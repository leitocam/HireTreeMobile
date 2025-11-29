package com.calyrsoft.ucbp1.features.interview.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewResultsScreen(
    scores: Map<SoftSkill, Int>,
    onNavigateHome: () -> Unit,
    onGenerateCertificate: () -> Unit = {},
    viewModel: InterviewViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val actualScores = uiState.scores ?: scores

    if (actualScores.isEmpty()) {
        // Mostrar loading si no hay scores
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultados de la Entrevista") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Success icon
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Completado",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "¡Entrevista Completada!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Aquí están tus resultados de evaluación de soft skills",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Score cards
            actualScores.forEach { (skill, score) ->
                SkillScoreCard(skill = skill, score = score)
            }

            // Promedio
            val averageScore = actualScores.values.average().toInt()
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Puntuación Promedio",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$averageScore/100",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = getPerformanceLevel(averageScore),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons
            Button(
                onClick = onGenerateCertificate,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = false // TODO: Habilitar en Fase 6
            ) {
                Text("Generar Certificado (Próximamente)")
            }

            OutlinedButton(
                onClick = onNavigateHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Volver al Inicio")
            }
        }
    }
}

@Composable
fun SkillScoreCard(skill: SoftSkill, score: Int) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) score / 100f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "progress"
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = skill.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = skill.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "$score/100",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = getScoreColor(score)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = getScoreColor(score),
            )
        }
    }
}

@Composable
fun getScoreColor(score: Int): androidx.compose.ui.graphics.Color {
    return when {
        score >= 80 -> MaterialTheme.colorScheme.primary
        score >= 60 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.error
    }
}

fun getPerformanceLevel(score: Int): String {
    return when {
        score >= 90 -> "Excelente desempeño"
        score >= 80 -> "Muy buen desempeño"
        score >= 70 -> "Buen desempeño"
        score >= 60 -> "Desempeño adecuado"
        else -> "Necesita mejorar"
    }
}

