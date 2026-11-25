package com.aulanota.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Entidad 3: apunte. Pertenece a una materia (1 materia : N apuntes). */
@Entity(
    tableName = "apunte",
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
data class Apunte(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_apunte") val id: Long = 0,
    @ColumnInfo(name = "id_materia") val idMateria: Long,
    val titulo: String,
    val contenido: String = "",
    @ColumnInfo(name = "fecha_creacion") val fechaCreacion: String,
)
