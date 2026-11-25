package com.aulanota.app.data.repository

import com.aulanota.app.data.local.dao.HorarioDao
import com.aulanota.app.data.local.entity.HorarioClase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class HorarioRepository @Inject constructor(private val dao: HorarioDao) {
    fun observarPorMateria(idMateria: Long): Flow<List<HorarioClase>> = dao.observarPorMateria(idMateria)
    suspend fun insertar(horario: HorarioClase) = dao.insertar(horario)
    suspend fun actualizar(horario: HorarioClase) = dao.actualizar(horario)
    suspend fun eliminar(horario: HorarioClase) = dao.eliminar(horario)
}
