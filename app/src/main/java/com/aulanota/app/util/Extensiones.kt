package com.aulanota.app.util

import android.util.Patterns
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Función de extensión: agrega el método sha256() a cualquier String. */
fun String.sha256(): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}

/** Función de extensión: valida el formato de un correo. */
fun String.esCorreoValido(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

/** Función de extensión: valida una hora con formato HH:mm (24 horas). */
fun String.esHoraValida(): Boolean = Regex("^([01]\\d|2[0-3]):[0-5]\\d$").matches(this)

/** Fecha y hora actual como texto, para guardar la fecha de creación de un apunte. */
fun fechaActual(): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
