package com.aulanota.app.data.repository

import com.aulanota.app.data.local.dao.MateriaDao
import com.aulanota.app.data.local.entity.Materia
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class MateriaRepository @Inject constructor(private val dao: MateriaDao) {
    fun observarPorUsuario(idUsuario: Long): Flow<List<Materia>> = dao.observarPorUsuario(idUsuario)
    fun observarPorId(id: Long): Flow<Materia?> = dao.observarPorId(id)
    suspend fun insertar(materia: Materia) = dao.insertar(materia)
    suspend fun actualizar(materia: Materia) = dao.actualizar(materia)
    suspend fun eliminar(materia: Materia) = dao.eliminar(materia)
}
