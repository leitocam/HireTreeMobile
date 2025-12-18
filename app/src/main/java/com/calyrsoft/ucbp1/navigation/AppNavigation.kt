package com.calyrsoft.ucbp1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.calyrsoft.ucbp1.features.auth.presentation.AuthViewModel
import com.calyrsoft.ucbp1.features.auth.presentation.LoginScreen
import com.calyrsoft.ucbp1.features.auth.presentation.SignUpScreen
import com.calyrsoft.ucbp1.features.home.presentation.HomeScreen
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import com.calyrsoft.ucbp1.features.interview.presentation.InterviewResultsScreen
import com.calyrsoft.ucbp1.features.interview.presentation.InterviewScreen
import com.calyrsoft.ucbp1.features.interview.presentation.InterviewViewModel
import com.calyrsoft.ucbp1.features.profile.application.ProfileScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.UUID

/**
 * Navegación principal de HireTree Mobile
 * Incluye solo las pantallas relacionadas con entrevistas y evaluación de soft skills
 */
@Composable
fun AppNavigation(
    navigationViewModel: NavigationViewModel,
    modifier: Modifier,
    navController: NavHostController
) {
    // Manejar navegación desde el ViewModel
    LaunchedEffect(Unit) {
        navigationViewModel.navigationCommand.collect { command ->
            when (command) {
                is NavigationViewModel.NavigationCommand.NavigateTo -> {
                    navController.navigate(command.route) {
                        when (command.options) {
                            NavigationOptions.CLEAR_BACK_STACK -> {
                                popUpTo(0) // Limpiar todo el back stack
                            }
                            NavigationOptions.REPLACE_HOME -> {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                            else -> {
                                // Navegación normal
                            }
                        }
                    }
                }
                is NavigationViewModel.NavigationCommand.PopBackStack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        // ============================================
        // AUTHENTICATION SCREENS
        // ============================================

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateAsGuest = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSignUpSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // ============================================
        // MAIN SCREENS (HireTree)
        // ============================================

        composable(Screen.Home.route) {
            HomeScreen(
                onStartInterview = {
                    navController.navigate(Screen.Interview.route)
                },
                onViewHistory = {
                    // Navegar a historial (por implementar)
                    // Por ahora muestra un resultado de ejemplo
                    val fakeScores = mapOf(
                        SoftSkill.COMMUNICATION to 90,
                        SoftSkill.LEADERSHIP to 75,
                        SoftSkill.TEAMWORK to 95,
                        SoftSkill.PROBLEM_SOLVING to 80,
                        SoftSkill.ADAPTABILITY to 88
                    )
                    val scoresJson = Json.encodeToString(fakeScores)
                    val encodedScores = URLEncoder.encode(scoresJson, "UTF-8")
                    navController.navigate("${Screen.InterviewResults.route}/$encodedScores")
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Interview.route) {
            val authViewModel: AuthViewModel = koinViewModel()
            val interviewViewModel: InterviewViewModel = koinViewModel()
            val userState by authViewModel.uiState.collectAsState()

            // Escuchar cuando la entrevista se completa para navegar
            val interviewState by interviewViewModel.uiState.collectAsState()
            LaunchedEffect(interviewState.isCompleted) {
                if (interviewState.isCompleted) {
                    interviewState.scores?.let { scores ->
                        val scoresJson = Json.encodeToString(scores)
                        val encodedScores = URLEncoder.encode(scoresJson, "UTF-8")
                        navController.navigate("${Screen.InterviewResults.route}/$encodedScores") {
                            popUpTo(Screen.Interview.route) { inclusive = true }
                        }
                    }
                }
            }

            InterviewScreen(
                viewModel = interviewViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )

            // Iniciar la entrevista automáticamente
            // Usar un ID de invitado si el usuario no está logueado
            LaunchedEffect(userState.isAuthenticated) {
                val userId = userState.user?.uid ?: "guest_${UUID.randomUUID()}"
                interviewViewModel.startInterview(userId)
            }
        }

        composable(
            route = "${Screen.InterviewResults.route}/{scores}",
            arguments = listOf(navArgument("scores") { type = NavType.StringType })
        ) {
            val scoresJson = it.arguments?.getString("scores") ?: ""

            val scores: Map<SoftSkill, Int> = if (scoresJson.isNotEmpty()) {
                try {
                    val decodedScores = URLDecoder.decode(scoresJson, "UTF-8")
                    Json.decodeFromString(decodedScores)
                } catch (e: Exception) {
                    emptyMap()
                }
            } else {
                emptyMap()
            }

            InterviewResultsScreen(
                scores = scores,
                onNavigateHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}

