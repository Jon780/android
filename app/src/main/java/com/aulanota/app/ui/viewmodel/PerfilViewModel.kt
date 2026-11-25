package com.aulanota.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aulanota.app.data.SessionManager
import com.aulanota.app.data.local.entity.Usuario
import com.aulanota.app.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PerfilUiState(
    val usuario: Usuario? = null,
    val cuentaEliminada: Boolean = false,
)

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val repositorio: UsuarioRepository,
    private val sesion: SessionManager,
) : ViewModel() {

    private val cuentaEliminada = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<PerfilUiState> = combine(
        sesion.usuarioId.flatMapLatest { id ->
            if (id == null) flowOf(null) else repositorio.observarPorId(id)
        },
        cuentaEliminada,
    ) { usuario, eliminada ->
        PerfilUiState(usuario, eliminada)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PerfilUiState())

    fun actualizarNombre(nombre: String) {
        val usuario = uiState.value.usuario ?: return
        viewModelScope.launch { repositorio.actualizar(usuario.copy(nombre = nombre.trim())) }
    }

    fun eliminarCuenta() {
        val usuario = uiState.value.usuario ?: return
        viewModelScope.launch {
            repositorio.eliminar(usuario)
            sesion.cerrar()
            cuentaEliminada.value = true
        }
    }
}
