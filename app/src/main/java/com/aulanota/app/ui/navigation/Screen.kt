package com.aulanota.app.ui.navigation

/** Pantallas de AulaNota, en el orden en que se van construyendo. */
sealed class Screen(val route: String) {
    data object Bienvenida : Screen("bienvenida")
    data object Login : Screen("login")
}
