package com.aulanota.app.di

import android.content.Context
import androidx.room.Room
import com.aulanota.app.data.local.AulaNotaDatabase
import com.aulanota.app.data.local.dao.ApunteDao
import com.aulanota.app.data.local.dao.HorarioDao
import com.aulanota.app.data.local.dao.MateriaDao
import com.aulanota.app.data.local.dao.UsuarioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt: le enseña a Hilt cómo construir la base de datos y los DAOs.
 * @Singleton => solo existe UNA instancia de la base de datos en toda la app.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AulaNotaDatabase =
        // Nombre nuevo ("aulanota_room.db"): la versión anterior de la app creó "aulanota.db" con otras
        // tablas (SQLiteOpenHelper) y Room no puede abrir ese archivo viejo.
        Room.databaseBuilder(context, AulaNotaDatabase::class.java, "aulanota_room.db")
            // Mientras se desarrolla: si cambias las tablas, la BD se recrea en vez de cerrar la app.
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides fun provideUsuarioDao(db: AulaNotaDatabase): UsuarioDao = db.usuarioDao()
    @Provides fun provideMateriaDao(db: AulaNotaDatabase): MateriaDao = db.materiaDao()
    @Provides fun provideApunteDao(db: AulaNotaDatabase): ApunteDao = db.apunteDao()
    @Provides fun provideHorarioDao(db: AulaNotaDatabase): HorarioDao = db.horarioDao()
}
