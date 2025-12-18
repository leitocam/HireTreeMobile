package com.calyrsoft.ucbp1.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WorkHistory
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Navigation drawer items para HireTree Mobile
 */
sealed class NavigationDrawer(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
) {
    data object Home : NavigationDrawer(
        "Inicio",
        Icons.Filled.Home,
        Icons.Outlined.Home,
        Screen.Home.route
    )

    data object Interview : NavigationDrawer(
        "Entrevista",
        Icons.Filled.WorkHistory,
        Icons.Outlined.WorkHistory,
        Screen.Interview.route
    )

    data object Profile : NavigationDrawer(
        "Perfil",
        Icons.Filled.Person,
        Icons.Outlined.Person,
        Screen.Profile.route
    )
}

