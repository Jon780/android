package com.aulanota.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Entidad 1: usuario. Cada tabla de Room se declara con @Entity. */
@Entity(tableName = "usuario", indices = [Index(value = ["correo"], unique = true)])
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_usuario") val id: Long = 0,
    val nombre: String,
    val correo: String,
    @ColumnInfo(name = "contrasena_hash") val contrasenaHash: String,
)
