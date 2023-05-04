package com.example.emafoods.core.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import com.example.emafoods.R

@Composable
fun ScreenBackground(
    modifier: Modifier = Modifier
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            androidx.compose.material3.MaterialTheme.colorScheme.secondary
        ),
        startY = sizeImage.height.toFloat(),
        endY = sizeImage.height.toFloat() / 4,
    )

    Box() {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = modifier
                .onGloballyPositioned {
                    sizeImage = it.size
                }
                .fillMaxSize(),
            alpha = 0.2f
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0.7f)
                .background(gradient)
        )
    }
}