package com.aulanota.app.ui.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aulanota.app.R
import com.aulanota.app.data.SessionManager
import com.aulanota.app.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Estado de la pantalla (UDF): la UI solo lo lee y lo dibuja. */
data class AuthUiState(
    val cargando: Boolean = false,
    @StringRes val error: Int? = null,
    val exito: Boolean = false,
)

/**
 * UDF (flujo unidireccional): la UI envía eventos (iniciarSesion, registrar),
 * el ViewModel cambia el estado y la UI se vuelve a dibujar con el nuevo estado.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repositorio: UsuarioRepository,
    private val sesion: SessionManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun iniciarSesion(correo: String, contrasena: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(cargando = true)
            try {
                val usuario = repositorio.iniciarSesion(correo, contrasena)
                if (usuario == null) {
                    _uiState.value = AuthUiState(error = R.string.error_credenciales)
                } else {
                    sesion.iniciar(usuario.id)
                    _uiState.value = AuthUiState(exito = true)
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = R.string.error_generico)
            }
        }
    }

    fun registrar(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(cargando = true)
            try {
                val usuario = repositorio.registrar(nombre, correo, contrasena)
                if (usuario == null) {
                    _uiState.value = AuthUiState(error = R.string.error_correo_existe)
                } else {
                    sesion.iniciar(usuario.id)
                    _uiState.value = AuthUiState(exito = true)
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = R.string.error_generico)
            }
        }
    }
}
