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
import com.calyrsoft.ucbp1.features.cardexample.presentation.CardScreen
import com.calyrsoft.ucbp1.features.home.presentation.HomeScreen
import com.calyrsoft.ucbp1.features.interview.presentation.InterviewResultsScreen
import com.calyrsoft.ucbp1.features.interview.presentation.InterviewScreen
import com.calyrsoft.ucbp1.features.dollar.presentation.DollarScreen
import com.calyrsoft.ucbp1.features.github.presentation.GithubScreen
import com.calyrsoft.ucbp1.features.interview.domain.model.SoftSkill
import com.calyrsoft.ucbp1.features.interview.presentation.InterviewViewModel
import com.calyrsoft.ucbp1.features.movie.domain.model.MovieModel
import com.calyrsoft.ucbp1.features.movie.presentation.MovieDetailScreen
import com.calyrsoft.ucbp1.features.movie.presentation.PopularMoviesScreen
import com.calyrsoft.ucbp1.features.profile.application.ProfileScreen
import com.calyrsoft.ucbp1.features.webview.presentation.AtuladoScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.UUID

@Composable
fun AppNavigation(navigationViewModel: NavigationViewModel, modifier: Modifier, navController: NavHostController) {

    // Manejar navegación desde el ViewModel
    LaunchedEffect(Unit) {
        navigationViewModel.navigationCommand.collect { command ->
            when (command) {
                is NavigationViewModel.NavigationCommand.NavigateTo -> {
                    navController.navigate(command.route) {
                        // Configuración del back stack según sea necesario
                        when (command.options) {
                            NavigationOptions.CLEAR_BACK_STACK -> {
                                popUpTo(0) // Limpiar todo el back stack
                            }
                            NavigationOptions.REPLACE_HOME -> {
                                popUpTo(Screen.Dollar.route) { inclusive = true }
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
                onNavigateAsGuest = { // Added guest navigation action
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

        composable(Screen.Github.route) {
            GithubScreen(modifier = Modifier)
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onStartInterview = {
                    navController.navigate(Screen.Interview.route)
                },
                onViewHistory = {
                    // Simulate a certificate for the user
                    val fakeScores = mapOf(
                        SoftSkill.COMMUNICATION to 90,
                        SoftSkill.LEADERSHIP to 75,
                        SoftSkill.TEAMWORK to 95,
                        SoftSkill.PROBLEM_SOLVING to 80,
                        SoftSkill.ADAPTABILITY to 88
                    )
                    val scoresJson = Json.encodeToString(fakeScores)
                    val encodedScores = URLEncoder.encode(scoresJson, "UTF-8")
                    navController.navigate(Screen.InterviewResults.withArgs(encodedScores))
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

            // Listen for interview completion to navigate
            val interviewState by interviewViewModel.uiState.collectAsState()
            LaunchedEffect(interviewState.isCompleted) {
                if (interviewState.isCompleted) {
                    interviewState.scores?.let { scores ->
                        val scoresJson = Json.encodeToString(scores)
                        val encodedScores = URLEncoder.encode(scoresJson, "UTF-un8")
                        navController.navigate(Screen.InterviewResults.withArgs(encodedScores)) {
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

            // Start the interview automatically. Use a guest ID if the user is not logged in.
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
            if (scoresJson.isNotEmpty()) {
                val decodedScores = URLDecoder.decode(scoresJson, "UTF-8")
                val scores: Map<SoftSkill, Int> = Json.decodeFromString(decodedScores)

                InterviewResultsScreen(
                    scores = scores,
                    onNavigateHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            } else {
                // Handle error case where scores are not passed
                 InterviewResultsScreen(
                    scores = emptyMap(),
                    onNavigateHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }

        composable(Screen.CardExamples.route) { CardScreen() }

        composable(Screen.Dollar.route) {
            DollarScreen()
        }

        composable(Screen.PopularMovies.route) {
            PopularMoviesScreen( navigateToDetail  = { movie ->
                val movieJson = Json.encodeToString(movie)
                val encodeMovieJson = URLEncoder.encode(movieJson, "UTF-8")

                navController.navigate(
                    "${Screen.MovieDetail.route}/${encodeMovieJson}")
            })
        }

        composable(
            route = "${Screen.MovieDetail.route}/{movie}",
            arguments = listOf(
                navArgument("movie") { type = NavType.StringType }
            )
            ) {
            val movieJson = it.arguments?.getString("movie") ?: ""
            val movieDecoded = URLDecoder.decode(movieJson, "UTF-8")
            val movie = Json.decodeFromString<MovieModel>(movieDecoded)

            MovieDetailScreen(
                movie = movie,
                back = {
                    navController.popBackStack()
                })
        }

        composable(
            Screen.Atulado.route
        ) {
            AtuladoScreen(
                "https://www.bisa.com/atulado",
                postData = null,
                modifier = modifier,
                shouldStopBrowsing = { true }
            )
        }
    }
}
