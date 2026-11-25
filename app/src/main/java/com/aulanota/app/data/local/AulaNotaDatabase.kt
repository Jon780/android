package com.aulanota.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aulanota.app.data.local.dao.ApunteDao
import com.aulanota.app.data.local.dao.HorarioDao
import com.aulanota.app.data.local.dao.MateriaDao
import com.aulanota.app.data.local.dao.UsuarioDao
import com.aulanota.app.data.local.entity.Apunte
import com.aulanota.app.data.local.entity.HorarioClase
import com.aulanota.app.data.local.entity.Materia
import com.aulanota.app.data.local.entity.Usuario

/** Base de datos local SQLite administrada por Room (4 tablas). */
@Database(
    entities = [Usuario::class, Materia::class, Apunte::class, HorarioClase::class],
    version = 1,
    exportSchema = false,
)
abstract class AulaNotaDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun materiaDao(): MateriaDao
    abstract fun apunteDao(): ApunteDao
    abstract fun horarioDao(): HorarioDao
}
