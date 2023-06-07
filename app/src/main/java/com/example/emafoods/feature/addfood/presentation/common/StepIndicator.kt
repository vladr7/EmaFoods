package com.example.emafoods.feature.addfood.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emafoods.R

@Composable
fun StepIndicator(
    modifier: Modifier,
    step: Int
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
        ),
        startX = 300.0f,
        endX = 0.0f
    )

    Text(
        text = stringResource(id = R.string.step_indicator, step.toString()),
        fontSize = 26.sp,
        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
        fontWeight = FontWeight.Bold,
        style = TextStyle(
            brush = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.onSecondary,
                    MaterialTheme.colorScheme.onSecondary
                )
            )
        ),
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(start = 20.dp, top = 10.dp)
            .background(
                brush = gradient
            )

    )
}