package com.aulanota.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aulanota.app.ui.screens.BienvenidaScreen
import com.aulanota.app.ui.screens.DetalleMateriaScreen
import com.aulanota.app.ui.screens.LoginScreen
import com.aulanota.app.ui.screens.MateriasScreen
import com.aulanota.app.ui.viewmodel.AuthViewModel
import com.aulanota.app.ui.viewmodel.DetalleMateriaViewModel
import com.aulanota.app.ui.viewmodel.MateriasViewModel

/**
 * NavHost = contenedor de la navegación. Cada composable(ruta) { } (función de NavGraphBuilder)
 * registra una pantalla dentro del grafo de navegación.
 */
@Composable
fun AulaNotaNavHost(navController: NavHostController = rememberNavController()) {

    // Después de iniciar sesión se borra Bienvenida/Login del historial.
    val irAMaterias: () -> Unit = {
        navController.navigate(Screen.Materias.route) {
            popUpTo(Screen.Bienvenida.route) { inclusive = true }
        }
    }
    // Al cerrar sesión se vuelve a Bienvenida y se limpia el historial.
    val irABienvenida: () -> Unit = {
        navController.navigate(Screen.Bienvenida.route) {
            popUpTo(navController.graph.id)
        }
    }

    NavHost(navController = navController, startDestination = Screen.Bienvenida.route) {

        composable(Screen.Bienvenida.route) {
            BienvenidaScreen(onComenzar = { navController.navigate(Screen.Login.route) })
        }

        composable(Screen.Login.route) {
            val viewModel: AuthViewModel = hiltViewModel()
            val estado by viewModel.uiState.collectAsStateWithLifecycle()
            LoginScreen(
                estado = estado,
                onIniciarSesion = viewModel::iniciarSesion,
                onRegistrar = viewModel::registrar,
                onVolver = { navController.popBackStack() },
                onLoginExitoso = irAMaterias,
            )
        }

        composable(Screen.Materias.route) {
            val viewModel: MateriasViewModel = hiltViewModel()
            val estado by viewModel.uiState.collectAsStateWithLifecycle()
            MateriasScreen(
                estado = estado,
                onGuardar = viewModel::guardar,
                onEliminar = viewModel::eliminar,
                onAbrirMateria = { navController.navigate(Screen.DetalleMateria.crearRuta(it.id)) },
                onActualizarNombre = viewModel::actualizarNombre,
                onEliminarCuenta = viewModel::eliminarCuenta,
                onCuentaEliminada = irABienvenida,
                onSalir = {
                    viewModel.cerrarSesion()
                    irABienvenida()
                },
            )
        }

        composable(
            route = Screen.DetalleMateria.route,
            arguments = listOf(navArgument(Screen.DetalleMateria.ARG_ID) { type = NavType.LongType }),
        ) {
            val viewModel: DetalleMateriaViewModel = hiltViewModel()
            val estado by viewModel.uiState.collectAsStateWithLifecycle()
            DetalleMateriaScreen(
                estado = estado,
                onVolver = { navController.popBackStack() },
                onGuardarApunte = viewModel::guardarApunte,
                onEliminarApunte = viewModel::eliminarApunte,
                onGuardarHorario = viewModel::guardarHorario,
                onEliminarHorario = viewModel::eliminarHorario,
            )
        }
    }
}
