package com.example.emafoods.feature.generatefood.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.emafoods.R
import kotlinx.coroutines.launch

@Composable
fun GenerateScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: GenerateViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GenerateScreen(
        generatedImagedRef = state.food.imageRef,
        modifier = modifier,
        onGenerateClick = {
            viewModel.generateFoodEvent()
        },
        title = state.food.title,
        description = state.food.description
    )
}

@Composable
fun GenerateScreen(
    generatedImagedRef: String,
    onGenerateClick: () -> Unit,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GenerateImage(generatedImagedRef, modifier)
        GenerateTitle(modifier, title)
        GenerateDescription(modifier, description)
        GenerateButton(
            modifier = modifier,
            onGenerateClick = onGenerateClick
        )
    }
}


@Composable
fun GenerateTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        text = title,
        modifier = modifier.padding(10.dp),
        style = MaterialTheme.typography.h4
    )
}

@Composable
fun GenerateDescription(
    modifier: Modifier,
    description: String
) {
    Text(
        text = description,
        modifier = modifier.padding(10.dp),
        style = MaterialTheme.typography.subtitle1
    )
}

@Composable
fun GenerateImage(
    generatedImagedRef: String,
    modifier: Modifier
) {
    SubcomposeAsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(10.dp)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(8.dp)),
        model = ImageRequest.Builder(LocalContext.current)
            .data(generatedImagedRef)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        loading = {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(100.dp)
            )
        }
    )
}

@Composable
fun GenerateButton(
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }

    Box(
        modifier = modifier
            .offset(
                y = if (offsetY.value < -100f) {
                    maxOf(-100f, offsetY.value).dp
                } else if (offsetY.value > 0f) {
                    minOf(offsetY.value, 0f).dp
                } else {
                    offsetY.value.dp
                }
            )
            .draggable(
                state = rememberDraggableState { delta ->
                    coroutineScope.launch {
                        offsetY.animateTo(offsetY.value + delta)
                    }
                },
                orientation = Orientation.Vertical,
                onDragStarted = {

                },
                onDragStopped = {
                    if (offsetY.value < -40f) {
                        onGenerateClick()
                    }
                    coroutineScope.launch {
                        offsetY.animateTo(0f)
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.generate_button_text),
            fontSize = androidx.compose.material3.MaterialTheme.typography.titleLarge.fontSize,
            modifier = modifier
                .zIndex(1f)
                .offset(y = 70.dp),
            color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
        )
        ArcComposable(modifier = modifier)
    }

}

@Composable
private fun ArcComposable(modifier: Modifier = Modifier) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme.primary
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(0f)

    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val formWidth = (canvasWidth * 2)
        val xPos = canvasWidth / 2

        drawArc(
            color = color,
            0f,
            -180f,
            useCenter = true,
            size = Size(formWidth, 1700f),
            topLeft = Offset(x = -xPos, y = canvasHeight - 350)
        )
    }
}