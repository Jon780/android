package com.aulanota.app.ui.navigation

/** Las 4 interfaces de AulaNota. */
sealed class Screen(val route: String) {
    data object Bienvenida : Screen("bienvenida")
    data object Login : Screen("login")
    data object Materias : Screen("materias")

    data object DetalleMateria : Screen("materia/{idMateria}") {
        const val ARG_ID = "idMateria"
        fun crearRuta(idMateria: Long) = "materia/$idMateria"
    }
}
