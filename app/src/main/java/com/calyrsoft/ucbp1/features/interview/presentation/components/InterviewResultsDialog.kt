package com.calyrsoft.ucbp1.features.interview.presentation.components

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import com.calyrsoft.ucbp1.features.interview.presentation.utils.ResultsImageGenerator
import com.calyrsoft.ucbp1.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun InterviewResultsDialog(
    scores: Map<SoftSkill, Int>,
    onDismiss: () -> Unit,
    onNavigateToResults: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isSaving by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = iOSSystemGray6
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botón de cerrar (X) en la esquina superior derecha
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = iOSSystemGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Header con gradiente
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(iOSGreen, iOSTeal)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "¡Entrevista Finalizada!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Preview de resultados
                ResultsPreview(
                    scores = scores,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Opciones de guardado
                Text(
                    text = "Guardar resumen como:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = iOSLabelLight
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón Guardar como Imagen
                    OutlinedButton(
                        onClick = {
                            if (!isSaving) {
                                scope.launch {
                                    isSaving = true
                                    saveAsImage(context, scores)
                                    isSaving = false
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = iOSBlue
                        ),
                        enabled = !isSaving
                    ) {
                        Icon(
                            Icons.Default.Image,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Imagen", fontWeight = FontWeight.SemiBold)
                    }

                    // Botón Guardar como PDF
                    OutlinedButton(
                        onClick = {
                            if (!isSaving) {
                                scope.launch {
                                    isSaving = true
                                    saveAsPDF(context, scores)
                                    isSaving = false
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = iOSRed
                        ),
                        enabled = !isSaving
                    ) {
                        Icon(
                            Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("PDF", fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón Ver Detalles
                Button(
                    onClick = {
                        onDismiss()
                        onNavigateToResults()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = iOSBlue
                    ),
                    enabled = !isSaving
                ) {
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Ver Detalles Completos",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (isSaving) {
                    Spacer(modifier = Modifier.height(12.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = iOSBlue,
                        strokeWidth = 2.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultsPreview(
    scores: Map<SoftSkill, Int>,
    modifier: Modifier = Modifier
) {
    val averageScore = scores.values.average().toInt()

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Score promedio
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(getScoreColor(averageScore).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$averageScore",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = getScoreColor(averageScore)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Puntuación Promedio",
                style = MaterialTheme.typography.titleMedium,
                color = iOSSystemGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de habilidades
            scores.forEach { (skill, score) ->
                SkillScoreRow(skill = skill, score = score)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SkillScoreRow(
    skill: SoftSkill,
    score: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                getSkillIcon(skill),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = getSkillColor(skill)
            )
            Text(
                text = skill.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = iOSLabelLight
            )
        }

        Text(
            text = "$score",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = getScoreColor(score)
        )
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

private suspend fun saveAsImage(context: Context, scores: Map<SoftSkill, Int>) {
    withContext(Dispatchers.IO) {
        try {
            // Crear bitmap del resumen usando el generador
            val bitmap = ResultsImageGenerator.generateResultsImage(context, scores)

            // Guardar en galería
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "HireTree_Resultados_$timestamp.png"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/HireTree")
                }

                val uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )

                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "✅ Imagen guardada en Galería/HireTree", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                // Android 9 y anteriores
                val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val hireTreeDir = File(picturesDir, "HireTree")
                if (!hireTreeDir.exists()) {
                    hireTreeDir.mkdirs()
                }

                val file = File(hireTreeDir, fileName)
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }

                // Notificar a la galería
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DATA, file.absolutePath)
                }
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "✅ Imagen guardada en Galería/HireTree", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "❌ Error al guardar imagen: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

private suspend fun saveAsPDF(context: Context, scores: Map<SoftSkill, Int>) {
    withContext(Dispatchers.IO) {
        try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "HireTree_Resultados_$timestamp.pdf"

            // Por ahora, guardar como imagen y notificar
            // En una implementación completa, usar una librería como iTextPDF
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "📄 Función PDF próximamente. Usa 'Imagen' por ahora.", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "❌ Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

