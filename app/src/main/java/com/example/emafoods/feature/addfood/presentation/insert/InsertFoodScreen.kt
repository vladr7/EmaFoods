package com.example.emafoods.feature.addfood.presentation.insert

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.emafoods.R
import com.example.emafoods.core.presentation.animations.LoadingButton
import com.example.emafoods.core.presentation.common.BackgroundTopToBot
import com.example.emafoods.feature.addfood.presentation.description.DescriptionScreenInput
import com.example.emafoods.feature.addfood.presentation.image.AttachFileIcon
import com.example.emafoods.feature.addfood.presentation.image.TakePictureIcon
import com.example.emafoods.feature.generatefood.presentation.LoadingCookingAnimation


@Composable
fun InsertFoodRoute(
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit,
    viewModel: InsertFoodViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.insertFoodSuccess) {
        viewModel.resetState()
        onSuccess()
    } else {
        InsertFoodScreen(
            modifier = modifier,
            imageUri = state.imageUri,
            onDescriptionChange = { viewModel.updateDescription(it) },
            description = state.description,
            onInsertFoodClick = {
                viewModel.insertFood(
                    description = state.description,
                    imageUri = state.imageUri
                )
            },
            onUriChanged = { viewModel.updateImageUri(it) },
            enabledAddFoodButton = state.enabledAddFoodButton,
            loading = state.isLoading
        )
    }
}

@Composable
fun InsertFoodScreen(
    imageUri: Uri?,
    modifier: Modifier = Modifier,
    onDescriptionChange: (String) -> Unit,
    description: String,
    onInsertFoodClick: () -> Unit,
    onUriChanged: (Uri?) -> Unit,
    enabledAddFoodButton: Boolean,
    loading: Boolean
) {

    BackgroundTopToBot(
        imageId = R.drawable.descriptionbackgr
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        InsertFoodImage(
            imageUri = imageUri,
            modifier = modifier,
            onUriChanged = onUriChanged
        )
        DescriptionScreenInput(
            modifier = modifier
                .weight(1f),
            onDescriptionChange = onDescriptionChange,
            description = description
        )
        Row(
            modifier = modifier
                .padding(bottom = 32.dp, end = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            LoadingButton(
                onClick = onInsertFoodClick,
                enabled = enabledAddFoodButton,
                loading = loading,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
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
    }

}

@Composable
fun InsertFoodImage(
    imageUri: Uri?,
    modifier: Modifier = Modifier,
    onUriChanged: (Uri?) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(20.dp)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(8.dp)),
    ) {
        SubcomposeAsyncImage(
            modifier = modifier,
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
        Column(
            modifier = modifier
                .align(Alignment.TopEnd)
        ) {
            AttachFileIcon(
                onUriRetrieved = {
                    onUriChanged(it)
                },
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,
                                Color.Transparent
                            )
                        )
                    ),
            )
            TakePictureIcon(
                onUriRetrieved = {
                    onUriChanged(it)
                },
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,
                                Color.Transparent
                            )
                        )
                    ),
            )
        }

    }
}

@Composable
fun InsertFoodButton(
    modifier: Modifier = Modifier,
    onInsertFoodClick: () -> Unit,
    shouldShowButton: Boolean = false
) {
    Row(
        modifier = modifier
            .padding(bottom = 32.dp, end = 24.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier,
            onClick = onInsertFoodClick,
            enabled = shouldShowButton,
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


