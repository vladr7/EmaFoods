package com.example.emafoods.core.presentation.common.alert

import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun AlertDialog2Buttons(
    modifier: Modifier = Modifier,
    title: String,
    dismissText: String = "",
    confirmText: String = "",
    onDismissClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        iconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        textContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = modifier,
        onDismissRequest = { onDismissClick() },
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
            )
        },
        confirmButton = {
            Text(
                text = confirmText,
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { onConfirmClick() }
            )
        },
        dismissButton = {
            Text(
                text = dismissText,
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { onDismissClick() }
            )
        }
    )
}