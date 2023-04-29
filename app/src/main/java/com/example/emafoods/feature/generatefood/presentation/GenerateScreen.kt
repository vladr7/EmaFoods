package com.example.emafoods.feature.generatefood.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun GenerateScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: GenerateViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GenerateScreen(
        state.food.imageRef,
        modifier = modifier
    )
}

@Composable
fun GenerateScreen(
    generatedImagedRef: String,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        modifier = Modifier
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
