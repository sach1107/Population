package com.sachin.population.presentation.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AlertDialogComponent(
    title: String,
    message: String? = null,
    confirmButtonText: String? = null,
    dismissButtonText: String? = null,
    onConfirmButtonClick: (() -> Unit)? = null,
    onDismissButtonClick: (() -> Unit)? = null,
    onClose: () -> Unit
) {
    AlertDialog(
        title = {
            Text(
                text = title,
            )
        },
        text = {
            message?.let {
                Text(
                    text = it,
                )
            }
        },
        onDismissRequest = onClose,
        confirmButton = {
            confirmButtonText?.let {
                TextButton(
                    onClick = {
                        onConfirmButtonClick?.invoke()
                    }
                ) {
                    Text(it)
                }
            }
        },
        dismissButton = {
            dismissButtonText?.let {
                TextButton(
                    onClick = {
                        onDismissButtonClick?.invoke()
                    }
                ) {
                    Text(it)
                }
            }
        },
    )
}