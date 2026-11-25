package com.aulanota.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aulanota.app.ui.navigation.AulaNotaNavHost
import com.aulanota.app.ui.theme.AulaNotaTheme
import dagger.hilt.android.AndroidEntryPoint

/** Punto de entrada de la app (MAIN/LAUNCHER en el Manifest). @AndroidEntryPoint permite inyectar en ella. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AulaNotaTheme {
                AulaNotaNavHost()
            }
        }
    }
}
