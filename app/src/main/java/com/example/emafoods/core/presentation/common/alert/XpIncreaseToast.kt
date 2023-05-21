package com.example.emafoods.core.presentation.common.alert

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType

@Composable
fun XpIncreaseToast(
    modifier: Modifier = Modifier,
    increaseXpActionType: IncreaseXpActionType,
    onToastShown: () -> Unit = {},
    context: Context,
    customXP: Int = 0
) {
    when(increaseXpActionType) {
        IncreaseXpActionType.FIRST_TIME_OPENING_APP_TODAY -> {
            Toast.makeText(context, "Salut din nou! Ai acumulat ${increaseXpActionType.xp} puncte de experienta!", Toast.LENGTH_SHORT).show()
            onToastShown()
        }
        IncreaseXpActionType.GENERATE_RECIPE -> {
            Toast.makeText(context, "Ai acumulat $customXP puncte de experienta!", Toast.LENGTH_SHORT).show()
            onToastShown()
        }
        else -> {
            Toast.makeText(context, "Ai acumulat ${increaseXpActionType.xp} puncte de experienta!", Toast.LENGTH_SHORT).show()
            onToastShown()
        }
    }

}

const val XP_INCREASE_THRESHOLD = 200 // show toast every 'X' xp