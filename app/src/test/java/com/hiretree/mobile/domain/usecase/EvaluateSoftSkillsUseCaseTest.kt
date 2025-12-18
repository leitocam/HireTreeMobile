package com.hiretree.mobile.domain.usecase

import com.calyrsoft.ucbp1.features.interview.domain.model.ChatMessage
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para la evaluación de soft skills
 */
class EvaluateSoftSkillsUseCaseTest {

    private lateinit var useCase: EvaluateSoftSkillsUseCase

    @Before
    fun setup() {
        useCase = EvaluateSoftSkillsUseCase()
    }

    @Test
    fun `evaluate response with communication keywords returns high communication score`() = runTest {
        // Arrange
        val messages = listOf(
            ChatMessage(
                content = "Expliqué claramente el proyecto al equipo, comunicando los objetivos y expectativas de forma precisa",
                isFromUser = true
            )
        )

        // Act
        val result = useCase(messages)

        // Assert
        assertTrue(
            "Communication score should be > 60, was ${result[SoftSkill.COMMUNICATION]}",
            (result[SoftSkill.COMMUNICATION] ?: 0) > 60
        )
    }

    @Test
    fun `evaluate response with leadership keywords returns high leadership score`() = runTest {
        // Arrange
        val messages = listOf(
            ChatMessage(
                content = "Lideré al equipo durante el proyecto, tomé decisiones difíciles y motivé a todos para alcanzar los objetivos",
                isFromUser = true
            )
        )

        // Act
        val result = useCase(messages)

        // Assert
        assertTrue(
            "Leadership score should be > 60, was ${result[SoftSkill.LEADERSHIP]}",
            (result[SoftSkill.LEADERSHIP] ?: 0) > 60
        )
    }

    @Test
    fun `evaluate response with teamwork keywords returns high teamwork score`() = runTest {
        // Arrange
        val messages = listOf(
            ChatMessage(
                content = "Trabajé en equipo colaborando con mis compañeros, compartiendo conocimientos y apoyando en las tareas",
                isFromUser = true
            )
        )

        // Act
        val result = useCase(messages)

        // Assert
        assertTrue(
            "Teamwork score should be > 60, was ${result[SoftSkill.TEAMWORK]}",
            (result[SoftSkill.TEAMWORK] ?: 0) > 60
        )
    }

    @Test
    fun `evaluate response with problem solving keywords returns high score`() = runTest {
        // Arrange
        val messages = listOf(
            ChatMessage(
                content = "Analicé el problema cuidadosamente, identifiqué la causa raíz y desarrollé una solución efectiva",
                isFromUser = true
            )
        )

        // Act
        val result = useCase(messages)

        // Assert
        assertTrue(
            "Problem solving score should be > 60, was ${result[SoftSkill.PROBLEM_SOLVING]}",
            (result[SoftSkill.PROBLEM_SOLVING] ?: 0) > 60
        )
    }

    @Test
    fun `evaluate response with adaptability keywords returns high adaptability score`() = runTest {
        // Arrange
        val messages = listOf(
            ChatMessage(
                content = "Me adapté rápidamente a los cambios, ajusté mi enfoque y fui flexible ante nuevas situaciones",
                isFromUser = true
            )
        )

        // Act
        val result = useCase(messages)

        // Assert
        assertTrue(
            "Adaptability score should be > 60, was ${result[SoftSkill.ADAPTABILITY]}",
            (result[SoftSkill.ADAPTABILITY] ?: 0) > 60
        )
    }

    @Test
    fun `evaluate multiple responses calculates scores correctly`() = runTest {
        // Arrange
        val messages = listOf(
            ChatMessage(content = "Trabajo en equipo colaborando", isFromUser = true),
            ChatMessage(content = "Resuelvo problemas analizando", isFromUser = true),
            ChatMessage(content = "Me adapto rápidamente a cambios", isFromUser = true)
        )

        // Act
        val result = useCase(messages)

        // Assert
        assertTrue("Teamwork should be > 0", (result[SoftSkill.TEAMWORK] ?: 0) > 0)
        assertTrue("Problem solving should be > 0", (result[SoftSkill.PROBLEM_SOLVING] ?: 0) > 0)
        assertTrue("Adaptability should be > 0", (result[SoftSkill.ADAPTABILITY] ?: 0) > 0)
    }

