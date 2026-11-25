package com.aulanota.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Entidad 4: horario_clase. Pertenece a una materia (1 materia : N horarios). */
@Entity(
    tableName = "horario_clase",
    foreignKeys = [
        ForeignKey(
            entity = Materia::class,
            parentColumns = ["id_materia"],
            childColumns = ["id_materia"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("id_materia")],
)
data class HorarioClase(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_horario") val id: Long = 0,
    @ColumnInfo(name = "id_materia") val idMateria: Long,
    /** 1 = Lunes ... 7 = Domingo */
    @ColumnInfo(name = "dia_semana") val diaSemana: Int,
    @ColumnInfo(name = "hora_inicio") val horaInicio: String,
    @ColumnInfo(name = "hora_fin") val horaFin: String,
)
