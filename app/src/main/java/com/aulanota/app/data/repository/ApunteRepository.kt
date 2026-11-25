package com.aulanota.app.data.repository

import com.aulanota.app.data.local.dao.ApunteDao
import com.aulanota.app.data.local.entity.Apunte
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class ApunteRepository @Inject constructor(private val dao: ApunteDao) {
    fun observarPorMateria(idMateria: Long): Flow<List<Apunte>> = dao.observarPorMateria(idMateria)
    suspend fun insertar(apunte: Apunte) = dao.insertar(apunte)
    suspend fun actualizar(apunte: Apunte) = dao.actualizar(apunte)
    suspend fun eliminar(apunte: Apunte) = dao.eliminar(apunte)
}
