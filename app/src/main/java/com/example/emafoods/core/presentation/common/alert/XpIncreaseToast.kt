package com.example.emafoods.core.presentation.common.alert

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.emafoods.R
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType

@Composable
fun XpIncreaseToast(
    modifier: Modifier = Modifier,
    increaseXpActionType: IncreaseXpActionType,
    onToastShown: () -> Unit = {},
    context: Context,
    xpIncreased: Long
) {
    when(increaseXpActionType) {
        IncreaseXpActionType.RECIPE_ACCEPTED -> {
            Toast.makeText(context,
                stringResource(R.string.you_gained_points, xpIncreased), Toast.LENGTH_SHORT).show()
            onToastShown()
        }
        else -> {
            Toast.makeText(context, stringResource(R.string.you_gained_points, xpIncreased), Toast.LENGTH_SHORT).show()
            onToastShown()
        }
    }

}

const val XP_INCREASE_THRESHOLD = 50 // show toast every 'X' xp