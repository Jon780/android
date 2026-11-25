package com.aulanota.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aulanota.app.data.local.entity.Apunte
import kotlinx.coroutines.flow.Flow

@Dao
interface ApunteDao {

    @Insert
    suspend fun insertar(apunte: Apunte): Long

    @Query("SELECT * FROM apunte WHERE id_materia = :idMateria ORDER BY id_apunte DESC")
    fun observarPorMateria(idMateria: Long): Flow<List<Apunte>>

    @Update
    suspend fun actualizar(apunte: Apunte)

    @Delete
    suspend fun eliminar(apunte: Apunte)
}
