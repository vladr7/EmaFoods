package com.example.emafoods.core.presentation.animations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty

@Composable
fun LottieAnimationContent(
    modifier: Modifier = Modifier,
    animationId: Int,
    speed: Float = 1f,
    iterations: Int = LottieConstants.IterateForever,
    color: Color = Color.Unspecified,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationId))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        speed = speed
    )
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = SimpleColorFilter(color.toArgb()),
            keyPath = arrayOf("**")
        ),
    )
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { progress },
        dynamicProperties = dynamicProperties,
    )
}