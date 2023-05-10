package com.example.emafoods.feature.addfood.presentation.insert

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.example.emafoods.core.presentation.common.BackgroundTopToBot
import com.example.emafoods.core.presentation.common.keyboardAsState
import com.example.emafoods.feature.addfood.presentation.description.DescriptionScreenInput
import com.example.emafoods.feature.addfood.presentation.title.TitleScreenInput
import com.example.emafoods.feature.generatefood.presentation.LoadingCookingAnimation


@Composable
fun InsertFoodRoute(
    modifier: Modifier = Modifier,
    onConfirmedClick: () -> Unit,
    viewModel: InsertFoodViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    InsertFoodScreen(
        modifier = modifier,
        imageUri = state.imageUri,
        onTitleChange = { viewModel.updateTitle(it) },
        title = state.title,
        onDescriptionChange = { viewModel.updateDescription(it) },
        description = state.description,
        onConfirmedClick = onConfirmedClick
    )
}

@Composable
fun InsertFoodScreen(
    imageUri: Uri?,
    modifier: Modifier = Modifier,
    onTitleChange: (String) -> Unit,
    title: String,
    onDescriptionChange: (String) -> Unit,
    description: String,
    onConfirmedClick: () -> Unit
) {
    val isKeyboardVisible by keyboardAsState()

    BackgroundTopToBot(
        imageId = R.drawable.descriptionbackgr
    )
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column {
            InsertFoodImage(
                imageUri = imageUri,
                modifier = modifier
            )
            TitleScreenInput(
                onTitleChange = onTitleChange,
                title = title
            )
            DescriptionScreenInput(
                modifier = modifier
                    .fillMaxHeight(),
                onDescriptionChange = onDescriptionChange,
                description = description
            )
        }
        if(!isKeyboardVisible) {
            InsertFoodButton(
                modifier = modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 32.dp, end = 40.dp),
                onConfirmedClick = onConfirmedClick
            )
        }
    }
}

@Composable
fun InsertFoodImage(
    imageUri: Uri?,
    modifier: Modifier = Modifier,
) {
    SubcomposeAsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(20.dp)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(8.dp)),
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUri ?: R.drawable.background)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        loading = {
            LoadingCookingAnimation()
        },
        error = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(20.dp)
                    .shadow(elevation = 16.dp, shape = RoundedCornerShape(8.dp)),
            ) {
                Text(text = "Error loading image ${it.result.throwable.message}")
            }
        }
    )
}

@Composable
fun InsertFoodButton(
    modifier: Modifier = Modifier,
    onConfirmedClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = onConfirmedClick,
        icon = {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add recipe"
            )
        },
        text = { Text(stringResource(R.string.add_recipe)) }
    )
}

