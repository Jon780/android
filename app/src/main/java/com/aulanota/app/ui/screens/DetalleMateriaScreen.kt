package com.aulanota.app.ui.screens

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aulanota.app.R
import com.aulanota.app.data.local.entity.Apunte
import com.aulanota.app.data.local.entity.HorarioClase
import com.aulanota.app.data.local.entity.Materia
import com.aulanota.app.ui.components.ConfirmarEliminarDialog
import com.aulanota.app.ui.theme.AulaNotaTheme
import com.aulanota.app.ui.viewmodel.DetalleUiState
import com.aulanota.app.util.esHoraValida

/** Interfaz 5: detalle de una materia con dos pestañas: Apuntes y Horario (CRUD de ambas tablas). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleMateriaScreen(
    estado: DetalleUiState,
    onVolver: () -> Unit,
    onGuardarApunte: (apunte: Apunte?, titulo: String, contenido: String) -> Unit,
    onEliminarApunte: (Apunte) -> Unit,
    onGuardarHorario: (horario: HorarioClase?, dia: Int, horaInicio: String, horaFin: String) -> Unit,
    onEliminarHorario: (HorarioClase) -> Unit,
    modifier: Modifier = Modifier,
) {
    var pestana by rememberSaveable { mutableIntStateOf(0) }

    var mostrarApunteDialog by remember { mutableStateOf(false) }
    var apunteEditando by remember { mutableStateOf<Apunte?>(null) }
    var apunteAEliminar by remember { mutableStateOf<Apunte?>(null) }

    var mostrarHorarioDialog by remember { mutableStateOf(false) }
    var horarioEditando by remember { mutableStateOf<HorarioClase?>(null) }
    var horarioAEliminar by remember { mutableStateOf<HorarioClase?>(null) }

    val dias = stringArrayResource(R.array.dias_semana)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(estado.materia?.nombre ?: "") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.boton_volver_atras))
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (pestana == 0) {
                    apunteEditando = null
                    mostrarApunteDialog = true
                } else {
                    horarioEditando = null
                    mostrarHorarioDialog = true
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.boton_agregar))
            }
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            estado.materia?.let { materia ->
                Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    if (materia.profesor.isNotBlank()) {
                        Text(stringResource(R.string.detalle_profesor, materia.profesor), style = MaterialTheme.typography.bodyMedium)
                    }
                    if (materia.aula.isNotBlank()) {
                        Text(stringResource(R.string.detalle_aula, materia.aula), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            TabRow(selectedTabIndex = pestana) {
                Tab(selected = pestana == 0, onClick = { pestana = 0 }, text = { Text(stringResource(R.string.detalle_tab_apuntes)) })
                Tab(selected = pestana == 1, onClick = { pestana = 1 }, text = { Text(stringResource(R.string.detalle_tab_horario)) })
            }

            if (pestana == 0) {
                if (estado.apuntes.isEmpty()) {
                    TextoVacio(stringResource(R.string.detalle_sin_apuntes))
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(estado.apuntes, key = { it.id }) { apunte ->
                            Card(Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Column(Modifier.weight(1f)) {
                                        Text(apunte.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        if (apunte.contenido.isNotBlank()) {
                                            Text(apunte.contenido, style = MaterialTheme.typography.bodyMedium)
                                        }
                                        Text(
                                            stringResource(R.string.fecha_creacion, apunte.fechaCreacion),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                    IconButton(onClick = {
                                        apunteEditando = apunte
                                        mostrarApunteDialog = true
                                    }) { Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.boton_editar)) }
                                    IconButton(onClick = { apunteAEliminar = apunte }) {
                                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.boton_eliminar))
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (estado.horarios.isEmpty()) {
                    TextoVacio(stringResource(R.string.detalle_sin_horarios))
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(estado.horarios, key = { it.id }) { horario ->
                            Card(Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Column(Modifier.weight(1f)) {
                                        Text(dias[horario.diaSemana - 1], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        Text("${horario.horaInicio} - ${horario.horaFin}", style = MaterialTheme.typography.bodyMedium)
                                    }
                                    IconButton(onClick = {
                                        horarioEditando = horario
                                        mostrarHorarioDialog = true
                                    }) { Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.boton_editar)) }
                                    IconButton(onClick = { horarioAEliminar = horario }) {
                                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.boton_eliminar))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarApunteDialog) {
        ApunteDialog(
            apunte = apunteEditando,
            onGuardar = { titulo, contenido ->
                onGuardarApunte(apunteEditando, titulo, contenido)
                mostrarApunteDialog = false
            },
            onCerrar = { mostrarApunteDialog = false },
        )
    }
    apunteAEliminar?.let { apunte ->
        ConfirmarEliminarDialog(
            titulo = apunte.titulo,
            mensaje = stringResource(R.string.apunte_eliminar_mensaje),
            onConfirmar = {
                onEliminarApunte(apunte)
                apunteAEliminar = null
            },
            onCancelar = { apunteAEliminar = null },
        )
    }

    if (mostrarHorarioDialog) {
        HorarioDialog(
            horario = horarioEditando,
            onGuardar = { dia, inicio, fin ->
                onGuardarHorario(horarioEditando, dia, inicio, fin)
                mostrarHorarioDialog = false
            },
            onCerrar = { mostrarHorarioDialog = false },
        )
    }
    horarioAEliminar?.let { horario ->
        ConfirmarEliminarDialog(
            titulo = dias[horario.diaSemana - 1],
            mensaje = stringResource(R.string.horario_eliminar_mensaje),
            onConfirmar = {
                onEliminarHorario(horario)
                horarioAEliminar = null
            },
            onCancelar = { horarioAEliminar = null },
        )
    }
}

@Composable
private fun TextoVacio(texto: String) {
    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Text(texto, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ApunteDialog(
    apunte: Apunte?,
    onGuardar: (titulo: String, contenido: String) -> Unit,
    onCerrar: () -> Unit,
) {
    var titulo by remember { mutableStateOf(apunte?.titulo ?: "") }
    var contenido by remember { mutableStateOf(apunte?.contenido ?: "") }
    var intentoEnvio by remember { mutableStateOf(false) }
    val tituloValido = titulo.isNotBlank()

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text(stringResource(if (apunte == null) R.string.apunte_nuevo else R.string.apunte_editar)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text(stringResource(R.string.apunte_titulo)) },
                    singleLine = true,
                    isError = intentoEnvio && !tituloValido,
                    supportingText = {
                        if (intentoEnvio && !tituloValido) Text(stringResource(R.string.apunte_titulo_error))
                    },
                )
                OutlinedTextField(
                    value = contenido,
                    onValueChange = { contenido = it },
                    label = { Text(stringResource(R.string.apunte_contenido)) },
                    minLines = 3,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                intentoEnvio = true
                if (tituloValido) onGuardar(titulo, contenido)
            }) { Text(stringResource(R.string.boton_guardar)) }
        },
        dismissButton = { TextButton(onClick = onCerrar) { Text(stringResource(R.string.boton_cancelar)) } },
    )
}

@Composable
private fun HorarioDialog(
    horario: HorarioClase?,
    onGuardar: (dia: Int, horaInicio: String, horaFin: String) -> Unit,
    onCerrar: () -> Unit,
) {
    val diasCorto = stringArrayResource(R.array.dias_corto)
    var dia by remember { mutableIntStateOf(horario?.diaSemana ?: 1) }
    var inicio by remember { mutableStateOf(horario?.horaInicio ?: "") }
    var fin by remember { mutableStateOf(horario?.horaFin ?: "") }
    var intentoEnvio by remember { mutableStateOf(false) }

    val horasValidas = inicio.esHoraValida() && fin.esHoraValida()
    // Como el formato es HH:mm, comparar texto equivale a comparar horas.
    val ordenValido = horasValidas && fin > inicio

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text(stringResource(if (horario == null) R.string.horario_nuevo else R.string.horario_editar)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    diasCorto.forEachIndexed { indice, nombre ->
                        FilterChip(
                            selected = dia == indice + 1,
                            onClick = { dia = indice + 1 },
                            label = { Text(nombre) },
                        )
                    }
                }
                OutlinedTextField(
                    value = inicio,
                    onValueChange = { inicio = it.trim() },
                    label = { Text(stringResource(R.string.horario_hora_inicio)) },
                    singleLine = true,
                    isError = intentoEnvio && !inicio.esHoraValida(),
                    supportingText = {
                        if (intentoEnvio && !inicio.esHoraValida()) Text(stringResource(R.string.horario_hora_error))
                    },
                )
                OutlinedTextField(
                    value = fin,
                    onValueChange = { fin = it.trim() },
                    label = { Text(stringResource(R.string.horario_hora_fin)) },
                    singleLine = true,
                    isError = intentoEnvio && !ordenValido,
                    supportingText = {
                        if (intentoEnvio && !fin.esHoraValida()) {
                            Text(stringResource(R.string.horario_hora_error))
                        } else if (intentoEnvio && !ordenValido) {
                            Text(stringResource(R.string.horario_orden_error))
                        }
                    },
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                intentoEnvio = true
                if (ordenValido) onGuardar(dia, inicio, fin)
            }) { Text(stringResource(R.string.boton_guardar)) }
        },
        dismissButton = { TextButton(onClick = onCerrar) { Text(stringResource(R.string.boton_cancelar)) } },
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetallePreview() {
    AulaNotaTheme {
        DetalleMateriaScreen(
            estado = DetalleUiState(
                materia = Materia(id = 1, idUsuario = 1, nombre = "Programación Móvil", profesor = "Ing. Gómez", aula = "Lab 2"),
                apuntes = listOf(Apunte(id = 1, idMateria = 1, titulo = "Room", contenido = "Entity, Dao, Database", fechaCreacion = "2026-11-22 10:00")),
                horarios = listOf(HorarioClase(id = 1, idMateria = 1, diaSemana = 1, horaInicio = "08:00", horaFin = "10:00")),
            ),
            onVolver = {},
            onGuardarApunte = { _, _, _ -> },
            onEliminarApunte = {},
            onGuardarHorario = { _, _, _, _ -> },
            onEliminarHorario = {},
        )
    }
}
