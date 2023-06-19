package com.example.emafoods.core.presentation.common.alert

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emafoods.R

@Composable
fun BackButtonAskExitDialog(
    modifier: Modifier = Modifier,
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit,
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
                text = stringResource(R.string.are_you_sure_you_want_to_exit),
                fontSize = 20.sp,
            )
        },
        dismissButton = {
            Text(
                text = stringResource(R.string.i_want_to_stay),
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { onDismissClick() }
                    .padding(16.dp)
            )
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.exit),
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { onConfirmClick() }
                    .padding(16.dp)
            )
        },
    )
}