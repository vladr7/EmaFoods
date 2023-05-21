package com.example.emafoods.core.presentation.common.alert

import androidx.compose.runtime.Composable
import com.example.emafoods.feature.game.domain.model.UserLevel

@Composable
fun LevelUpDialog(newLevel: UserLevel?, onDismiss: () -> Unit) {

    AlertDialog2Buttons(
        title = "Level Up to ${newLevel?.string}!",
        confirmText = "OK",
        onConfirmClick = { onDismiss() },
    )
}