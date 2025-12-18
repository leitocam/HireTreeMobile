package com.calyrsoft.ucbp1.features.interview.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewResultsScreen(
    scores: Map<SoftSkill, Int>,
    onNavigateHome: () -> Unit
) {
    if (scores.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Certificado de Soft Skills") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Completado",
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "¡Felicitaciones!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Has completado exitosamente la evaluación de habilidades blandas. Este es un resumen de tu desempeño:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Skill cards with stars and recommendations
            scores.forEach { (skill, score) ->
                SkillCertificateCard(skill = skill, score = score)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Back to home button
            Button(
                onClick = onNavigateHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Volver al Inicio")
            }
        }
    }
}

@Composable
fun SkillCertificateCard(skill: SoftSkill, score: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = skill.displayName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Stars display
            StarRating(score = score)

            // Recommendation text
            Text(
                text = getRecommendation(skill, score),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StarRating(score: Int) {
    val starCount = scoreToStars(score)
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= starCount) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = null,
                tint = if (i <= starCount) Color(0xFFFFD700) else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

fun scoreToStars(score: Int): Int {
    return when {
        score >= 90 -> 5
        score >= 75 -> 4
        score >= 60 -> 3
        score >= 40 -> 2
        else -> 1
    }
}

fun getRecommendation(skill: SoftSkill, score: Int): String {
    val baseRecommendation = when (skill) {
        SoftSkill.COMMUNICATION -> "Busca oportunidades para presentar tus ideas en público o liderar reuniones."
        SoftSkill.LEADERSHIP -> "Considera tomar la iniciativa en pequeños proyectos o mentorizar a un compañero."
        SoftSkill.TEAMWORK -> "Participa activamente en discusiones de equipo, asegurándote de escuchar todas las opiniones."
        SoftSkill.PROBLEM_SOLVING -> "Practica desglosando problemas complejos en partes más pequeñas antes de buscar soluciones."
        SoftSkill.ADAPTABILITY -> "Intenta exponerte a nuevas herramientas o métodos de trabajo, incluso si al principio te sientes incómodo."
    }

    val performanceLevel = when {
        score >= 90 -> "Tu habilidad es excepcional. ¡Sigue así!"
        score >= 75 -> "Demuestras una gran fortaleza en esta área."
        score >= 60 -> "Tienes una base sólida. Con un poco de práctica, puedes destacar aún más."
        else -> "Esta es un área con oportunidad de crecimiento."
    }

    return "$performanceLevel $baseRecommendation"
}
