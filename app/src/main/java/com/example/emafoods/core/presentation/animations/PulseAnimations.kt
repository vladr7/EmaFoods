package com.example.emafoods.core.presentation.animations

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun Pulsating(
    modifier: Modifier = Modifier,
    pulseFraction: Float = 1.2f,
    duration : Int = 1000,
    content: @Composable () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = pulseFraction,
        animationSpec = infiniteRepeatable(
            animation = tween(duration),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(modifier = modifier.scale(scale)) {
        content()
    }
}

@Composable
fun PulsatingCircle(
    modifier: Modifier = Modifier,
    delay: Int = 0,
    durationMillis: Int = 1500,
    scale: Float = 0f,
) {
    var currentScale by remember { mutableStateOf(scale) }
    if (scale == 0f) {
        currentScale = LocalConfiguration.current.screenWidthDp.toFloat()
    }
    var targetValue by remember { mutableStateOf(0f) }

    val animationProgress by animateFloatAsState(
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, delayMillis = delay),

            ),
        label = "",
    )
    val color = MaterialTheme.colorScheme.onSecondary
    SideEffect { targetValue = 1f }
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .scale(animationProgress)
    ) {
        drawCircle(
            color = color,
            alpha = 1 - animationProgress,
            center = center,
            radius = currentScale,
            style = Stroke(5f)
        )
    }
}