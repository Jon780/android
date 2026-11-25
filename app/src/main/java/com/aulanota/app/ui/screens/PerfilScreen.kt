package com.aulanota.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aulanota.app.R
import com.aulanota.app.data.local.entity.Usuario
import com.aulanota.app.ui.components.ConfirmarEliminarDialog
import com.aulanota.app.ui.theme.AulaNotaTheme
import com.aulanota.app.ui.viewmodel.PerfilUiState

/** Interfaz 6: perfil. Permite actualizar el nombre (UPDATE) y eliminar la cuenta (DELETE). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    estado: PerfilUiState,
    onActualizarNombre: (String) -> Unit,
    onEliminarCuenta: () -> Unit,
    onVolver: () -> Unit,
    onCuentaEliminada: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val usuario = estado.usuario
    // remember(usuario?.id): el campo se inicializa con el nombre guardado cuando carga el usuario.
    var nombre by remember(usuario?.id) { mutableStateOf(usuario?.nombre ?: "") }
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    LaunchedEffect(estado.cuentaEliminada) {
        if (estado.cuentaEliminada) onCuentaEliminada()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.perfil_titulo)) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.boton_volver_atras))
                    }
                },
            )
        },
    ) { padding ->
        if (usuario != null) {
            Column(Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
                Text(
                    text = stringResource(R.string.perfil_correo, usuario.correo),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text(stringResource(R.string.registro_nombre)) },
                    singleLine = true,
                    isError = nombre.isBlank(),
                    supportingText = { if (nombre.isBlank()) Text(stringResource(R.string.registro_nombre_error)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { onActualizarNombre(nombre) },
                    enabled = nombre.isNotBlank() && nombre.trim() != usuario.nombre,
                    modifier = Modifier.fillMaxWidth(),
                ) { Text(stringResource(R.string.perfil_guardar)) }
                Spacer(Modifier.height(32.dp))
                OutlinedButton(
                    onClick = { mostrarConfirmacion = true },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text(stringResource(R.string.perfil_eliminar_cuenta)) }
            }
        }
    }

    if (mostrarConfirmacion) {
        ConfirmarEliminarDialog(
            titulo = stringResource(R.string.perfil_eliminar_cuenta),
            mensaje = stringResource(R.string.perfil_eliminar_mensaje),
            onConfirmar = {
                mostrarConfirmacion = false
                onEliminarCuenta()
            },
            onCancelar = { mostrarConfirmacion = false },
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PerfilPreview() {
    AulaNotaTheme {
        PerfilScreen(
            estado = PerfilUiState(Usuario(id = 1, nombre = "Juan Pérez", correo = "juan@gmail.com", contrasenaHash = "")),
            onActualizarNombre = {},
            onEliminarCuenta = {},
            onVolver = {},
            onCuentaEliminada = {},
        )
    }
}
