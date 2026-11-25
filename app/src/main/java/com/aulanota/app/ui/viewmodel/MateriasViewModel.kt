package com.aulanota.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aulanota.app.data.SessionManager
import com.aulanota.app.data.local.entity.Materia
import com.aulanota.app.data.local.entity.Usuario
import com.aulanota.app.data.repository.MateriaRepository
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

data class MateriasUiState(
    val materias: List<Materia> = emptyList(),
    val usuario: Usuario? = null,
    val cuentaEliminada: Boolean = false,
)

@HiltViewModel
class MateriasViewModel @Inject constructor(
    private val materiaRepositorio: MateriaRepository,
    private val usuarioRepositorio: UsuarioRepository,
    private val sesion: SessionManager,
) : ViewModel() {

    private val cuentaEliminada = MutableStateFlow(false)

    /** Observer: la lista y el usuario se actualizan solos cuando cambian las tablas. */
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<MateriasUiState> = combine(
        sesion.usuarioId.flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else materiaRepositorio.observarPorUsuario(id)
        },
        sesion.usuarioId.flatMapLatest { id ->
            if (id == null) flowOf(null) else usuarioRepositorio.observarPorId(id)
        },
        cuentaEliminada,
    ) { materias, usuario, eliminada ->
        MateriasUiState(materias, usuario, eliminada)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MateriasUiState())

    // ---------- Materias ----------
    /** Si [materia] es null se INSERTA una nueva; si no, se ACTUALIZA la existente. */
    fun guardar(materia: Materia?, nombre: String, profesor: String, aula: String) {
        val idUsuario = sesion.usuarioId.value ?: return
        viewModelScope.launch {
            if (materia == null) {
                materiaRepositorio.insertar(
                    Materia(idUsuario = idUsuario, nombre = nombre.trim(), profesor = profesor.trim(), aula = aula.trim())
                )
            } else {
                materiaRepositorio.actualizar(
                    materia.copy(nombre = nombre.trim(), profesor = profesor.trim(), aula = aula.trim())
                )
            }
        }
    }

    fun eliminar(materia: Materia) {
        viewModelScope.launch { materiaRepositorio.eliminar(materia) }
    }

    // ---------- Perfil del usuario (UPDATE y DELETE de la tabla usuario) ----------
    fun actualizarNombre(nombre: String) {
        val usuario = uiState.value.usuario ?: return
        viewModelScope.launch { usuarioRepositorio.actualizar(usuario.copy(nombre = nombre.trim())) }
    }

    fun eliminarCuenta() {
        val usuario = uiState.value.usuario ?: return
        viewModelScope.launch {
            usuarioRepositorio.eliminar(usuario)
            sesion.cerrar()
            cuentaEliminada.value = true
        }
    }

    fun cerrarSesion() = sesion.cerrar()
}
