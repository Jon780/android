package com.aulanota.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aulanota.app.R
import com.aulanota.app.data.local.entity.Materia
import com.aulanota.app.data.local.entity.Usuario
import com.aulanota.app.ui.components.ConfirmarEliminarDialog
import com.aulanota.app.ui.theme.AulaNotaTheme
import com.aulanota.app.ui.viewmodel.MateriasUiState

/** Interfaz 4: lista de materias con CRUD (insertar, listar, editar, eliminar). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriasScreen(
    estado: MateriasUiState,
    onGuardar: (materia: Materia?, nombre: String, profesor: String, aula: String) -> Unit,
    onEliminar: (Materia) -> Unit,
    onAbrirMateria: (Materia) -> Unit,
    onActualizarNombre: (String) -> Unit,
    onEliminarCuenta: () -> Unit,
    onCuentaEliminada: () -> Unit,
    onSalir: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // null en ambos = diálogo cerrado. materiaEditando == null y mostrarDialogo == true => nueva materia.
    var mostrarDialogo by remember { mutableStateOf(false) }
    var materiaEditando by remember { mutableStateOf<Materia?>(null) }
    var materiaAEliminar by remember { mutableStateOf<Materia?>(null) }
    var mostrarPerfil by remember { mutableStateOf(false) }
    var mostrarEliminarCuenta by remember { mutableStateOf(false) }

    LaunchedEffect(estado.cuentaEliminada) {
        if (estado.cuentaEliminada) onCuentaEliminada()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.materias_titulo)) },
                actions = {
                    IconButton(onClick = { mostrarPerfil = true }) {
                        Icon(Icons.Default.Person, contentDescription = stringResource(R.string.materias_perfil))
                    }
                    TextButton(onClick = onSalir) { Text(stringResource(R.string.materias_salir)) }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                materiaEditando = null
                mostrarDialogo = true
            }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.materias_agregar))
            }
        },
    ) { padding ->
        if (estado.materias.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding).padding(32.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.materias_vacio),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(estado.materias, key = { it.id }) { materia ->
                    Card(onClick = { onAbrirMateria(materia) }, modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(materia.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                if (materia.profesor.isNotBlank()) {
                                    Text(materia.profesor, style = MaterialTheme.typography.bodyMedium)
                                }
                                if (materia.aula.isNotBlank()) {
                                    Text(materia.aula, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            IconButton(onClick = {
                                materiaEditando = materia
                                mostrarDialogo = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.boton_editar))
                            }
                            IconButton(onClick = { materiaAEliminar = materia }) {
                                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.boton_eliminar))
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        MateriaDialog(
            materia = materiaEditando,
            onGuardar = { nombre, profesor, aula ->
                onGuardar(materiaEditando, nombre, profesor, aula)
                mostrarDialogo = false
            },
            onCerrar = { mostrarDialogo = false },
        )
    }

    if (mostrarPerfil && estado.usuario != null) {
        PerfilDialog(
            usuario = estado.usuario,
            onGuardarNombre = onActualizarNombre,
            onEliminarCuenta = {
                mostrarPerfil = false
                mostrarEliminarCuenta = true
            },
            onCerrar = { mostrarPerfil = false },
        )
    }

    if (mostrarEliminarCuenta) {
        ConfirmarEliminarDialog(
            titulo = stringResource(R.string.perfil_eliminar_cuenta),
            mensaje = stringResource(R.string.perfil_eliminar_mensaje),
            onConfirmar = {
                mostrarEliminarCuenta = false
                onEliminarCuenta()
            },
            onCancelar = { mostrarEliminarCuenta = false },
        )
    }

    materiaAEliminar?.let { materia ->
        ConfirmarEliminarDialog(
            titulo = stringResource(R.string.materia_eliminar_titulo),
            mensaje = stringResource(R.string.materia_eliminar_mensaje),
            onConfirmar = {
                onEliminar(materia)
                materiaAEliminar = null
            },
            onCancelar = { materiaAEliminar = null },
        )
    }
}

@Composable
private fun PerfilDialog(
    usuario: Usuario,
    onGuardarNombre: (String) -> Unit,
    onEliminarCuenta: () -> Unit,
    onCerrar: () -> Unit,
) {
    var nombre by remember(usuario.id) { mutableStateOf(usuario.nombre) }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text(stringResource(R.string.perfil_titulo)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.perfil_correo, usuario.correo), style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text(stringResource(R.string.registro_nombre)) },
                    singleLine = true,
                    isError = nombre.isBlank(),
                    supportingText = { if (nombre.isBlank()) Text(stringResource(R.string.registro_nombre_error)) },
                )
                TextButton(onClick = onEliminarCuenta) {
                    Text(stringResource(R.string.perfil_eliminar_cuenta), color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onGuardarNombre(nombre)
                    onCerrar()
                },
                enabled = nombre.isNotBlank() && nombre.trim() != usuario.nombre,
            ) { Text(stringResource(R.string.perfil_guardar)) }
        },
        dismissButton = { TextButton(onClick = onCerrar) { Text(stringResource(R.string.boton_cancelar)) } },
    )
}

@Composable
private fun MateriaDialog(
    materia: Materia?,
    onGuardar: (nombre: String, profesor: String, aula: String) -> Unit,
    onCerrar: () -> Unit,
) {
    var nombre by remember { mutableStateOf(materia?.nombre ?: "") }
    var profesor by remember { mutableStateOf(materia?.profesor ?: "") }
    var aula by remember { mutableStateOf(materia?.aula ?: "") }
    var intentoEnvio by remember { mutableStateOf(false) }
    val nombreValido = nombre.isNotBlank()

    AlertDialog(
        onDismissRequest = onCerrar,
        title = {
            Text(stringResource(if (materia == null) R.string.materia_nueva else R.string.materia_editar))
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text(stringResource(R.string.materia_nombre)) },
                    singleLine = true,
                    isError = intentoEnvio && !nombreValido,
                    supportingText = {
                        if (intentoEnvio && !nombreValido) Text(stringResource(R.string.materia_nombre_error))
                    },
                )
                OutlinedTextField(
                    value = profesor,
                    onValueChange = { profesor = it },
                    label = { Text(stringResource(R.string.materia_profesor)) },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = aula,
                    onValueChange = { aula = it },
                    label = { Text(stringResource(R.string.materia_aula)) },
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                intentoEnvio = true
                if (nombreValido) onGuardar(nombre, profesor, aula)
            }) { Text(stringResource(R.string.boton_guardar)) }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) { Text(stringResource(R.string.boton_cancelar)) }
        },
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MateriasPreview() {
    AulaNotaTheme {
        MateriasScreen(
            estado = MateriasUiState(
                materias = listOf(Materia(id = 1, idUsuario = 1, nombre = "Programación Móvil", profesor = "Ing. Gómez", aula = "Lab 2")),
                usuario = Usuario(id = 1, nombre = "Juan Pérez", correo = "juan@gmail.com", contrasenaHash = ""),
            ),
            onGuardar = { _, _, _, _ -> },
            onEliminar = {},
            onAbrirMateria = {},
            onActualizarNombre = {},
            onEliminarCuenta = {},
            onCuentaEliminada = {},
            onSalir = {},
        )
    }
}
