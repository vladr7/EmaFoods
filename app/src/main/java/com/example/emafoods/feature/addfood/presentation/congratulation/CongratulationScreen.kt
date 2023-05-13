package com.example.emafoods.feature.addfood.presentation.congratulation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.emafoods.R
import com.example.emafoods.core.presentation.animations.bounceClick

@Composable
fun CongratulationRoute(
    modifier: Modifier = Modifier,
    onInsertNewFoodClick: () -> Unit,
    viewModel: CongratulationViewModel = hiltViewModel(),
) {

    CongratulationScreen(
        modifier = modifier,
        onInsertFoodClick = onInsertNewFoodClick
    )
}

@Composable
fun CongratulationScreen(
    modifier: Modifier = Modifier,
    onInsertFoodClick: () -> Unit,
) {

    CongratulationBackground(imageId = R.drawable.cutecelebrationbackgr)
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        CongratulationsAnimation(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
        InsertNewFoodButton(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            onInsertFoodClick = onInsertFoodClick
        )
    }
}

@Composable
fun InsertNewFoodButton(
    modifier: Modifier = Modifier,
    onInsertFoodClick: () -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .padding(bottom = 32.dp, end = 24.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.bounceClick(),
            onClick = onInsertFoodClick,
            enabled = enabled,
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add recipe",
                tint = MaterialTheme.colorScheme.onSecondary,
            )
            Text(
                stringResource(R.string.add_recipe),
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Composable
fun CongratulationsAnimation(
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.congratulationanimation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = 1f
    )
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { progress },
    )
}

@Composable
fun CongratulationBackground(
    modifier: Modifier = Modifier,
    imageId: Int,
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            MaterialTheme.colorScheme.secondary
        ),
        startY = 100f,
        endY = sizeImage.height.toFloat(),
    )

    Box() {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = modifier
                .onGloballyPositioned {
                    sizeImage = it.size
                }
                .fillMaxSize(),
            alpha = 0.60f
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0.45f)
                .background(gradient)
        )
    }
}