    @Test
    fun `evaluate empty messages returns all scores as zero`() = runTest {
        // Arrange
        val messages = emptyList<ChatMessage>()

        // Act
        val result = useCase(messages)

        // Assert
        assertEquals("All scores should be 0", 0, result[SoftSkill.COMMUNICATION])
        assertEquals("All scores should be 0", 0, result[SoftSkill.LEADERSHIP])
        assertEquals("All scores should be 0", 0, result[SoftSkill.TEAMWORK])
        assertEquals("All scores should be 0", 0, result[SoftSkill.PROBLEM_SOLVING])
        assertEquals("All scores should be 0", 0, result[SoftSkill.ADAPTABILITY])
    }

    @Test
    fun `evaluate messages from AI are ignored`() = runTest {
        // Arrange
        val messages = listOf(
            ChatMessage(content = "Lidero equipos exitosamente", isFromUser = false), // De IA
            ChatMessage(content = "Hola", isFromUser = true) // Del usuario
        )

        // Act
        val result = useCase(messages)

        // Assert
        // El puntaje de liderazgo debe ser bajo porque el mensaje con palabras clave es de IA
        assertTrue(
            "Leadership score should be low since AI message is ignored",
            (result[SoftSkill.LEADERSHIP] ?: 0) < 50
        )
    }

    @Test
    fun `evaluate all skills are present in result`() = runTest {
        // Arrange
        val messages = listOf(
            ChatMessage(content = "Test message", isFromUser = true)
        )

        // Act
        val result = useCase(messages)

        // Assert
        assertTrue("Result should contain COMMUNICATION", result.containsKey(SoftSkill.COMMUNICATION))
        assertTrue("Result should contain LEADERSHIP", result.containsKey(SoftSkill.LEADERSHIP))
        assertTrue("Result should contain TEAMWORK", result.containsKey(SoftSkill.TEAMWORK))
        assertTrue("Result should contain PROBLEM_SOLVING", result.containsKey(SoftSkill.PROBLEM_SOLVING))
        assertTrue("Result should contain ADAPTABILITY", result.containsKey(SoftSkill.ADAPTABILITY))
    }
}

/**
 * UseCase simple para evaluar soft skills basado en palabras clave
 * (Implementación de ejemplo - en producción usaríamos el análisis de Gemini)
 */
class EvaluateSoftSkillsUseCase {
    operator fun invoke(messages: List<ChatMessage>): Map<SoftSkill, Int> {
        val userMessages = messages.filter { it.isFromUser }.map { it.content.lowercase() }

        if (userMessages.isEmpty()) {
            return SoftSkill.values().associateWith { 0 }
        }

        val allText = userMessages.joinToString(" ")

        return mapOf(
            SoftSkill.COMMUNICATION to calculateScore(allText, communicationKeywords),
            SoftSkill.LEADERSHIP to calculateScore(allText, leadershipKeywords),
            SoftSkill.TEAMWORK to calculateScore(allText, teamworkKeywords),
            SoftSkill.PROBLEM_SOLVING to calculateScore(allText, problemSolvingKeywords),
            SoftSkill.ADAPTABILITY to calculateScore(allText, adaptabilityKeywords)
        )
    }

    private fun calculateScore(text: String, keywords: List<String>): Int {
        val matchedKeywords = keywords.count { keyword -> text.contains(keyword) }
        val baseScore = (matchedKeywords * 100 / keywords.size).coerceIn(0, 100)

        // Bonus por longitud de respuesta (respuestas más elaboradas = mejor)
        val lengthBonus = (text.split(" ").size / 10).coerceAtMost(20)

        return (baseScore + lengthBonus).coerceIn(0, 100)
    }

    private val communicationKeywords = listOf(
        "comunic", "explic", "clar", "precis", "articular", "expresar", "transmitir",
        "dialogar", "conversación", "presentar", "explicación"
    )

    private val leadershipKeywords = listOf(
        "lider", "liderar", "dirigir", "guiar", "motivar", "inspirar", "decisiones",
        "responsabilidad", "iniciativa", "coordinar", "delegar", "supervisar"
    )

    private val teamworkKeywords = listOf(
        "equipo", "colaborar", "colaboración", "cooperar", "compartir", "apoyar",
        "conjunto", "grupal", "compañeros", "trabajar juntos", "ayudar"
    )

    private val problemSolvingKeywords = listOf(
        "resolver", "solución", "problema", "analizar", "análisis", "identificar",
        "diagnosticar", "estrategia", "enfoque", "alternativa", "mejorar"
    )

    private val adaptabilityKeywords = listOf(
        "adaptar", "flexible", "cambio", "ajustar", "nuevo", "aprender",
        "transformar", "evolucionar", "modificar", "actualizar", "rápidamente"
    )
}

