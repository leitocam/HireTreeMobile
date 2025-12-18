package com.calyrsoft.ucbp1.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

enum class NavigationOptions {
    DEFAULT,
    CLEAR_BACK_STACK,
    REPLACE_HOME
}
class NavigationViewModel : ViewModel() {

    sealed class NavigationCommand {
        data class NavigateTo(val route: String, val options: NavigationOptions = NavigationOptions.DEFAULT) : NavigationCommand()
        object PopBackStack : NavigationCommand()
    }
    private val _navigationCommand = MutableSharedFlow<NavigationCommand>()
    val navigationCommand = _navigationCommand.asSharedFlow()

    fun navigateTo(route: String, options: NavigationOptions = NavigationOptions.DEFAULT) {
        viewModelScope.launch {
            _navigationCommand.emit(NavigationCommand.NavigateTo(route, options))
        }
    }

    fun popBackStack() {
        viewModelScope.launch {
            _navigationCommand.emit(NavigationCommand.PopBackStack)
        }
    }

    fun handleDeepLink(intent: android.content.Intent?) {
        viewModelScope.launch {
            try {
                // DEBUG: Log el intent recibido
                Log.d("NavigationViewModel", "Intent recibido: ${intent?.extras?.keySet()}")

                intent?.extras?.keySet()?.forEach { key ->
                    Log.d("NavigationViewModel", "Extra: $key = ${intent.getStringExtra(key)}")
                }

                when {
                    intent?.hasExtra("navigateTo") == true -> {
                        val destination = intent.getStringExtra("navigateTo")
                        Log.d("NavigationViewModel", "Procesando navigateTo: $destination")
                        handleNavigationDestination(destination)
                    }
                    intent?.action == android.content.Intent.ACTION_VIEW -> {
                        Log.d("NavigationViewModel", "Procesando ACTION_VIEW: ${intent.data}")
                        handleUriDeepLink(intent.data)
                    }
                    intent?.hasExtra("click_action") == true -> {
                        val clickAction = intent.getStringExtra("click_action")
                        Log.d("NavigationViewModel", "Procesando click_action: $clickAction")
                        handleClickAction(clickAction)
                    }
                    else -> {
                        Log.d("NavigationViewModel", "Navegación por defecto a Home")
                        navigateTo(Screen.Home.route, NavigationOptions.CLEAR_BACK_STACK)
                    }
                }
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error en handleDeepLink", e)
                navigateTo(Screen.Home.route, NavigationOptions.CLEAR_BACK_STACK)
            }
        }
    }

    private fun handleClickAction(clickAction: String?) {
        when (clickAction) {
            "OPEN_PROFILE" -> navigateTo(Screen.Profile.route, NavigationOptions.REPLACE_HOME)
            "OPEN_INTERVIEW" -> navigateTo(Screen.Interview.route, NavigationOptions.REPLACE_HOME)
            "OPEN_HOME" -> navigateTo(Screen.Home.route, NavigationOptions.CLEAR_BACK_STACK)
            else -> navigateTo(Screen.Home.route, NavigationOptions.CLEAR_BACK_STACK)
        }
    }

    private fun handleUriDeepLink(uri: android.net.Uri?) {
        when (uri?.host) {
            "profile" -> {
                navigateTo(Screen.Profile.route, NavigationOptions.REPLACE_HOME)
            }
            "interview" -> {
                navigateTo(Screen.Interview.route, NavigationOptions.REPLACE_HOME)
            }
            "home" -> {
                navigateTo(Screen.Home.route, NavigationOptions.CLEAR_BACK_STACK)
            }
            else -> {
                navigateTo(Screen.Home.route, NavigationOptions.CLEAR_BACK_STACK)
            }
        }
    }

    private fun handleNavigationDestination(destination: String?) {
        when (destination?.uppercase()) {
            "PROFILE" -> navigateTo(Screen.Profile.route, NavigationOptions.REPLACE_HOME)
            "INTERVIEW" -> navigateTo(Screen.Interview.route, NavigationOptions.REPLACE_HOME)
            "HOME" -> navigateTo(Screen.Home.route, NavigationOptions.REPLACE_HOME)
            else -> navigateTo(Screen.Home.route, NavigationOptions.CLEAR_BACK_STACK)
        }
    }
}