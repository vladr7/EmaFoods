package com.example.emafoods.feature.addfood.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.emafoods.R

@Composable
fun NextStepButton(
    modifier: Modifier = Modifier,
    onConfirmedClick: () -> Unit,
    visible: Boolean
) {
    AnimatedVisibility(visible = visible,
        enter = slideInHorizontally {
            it
        },
        exit = slideOutHorizontally(
            targetOffsetX = { it }
        ),
        modifier = modifier,
    ) {
        ExtendedFloatingActionButton(
            onClick = onConfirmedClick,
            shape = CircleShape,
        ) {
            Text(
                text = stringResource(R.string.next_step),
            )
            Icon(
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = "Next Step"
            )
        }
    }
}