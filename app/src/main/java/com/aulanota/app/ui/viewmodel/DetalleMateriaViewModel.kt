package com.aulanota.app.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aulanota.app.data.local.entity.Apunte
import com.aulanota.app.data.local.entity.HorarioClase
import com.aulanota.app.data.local.entity.Materia
import com.aulanota.app.data.repository.ApunteRepository
import com.aulanota.app.data.repository.HorarioRepository
import com.aulanota.app.data.repository.MateriaRepository
import com.aulanota.app.ui.navigation.Screen
import com.aulanota.app.util.fechaActual
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DetalleUiState(
    val materia: Materia? = null,
    val apuntes: List<Apunte> = emptyList(),
    val horarios: List<HorarioClase> = emptyList(),
)

@HiltViewModel
class DetalleMateriaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    materiaRepositorio: MateriaRepository,
    private val apunteRepositorio: ApunteRepository,
    private val horarioRepositorio: HorarioRepository,
) : ViewModel() {

    // Id de la materia que llega como argumento de navegación.
    private val idMateria: Long = checkNotNull(savedStateHandle.get<Long>(Screen.DetalleMateria.ARG_ID))

    val uiState: StateFlow<DetalleUiState> = combine(
        materiaRepositorio.observarPorId(idMateria),
        apunteRepositorio.observarPorMateria(idMateria),
        horarioRepositorio.observarPorMateria(idMateria),
    ) { materia, apuntes, horarios ->
        DetalleUiState(materia, apuntes, horarios)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DetalleUiState())

    // ---------- Apuntes ----------
    fun guardarApunte(apunte: Apunte?, titulo: String, contenido: String) {
        viewModelScope.launch {
            if (apunte == null) {
                apunteRepositorio.insertar(
                    Apunte(idMateria = idMateria, titulo = titulo.trim(), contenido = contenido.trim(), fechaCreacion = fechaActual())
                )
            } else {
                apunteRepositorio.actualizar(apunte.copy(titulo = titulo.trim(), contenido = contenido.trim()))
            }
        }
    }

    fun eliminarApunte(apunte: Apunte) {
        viewModelScope.launch { apunteRepositorio.eliminar(apunte) }
    }

    // ---------- Horarios ----------
    fun guardarHorario(horario: HorarioClase?, dia: Int, horaInicio: String, horaFin: String) {
        viewModelScope.launch {
            if (horario == null) {
                horarioRepositorio.insertar(
                    HorarioClase(idMateria = idMateria, diaSemana = dia, horaInicio = horaInicio, horaFin = horaFin)
                )
            } else {
                horarioRepositorio.actualizar(horario.copy(diaSemana = dia, horaInicio = horaInicio, horaFin = horaFin))
            }
        }
    }

    fun eliminarHorario(horario: HorarioClase) {
        viewModelScope.launch { horarioRepositorio.eliminar(horario) }
    }
}
