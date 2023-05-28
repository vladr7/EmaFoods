package com.example.emafoods.core.presentation.common.alert

import androidx.compose.runtime.Composable
import com.example.emafoods.feature.game.domain.model.UserLevel

@Composable
fun LevelUpDialog(newLevel: UserLevel?, onDismiss: () -> Unit) {

    AlertDialog2Buttons(
        title = "Nivel nou deblocat: ${newLevel?.uiString}!",
        confirmText = "OK",
        onConfirmClick = { onDismiss() },
    )
}