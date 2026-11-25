package com.aulanota.app.data.repository

import com.aulanota.app.data.local.dao.UsuarioDao
import com.aulanota.app.data.local.entity.Usuario
import com.aulanota.app.util.sha256
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

/** Patrón Repository: los ViewModels piden los datos aquí y no conocen el DAO ni Room. */
@Singleton
class UsuarioRepository @Inject constructor(private val dao: UsuarioDao) {

    /** Devuelve null si el correo ya está registrado. */
    suspend fun registrar(nombre: String, correo: String, contrasena: String): Usuario? {
        val correoLimpio = correo.trim().lowercase()
        if (dao.buscarPorCorreo(correoLimpio) != null) return null
        val nuevo = Usuario(
            nombre = nombre.trim(),
            correo = correoLimpio,
            contrasenaHash = contrasena.sha256(),
        )
        return nuevo.copy(id = dao.insertar(nuevo))
    }

    /** Devuelve null si el correo o la contraseña son incorrectos. */
    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? =
        dao.iniciarSesion(correo.trim().lowercase(), contrasena.sha256())

    fun observarPorId(id: Long): Flow<Usuario?> = dao.observarPorId(id)

    suspend fun actualizar(usuario: Usuario) = dao.actualizar(usuario)

    suspend fun eliminar(usuario: Usuario) = dao.eliminar(usuario)
}
