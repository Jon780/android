package com.aulanota.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aulanota.app.data.local.entity.HorarioClase
import kotlinx.coroutines.flow.Flow

@Dao
interface HorarioDao {

    @Insert
    suspend fun insertar(horario: HorarioClase): Long

    @Query("SELECT * FROM horario_clase WHERE id_materia = :idMateria ORDER BY dia_semana, hora_inicio")
    fun observarPorMateria(idMateria: Long): Flow<List<HorarioClase>>

    @Update
    suspend fun actualizar(horario: HorarioClase)

    @Delete
    suspend fun eliminar(horario: HorarioClase)
}
