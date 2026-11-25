package com.aulanota.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aulanota.app.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

/** DAO = objeto de acceso a datos. Aquí se declaran las operaciones CRUD de usuario. */
@Dao
interface UsuarioDao {

    // CREATE
    @Insert
    suspend fun insertar(usuario: Usuario): Long

    // READ
    @Query("SELECT * FROM usuario WHERE correo = :correo AND contrasena_hash = :hash LIMIT 1")
    suspend fun iniciarSesion(correo: String, hash: String): Usuario?

    @Query("SELECT * FROM usuario WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM usuario WHERE id_usuario = :id")
    fun observarPorId(id: Long): Flow<Usuario?>

    // UPDATE
    @Update
    suspend fun actualizar(usuario: Usuario)

    // DELETE
    @Delete
    suspend fun eliminar(usuario: Usuario)
}
