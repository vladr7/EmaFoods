package com.example.emafoods.core.presentation.animations

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val IndicatorSize = 8
private const val AnimationDurationMillis = 300
private const val NumIndicators = 3
private const val AnimationDelayMillis = AnimationDurationMillis / NumIndicators
private const val MarginHalf = 4

@Composable
fun LoadingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    indicatorSpacing: Dp = 12.dp,
    content: @Composable () -> Unit,
) {
    val contentAlpha by animateFloatAsState(targetValue = if (loading) 0f else 1f)
    val loadingAlpha by animateFloatAsState(targetValue = if (loading) 1f else 0f)

    Button(
        onClick = onClick,
        modifier = modifier
            .bounceClick(),
        enabled = enabled,
        colors = colors,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            LoadingIndicator(
                animating = loading,
                modifier = Modifier.graphicsLayer { alpha = loadingAlpha },
                color = MaterialTheme.colorScheme.onSecondary,
                indicatorSpacing = indicatorSpacing,
            )
            Box(
                modifier = Modifier.graphicsLayer { alpha = contentAlpha }
            ) {
                content()
            }
        }
    }
}


@Composable
private fun LoadingIndicator(
    // 1
    animating: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    indicatorSpacing: Dp = MarginHalf.dp,
) {
    val animatedValues = List(NumIndicators) { index ->
        // 2
        var animatedValue by remember(key1 = animating) { mutableStateOf(0f) }
        // 3
        LaunchedEffect(key1 = animating) {
            if (animating) {
                animate(
                    initialValue = IndicatorSize / 2f,
                    targetValue = -IndicatorSize / 2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = AnimationDurationMillis),
                        repeatMode = RepeatMode.Reverse,
                        initialStartOffset = StartOffset(AnimationDelayMillis * index),
                    ),
                ) { value, _ -> animatedValue = value }
            }
        }
        animatedValue
    }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        animatedValues.forEach { animatedValue ->
            LoadingDot(
                modifier = Modifier
                    .padding(horizontal = indicatorSpacing)
                    .width(IndicatorSize.dp)
                    .aspectRatio(1f)
                    .offset(y = animatedValue.dp),
                color = color,
            )
        }
    }
}

@Composable
private fun LoadingDot(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = color)
    )
}