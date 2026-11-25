package com.aulanota.app.data

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Guarda qué usuario inició sesión. Es @Singleton: Hilt crea una sola instancia
 * y la comparten todos los ViewModels.
 */
@Singleton
class SessionManager @Inject constructor() {
    private val _usuarioId = MutableStateFlow<Long?>(null)
    val usuarioId: StateFlow<Long?> = _usuarioId.asStateFlow()

    fun iniciar(idUsuario: Long) {
        _usuarioId.value = idUsuario
    }

    fun cerrar() {
        _usuarioId.value = null
    }
}
