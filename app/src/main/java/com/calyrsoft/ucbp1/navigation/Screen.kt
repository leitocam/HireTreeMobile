package com.calyrsoft.ucbp1.navigation

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object SignUp: Screen("signup")
    object Home: Screen("home")
    object Interview: Screen("interview")
    object InterviewResults: Screen("interviewResults")
    object Github: Screen("github")
    object Profile: Screen("profile")

    object CardExamples: Screen("card")
    object Dollar: Screen("dollar")
    object PopularMovies: Screen("popularMovies")
    object MovieDetail: Screen("movieDetail")
    object Atulado: Screen("atulado")
}