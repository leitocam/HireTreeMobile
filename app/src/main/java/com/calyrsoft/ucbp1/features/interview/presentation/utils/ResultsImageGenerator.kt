package com.calyrsoft.ucbp1.features.interview.presentation.utils

import android.content.Context
import android.graphics.*
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import java.text.SimpleDateFormat
import java.util.*

object ResultsImageGenerator {

    fun generateResultsImage(context: Context, scores: Map<SoftSkill, Int>): Bitmap {
        val width = 1080
        val height = 1920
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Colores iOS
        val backgroundColor = Color.parseColor("#F2F2F7")
        val primaryColor = Color.parseColor("#007AFF")
        val greenColor = Color.parseColor("#34C759")
        val textColor = Color.parseColor("#000000")
        val grayColor = Color.parseColor("#8E8E93")

        // Dibujar fondo
        canvas.drawColor(backgroundColor)

        // Configurar paints
        val titlePaint = Paint().apply {
            color = Color.WHITE
            textSize = 72f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val subtitlePaint = Paint().apply {
            color = Color.WHITE
            textSize = 48f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val headerPaint = Paint().apply {
            color = textColor
            textSize = 56f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val bodyPaint = Paint().apply {
            color = textColor
            textSize = 42f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            isAntiAlias = true
        }

        val scorePaint = Paint().apply {
            textSize = 120f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val smallTextPaint = Paint().apply {
            color = grayColor
            textSize = 36f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        // 1. Header con gradiente
        val gradientPaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), 400f,
                intArrayOf(primaryColor, greenColor),
                null,
                Shader.TileMode.CLAMP
            )
        }

        val headerRect = RectF(0f, 0f, width.toFloat(), 400f)
        canvas.drawRect(headerRect, gradientPaint)

        // Logo/Título en header
        canvas.drawText("HireTree", width / 2f, 150f, titlePaint)
        canvas.drawText("Resultados de Entrevista", width / 2f, 250f, subtitlePaint)

        val date = SimpleDateFormat("dd MMM yyyy", Locale("es", "ES")).format(Date())
        canvas.drawText(date, width / 2f, 320f, subtitlePaint.apply { textSize = 36f })

        // 2. Score promedio (círculo)
        val averageScore = scores.values.average().toInt()
        val centerY = 650f

        // Círculo de fondo
        val circlePaint = Paint().apply {
            color = getScoreColorInt(averageScore)
            alpha = 30
            isAntiAlias = true
        }
        canvas.drawCircle(width / 2f, centerY, 180f, circlePaint)

        // Score number
        scorePaint.color = getScoreColorInt(averageScore)
        canvas.drawText("$averageScore", width / 2f, centerY + 40f, scorePaint)

        // Label
        canvas.drawText("Puntuación Promedio", width / 2f, centerY + 150f, smallTextPaint)
        canvas.drawText(getScoreLabel(averageScore), width / 2f, centerY + 200f,
            smallTextPaint.apply {
                color = getScoreColorInt(averageScore)
                textSize = 42f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            })

        // 3. Desglose de habilidades
        var currentY = 950f

        headerPaint.apply { textSize = 48f }
        canvas.drawText("Desglose por Habilidad", 80f, currentY, headerPaint)
        currentY += 80f

        scores.forEach { (skill, score) ->
            // Card background
            val cardPaint = Paint().apply {
                color = Color.WHITE
                isAntiAlias = true
            }

            val cardRect = RectF(60f, currentY - 60f, width - 60f, currentY + 80f)
            val cornerRadius = 24f
            canvas.drawRoundRect(cardRect, cornerRadius, cornerRadius, cardPaint)

            // Skill name
            bodyPaint.apply {
                color = textColor
                textAlign = Paint.Align.LEFT
            }
            canvas.drawText(skill.displayName, 120f, currentY, bodyPaint)

            // Score
            val scoreText = "$score"
            bodyPaint.apply {
                color = getScoreColorInt(score)
                textSize = 56f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.RIGHT
            }
            canvas.drawText(scoreText, width - 120f, currentY + 5f, bodyPaint)

            // Progress bar
            val progressY = currentY + 50f
            val progressBarPaint = Paint().apply {
                color = Color.parseColor("#E5E5EA")
                isAntiAlias = true
            }
            val progressRect = RectF(120f, progressY, width - 120f, progressY + 12f)
            canvas.drawRoundRect(progressRect, 6f, 6f, progressBarPaint)

            // Filled progress
            val filledPaint = Paint().apply {
                color = getScoreColorInt(score)
                isAntiAlias = true
            }
            val progress = score / 100f
            val filledWidth = (width - 240f) * progress
            val filledRect = RectF(120f, progressY, 120f + filledWidth, progressY + 12f)
            canvas.drawRoundRect(filledRect, 6f, 6f, filledPaint)

            currentY += 180f
        }

        // 4. Footer
        val footerY = height - 100f
        smallTextPaint.apply {
            color = grayColor
            textSize = 32f
        }
        canvas.drawText("Generado por HireTree Mobile", width / 2f, footerY, smallTextPaint)

        return bitmap
    }

    private fun getScoreColorInt(score: Int): Int {
        return when {
            score >= 80 -> Color.parseColor("#34C759") // Verde
            score >= 60 -> Color.parseColor("#007AFF") // Azul
            score >= 40 -> Color.parseColor("#FF9500") // Naranja
            else -> Color.parseColor("#FF3B30") // Rojo
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
}

