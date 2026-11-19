package com.aulanota.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.aulanota.app.ui.navigation.Screen
import com.aulanota.app.ui.screens.BienvenidaScreen
import com.aulanota.app.ui.screens.LoginScreen

/** Navegación simple por estado: Bienvenida -> Login. Se irán agregando más pantallas. */
@Composable
fun AulaNotaApp() {
    var ruta by rememberSaveable { mutableStateOf(Screen.Bienvenida.route) }

    BackHandler(enabled = ruta == Screen.Login.route) { ruta = Screen.Bienvenida.route }

    Crossfade(targetState = ruta, label = "Navegacion") { destino ->
        when (destino) {
            Screen.Login.route -> LoginScreen(
                onVolver = { ruta = Screen.Bienvenida.route },
                // TODO: aquí se navegará a la siguiente interfaz cuando exista.
                onLoginExitoso = { },
            )
            else -> BienvenidaScreen(onComenzar = { ruta = Screen.Login.route })
        }
    }
}
