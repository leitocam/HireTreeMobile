package com.calyrsoft.ucbp1.navigation

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object SignUp: Screen("signup")
    object Home: Screen("home")
    object Interview: Screen("interview")
    object InterviewResults: Screen("interview_results") // Base route
    object Github: Screen("github")
    object Profile: Screen("profile")

    object CardExamples: Screen("card")
    object Dollar: Screen("dollar")
    object PopularMovies: Screen("popularMovies")
    object MovieDetail: Screen("movieDetail")
    object Atulado: Screen("atulado")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}