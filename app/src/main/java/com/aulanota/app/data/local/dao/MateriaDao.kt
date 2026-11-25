package com.aulanota.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aulanota.app.data.local.entity.Materia
import kotlinx.coroutines.flow.Flow

@Dao
interface MateriaDao {

    @Insert
    suspend fun insertar(materia: Materia): Long

    /** Devuelve un Flow: cada vez que la tabla cambia, la lista se emite de nuevo (Observer). */
    @Query("SELECT * FROM materia WHERE id_usuario = :idUsuario ORDER BY nombre")
    fun observarPorUsuario(idUsuario: Long): Flow<List<Materia>>

    @Query("SELECT * FROM materia WHERE id_materia = :id")
    fun observarPorId(id: Long): Flow<Materia?>

    @Update
    suspend fun actualizar(materia: Materia)

    @Delete
    suspend fun eliminar(materia: Materia)
}
