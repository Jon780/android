package com.aulanota.app.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.aulanota.app.R

/** Diálogo reutilizable para confirmar antes de eliminar (DELETE). */
@Composable
fun ConfirmarEliminarDialog(
    titulo: String,
    mensaje: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(titulo) },
        text = { Text(mensaje) },
        confirmButton = {
            TextButton(onClick = onConfirmar) { Text(stringResource(R.string.boton_eliminar)) }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text(stringResource(R.string.boton_cancelar)) }
        },
    )
}
