package com.calyrsoft.ucbp1.navigation

sealed class Screen(val route: String) {
    // Auth screens
    object Login: Screen("login")
    object SignUp: Screen("signup")

    // Main screens (HireTree)
    object Home: Screen("home")
    object Interview: Screen("interview")
    object InterviewResults: Screen("interview_results")
    object Profile: Screen("profile")


    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}