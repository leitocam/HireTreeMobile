package com.calyrsoft.ucbp1.features.interview.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import com.calyrsoft.ucbp1.ui.theme.*
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewResultsScreen(
    scores: Map<SoftSkill, Int>,
    onNavigateHome: () -> Unit,
    onGenerateCertificate: () -> Unit = {},
    viewModel: InterviewViewModel = koinViewModel()
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    if (scores.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = iOSBlue,
                strokeWidth = 3.dp
            )
        }
        return
    }

    val averageScore = scores.values.average().toInt()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Resultados",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = iOSSystemGray6
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Success Header
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(800)) +
                        scaleIn(animationSpec = tween(800))
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(GradientStart, GradientEnd)
                                )
                            )
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.White
                                )
                            }

                            Text(
                                text = "¡Felicitaciones!",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "Entrevista completada exitosamente",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Average Score Card
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(800, delayMillis = 200)) +
                        slideInVertically(animationSpec = tween(800, delayMillis = 200)) { it }
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Puntuación Promedio",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = iOSLabelLight
                        )

                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            getScoreColor(averageScore).copy(alpha = 0.2f),
                                            getScoreColor(averageScore).copy(alpha = 0.1f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$averageScore",
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.Bold,
                                color = getScoreColor(averageScore)
                            )
                        }

                        StarRating(score = averageScore)

                        Text(
                            text = getScoreLabel(averageScore),
                            style = MaterialTheme.typography.titleMedium,
                            color = getScoreColor(averageScore),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Individual Skills
            Text(
                text = "Evaluación por Habilidad",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = iOSLabelLight,
                modifier = Modifier.padding(top = 8.dp)
            )

            scores.entries.forEachIndexed { index, (skill, score) ->
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(600, delayMillis = 400 + (index * 100))) +
                            slideInHorizontally(animationSpec = tween(600, delayMillis = 400 + (index * 100))) { it }
                ) {
                    SkillResultCard(skill = skill, score = score)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(800, delayMillis = 1000))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onGenerateCertificate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = iOSGreen
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Generar Certificado",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    OutlinedButton(
                        onClick = onNavigateHome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = iOSBlue
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 2.dp
                        )
                    ) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Volver al Inicio",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SkillResultCard(skill: SoftSkill, score: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(getSkillColor(skill).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            getSkillIcon(skill),
                            contentDescription = null,
                            tint = getSkillColor(skill),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = skill.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = iOSLabelLight
                    )
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(getScoreColor(score).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = getScoreColor(score)
                    )
                }
            }

            LinearProgressIndicator(
                progress = { score / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = getScoreColor(score),
                trackColor = iOSSystemGray5,
            )

            Text(
                text = getRecommendation(skill, score),
                style = MaterialTheme.typography.bodyMedium,
                color = iOSSystemGray,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
            )
        }
    }
}

@Composable
private fun StarRating(score: Int) {
    val starCount = scoreToStars(score)
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= starCount) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = null,
                tint = if (i <= starCount) iOSYellow else iOSSystemGray3,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

private fun scoreToStars(score: Int): Int {
    return when {
        score >= 90 -> 5
        score >= 75 -> 4
        score >= 60 -> 3
        score >= 40 -> 2
        else -> 1
    }
}

private fun getScoreColor(score: Int): Color {
    return when {
        score >= 80 -> iOSGreen
        score >= 60 -> iOSBlue
        score >= 40 -> iOSOrange
        else -> iOSRed
    }
}

private fun getScoreLabel(score: Int): String {
    return when {
        score >= 90 -> "Excelente"
        score >= 80 -> "Muy Bueno"
        score >= 70 -> "Bueno"
        score >= 60 -> "Satisfactorio"
        score >= 50 -> "Regular"
        else -> "Necesita Mejorar"
    }
}

private fun getSkillIcon(skill: SoftSkill): androidx.compose.ui.graphics.vector.ImageVector {
    return when (skill) {
        SoftSkill.COMMUNICATION -> Icons.Default.Face
        SoftSkill.LEADERSHIP -> Icons.Default.Star
        SoftSkill.TEAMWORK -> Icons.Default.Favorite
        SoftSkill.PROBLEM_SOLVING -> Icons.Default.Build
        SoftSkill.ADAPTABILITY -> Icons.Default.AccountCircle
    }
}

private fun getSkillColor(skill: SoftSkill): Color {
    return when (skill) {
        SoftSkill.COMMUNICATION -> iOSBlue
        SoftSkill.LEADERSHIP -> iOSOrange
        SoftSkill.TEAMWORK -> iOSPink
        SoftSkill.PROBLEM_SOLVING -> iOSPurple
        SoftSkill.ADAPTABILITY -> iOSTeal
    }
}

private fun getRecommendation(skill: SoftSkill, score: Int): String {
    val level = when {
        score >= 80 -> "excelente"
        score >= 60 -> "buen"
        else -> "mejorable"
    }

    val baseRecommendation = when (skill) {
        SoftSkill.COMMUNICATION -> if (score >= 80)
            "Excelente comunicación. Continúa compartiendo tus ideas con confianza."
        else
            "Practica expresar tus ideas de forma clara y concisa en diferentes contextos."

        SoftSkill.LEADERSHIP -> if (score >= 80)
            "Gran capacidad de liderazgo. Considera mentorizar a otros."
        else
            "Busca oportunidades para liderar pequeños proyectos o iniciativas."

        SoftSkill.TEAMWORK -> if (score >= 80)
            "Excelente trabajo en equipo. Tu colaboración es valiosa."
        else
            "Participa más activamente en discusiones de equipo y escucha todas las opiniones."

        SoftSkill.PROBLEM_SOLVING -> if (score >= 80)
            "Destacas en resolución de problemas. Aplica estos skills en desafíos complejos."
        else
            "Practica desglosar problemas complejos en partes más manejables."

        SoftSkill.ADAPTABILITY -> if (score >= 80)
            "Gran adaptabilidad. Sigues siendo flexible ante los cambios."
        else
            "Intenta salir de tu zona de confort y probar nuevas herramientas o métodos."
    }

    return baseRecommendation
}

