package com.example.emafoods.core.presentation.common.alert

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.emafoods.feature.game.presentation.model.IncreaseXpActionType

@Composable
fun XpIncreaseToast(
    modifier: Modifier = Modifier,
    increaseXpActionType: IncreaseXpActionType,
    onToastShown: () -> Unit,
    context: Context,
) {
    Toast.makeText(context, "Ai acumulat ${increaseXpActionType.xp} puncte de experienta!", Toast.LENGTH_SHORT).show()
    onToastShown()
}