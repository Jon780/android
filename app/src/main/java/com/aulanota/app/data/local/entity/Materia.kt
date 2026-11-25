package com.aulanota.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Entidad 2: materia. Pertenece a un usuario (1 usuario : N materias). */
@Entity(
    tableName = "materia",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("id_usuario")],
)
data class Materia(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_materia") val id: Long = 0,
    @ColumnInfo(name = "id_usuario") val idUsuario: Long,
    val nombre: String,
    val profesor: String = "",
    val aula: String = "",
)
