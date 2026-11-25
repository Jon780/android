package com.aulanota.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aulanota.app.R
import com.aulanota.app.ui.theme.AulaNotaTheme
import com.aulanota.app.ui.viewmodel.AuthUiState
import com.aulanota.app.util.esCorreoValido

/** Interfaz 2: Inicio de sesión y registro (se alterna con el botón de abajo). */
@Composable
fun LoginScreen(
    estado: AuthUiState,
    onIniciarSesion: (correo: String, contrasena: String) -> Unit,
    onRegistrar: (nombre: String, correo: String, contrasena: String) -> Unit,
    onVolver: () -> Unit,
    onLoginExitoso: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var modoRegistro by rememberSaveable { mutableStateOf(false) }
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }
    var mostrarContrasena by rememberSaveable { mutableStateOf(false) }
    var intentoEnvio by rememberSaveable { mutableStateOf(false) }

    val nombreValido = nombre.isNotBlank()
    val correoValido = correo.esCorreoValido()
    val contrasenaValida = contrasena.length >= 6
    val confirmarValido = confirmar == contrasena

    // Cuando el ViewModel avisa que el login fue exitoso, se navega a la siguiente pantalla.
    LaunchedEffect(estado.exito) {
        if (estado.exito) onLoginExitoso()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_aulanota),
            contentDescription = stringResource(R.string.logo_descripcion),
            modifier = Modifier.size(120.dp),
        )
        Text(
            text = stringResource(if (modoRegistro) R.string.registro_titulo else R.string.login_titulo),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.login_subtitulo),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(32.dp))

        if (modoRegistro) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text(stringResource(R.string.registro_nombre)) },
                singleLine = true,
                isError = intentoEnvio && !nombreValido,
                supportingText = {
                    if (intentoEnvio && !nombreValido) Text(stringResource(R.string.registro_nombre_error))
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
        }
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it.trim() },
            label = { Text(stringResource(R.string.login_correo)) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = intentoEnvio && !correoValido,
            supportingText = {
                if (intentoEnvio && !correoValido) Text(stringResource(R.string.login_correo_error))
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text(stringResource(R.string.login_contrasena)) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                TextButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                    Text(stringResource(if (mostrarContrasena) R.string.login_ocultar else R.string.login_mostrar))
                }
            },
            singleLine = true,
            visualTransformation =
                if (mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = intentoEnvio && !contrasenaValida,
            supportingText = {
                if (intentoEnvio && !contrasenaValida) Text(stringResource(R.string.login_contrasena_error))
            },
            modifier = Modifier.fillMaxWidth(),
        )

        if (modoRegistro) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmar,
                onValueChange = { confirmar = it },
                label = { Text(stringResource(R.string.registro_confirmar)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = intentoEnvio && !confirmarValido,
                supportingText = {
                    if (intentoEnvio && !confirmarValido) Text(stringResource(R.string.registro_confirmar_error))
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // Mensaje de error personalizado que viene del ViewModel (credenciales incorrectas, etc.).
        if (estado.error != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(estado.error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                intentoEnvio = true
                if (modoRegistro) {
                    if (nombreValido && correoValido && contrasenaValida && confirmarValido) {
                        onRegistrar(nombre, correo, contrasena)
                    }
                } else if (correoValido && contrasenaValida) {
                    onIniciarSesion(correo, contrasena)
                }
            },
            enabled = !estado.cargando,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(if (modoRegistro) R.string.registro_boton else R.string.login_boton_entrar))
        }
        TextButton(onClick = {
            modoRegistro = !modoRegistro
            intentoEnvio = false
        }) {
            Text(stringResource(if (modoRegistro) R.string.registro_ir_login else R.string.login_ir_registro))
        }
        TextButton(onClick = onVolver) {
            Text(stringResource(R.string.login_boton_volver))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginPreview() {
    AulaNotaTheme {
        LoginScreen(
            estado = AuthUiState(),
            onIniciarSesion = { _, _ -> },
            onRegistrar = { _, _, _ -> },
            onVolver = {},
            onLoginExitoso = {},
        )
    }
}
