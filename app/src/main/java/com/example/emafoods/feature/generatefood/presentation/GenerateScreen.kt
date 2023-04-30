package com.example.emafoods.feature.generatefood.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.emafoods.R

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
fun GenerateDescription(modifier: Modifier, description: String) {
    Text(
        text = description,
        modifier = modifier.padding(10.dp),
        style = MaterialTheme.typography.subtitle1
    )
}

@Composable
fun GenerateImage(generatedImagedRef: String, modifier: Modifier) {
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
    Button(onClick = {
        onGenerateClick()
    }) {
        Text(text = stringResource(R.string.generate))
    }
}