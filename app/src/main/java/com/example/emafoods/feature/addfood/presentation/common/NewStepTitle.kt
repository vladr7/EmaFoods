package com.example.emafoods.feature.addfood.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitleWithBackground(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 36.sp,
    textAlign: TextAlign = TextAlign.Center
) {

    val gradient = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondary,
            Color.Transparent
        ),
        startX = 300.0f,
        endX = 0.0f
    )

    Text(
        text = " $text ",
        fontSize = fontSize,
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
        textAlign = textAlign,
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp)
            .background(
                brush = gradient
            )

    )
}

