package com.example.emafoods.feature.addfood.presentation.insert

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.emafoods.R
import com.example.emafoods.core.extension.getCompressedImage
import com.example.emafoods.core.presentation.animations.LoadingButton
import com.example.emafoods.core.presentation.animations.LottieAnimationContent
import com.example.emafoods.core.presentation.common.BackgroundTopToBot
import com.example.emafoods.feature.addfood.presentation.description.DescriptionScreenInput
import com.example.emafoods.feature.addfood.presentation.image.AttachFileIcon
import com.example.emafoods.feature.addfood.presentation.image.TakePictureIcon
import com.example.emafoods.feature.generatefood.presentation.LoadingCookingAnimation
import kotlinx.coroutines.launch


@Composable
fun InsertFoodRoute(
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit,
    viewModel: InsertFoodViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val coroutine = rememberCoroutineScope()

    if (state.insertFoodSuccess) {
        viewModel.onXpIncrease()
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
            onUriChanged = { uri ->
                coroutine.launch {
                    val compressedUri = uri?.getCompressedImage(context)
                    // todo add a loading
                    viewModel.onSelectedNewImage()
                    viewModel.updateImageUri(compressedUri)
                }
            },
            loading = state.isLoading
        )
    }
    if (state.errorMessage?.isNotEmpty() == true) {
        Toast.makeText(context, "${state.errorMessage}", Toast.LENGTH_LONG).show()
        viewModel.hideError()
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
            onUriChangedChoseFile = onUriChanged,
            onUriChangedTakePicture = onUriChanged
        )
        DescriptionScreenInput(
            modifier = modifier
                .weight(1f),
            onDescriptionChange = onDescriptionChange,
            description = description
        )
//        AddRecipeButton(modifier, onInsertFoodClick, loading)
    }

}

@Composable
private fun AddRecipeButton(
    modifier: Modifier,
    onInsertFoodClick: () -> Unit,
    loading: Boolean
) {
    Row(
        modifier = modifier
            .padding(bottom = 32.dp, end = 24.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        LottieAnimationContent(
            animationId = R.raw.insertfoodplant,
            modifier = modifier
                .size(50.dp)
                .padding(end = 8.dp),
            color = MaterialTheme.colorScheme.onSecondary,
            speed = 0.3f,
            iterations = 1
        )
        LoadingButton(
            onClick = onInsertFoodClick,
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

@Composable
fun InsertFoodImage(
    imageUri: Uri?,
    modifier: Modifier = Modifier,
    onUriChangedChoseFile: (Uri?) -> Unit,
    onUriChangedTakePicture: (Uri?) -> Unit
) {
    val context = LocalContext.current
    val hasImage = imageUri != null && imageUri.toString().isNotEmpty()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(20.dp)
            .shadow(
                elevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
                clip = true
            )
    ) {
        SubcomposeAsyncImage(
            modifier = modifier,
            model = ImageRequest.Builder(context)
                .data(imageUri)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            loading = {
                LoadingCookingAnimation()
            },
            error = {
                if (imageUri.toString().isNotEmpty()) {
                    Toast.makeText(
                        context,
                        stringResource(R.string.error_loading_picture),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
        Column(
            modifier = modifier
                .align(Alignment.TopEnd),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .padding(top = 8.dp)
            ) {
                if(!hasImage) {
                    Text(
                        text = stringResource(R.string.choose_from_gallery),
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontWeight = FontWeight.Bold,
                    )
                }
                AttachFileIcon(
                    onUriRetrieved = {
                        onUriChangedChoseFile(it)
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if(!hasImage) {
                    Text(
                        text = stringResource(R.string.take_a_picture),
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontWeight = FontWeight.Bold,
                    )
                }
                TakePictureIcon(
                    onUriRetrieved = {
                        onUriChangedTakePicture(it)
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
}




