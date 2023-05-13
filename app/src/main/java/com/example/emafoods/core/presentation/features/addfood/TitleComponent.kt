package com.example.emafoods.core.presentation.features.addfood

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalTextApi::class)
@Composable
fun TitleComponent(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        text = text,
        fontSize = 36.sp,
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
    )
}