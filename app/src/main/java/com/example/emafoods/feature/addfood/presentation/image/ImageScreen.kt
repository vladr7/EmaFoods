package com.example.emafoods.feature.addfood.presentation.image

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.emafoods.R
import com.example.emafoods.core.extension.getCompressedImage
import com.example.emafoods.core.presentation.animations.LottieAnimationContent
import com.example.emafoods.core.presentation.animations.bounceClick
import com.example.emafoods.feature.addfood.data.composefileprovider.ComposeFileProvider
import com.example.emafoods.feature.addfood.presentation.common.AddRecipeTitle
import com.example.emafoods.feature.addfood.presentation.common.StepIndicator
import com.example.emafoods.feature.addfood.presentation.description.navigation.DescriptionArguments
import com.example.emafoods.feature.addfood.presentation.insert.InsertFoodImage
import kotlinx.coroutines.launch


@Composable
fun ImageRoute(
    onNextClicked: (DescriptionArguments?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddImageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()

    ImageScreen(
        onChoosePictureUriRetrieved = { uri ->
            uri?.let { imageUri ->
                viewModel.updateImageUri(imageUri.toString())
                viewModel.addPendingImageToTemporarilySavedImages(imageUri, context)
            }
        },
        modifier = modifier,
        onTakePictureUriRetrieved = { uri ->
            coroutine.launch {
                val compressedUri = uri?.getCompressedImage(context)
                viewModel.updateImageUri(compressedUri.toString())
                viewModel.updateHasTakePictureImage(true)
            }
        },
        hasTakePictureImage = state.hasTakePictureImage,
        onNextClicked = { imageUri ->
            onNextClicked(
                imageUri?.let { imageUriString ->
                    DescriptionArguments(
                        category = state.category,
                        uri = imageUriString
                    )
                }
            )
        },
        imageUri = Uri.parse(state.imageUri)
    )
}

@Composable
fun ImageScreen(
    modifier: Modifier = Modifier,
    onChoosePictureUriRetrieved: (Uri?) -> Unit,
    onTakePictureUriRetrieved: (Uri?) -> Unit,
    hasTakePictureImage: Boolean,
    imageUri: Uri,
    onNextClicked: (String?) -> Unit
) {
    ImageScreenBackground()
    Column {
        StepIndicator(
            modifier = modifier,
            step = 1,
        )
        AddRecipeTitle(text = stringResource(id = R.string.add_image_title))
        InsertFoodImage(
            imageUri = imageUri,
            modifier = modifier,
            onUriChangedChoseFile = onChoosePictureUriRetrieved,
            onUriChangedTakePicture = onTakePictureUriRetrieved
        )
        val visible = imageUri.toString().isNotEmpty()
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = 300
                )
            ),
        ) {
            ConfirmImageButton(
                modifier = modifier,
                onConfirmedClick = {
                    if (hasTakePictureImage) {
                        onNextClicked(imageUri.toString())
                    } else {
                        onNextClicked(null)
                    }
                }
            )
            HangingPlantAnimation(
                modifier = modifier
            )
        }
    }
}

@Composable
fun HangingPlantAnimation(
    modifier: Modifier = Modifier,
) {
    Row() {
        Spacer(modifier = modifier.weight(1f))
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.hangingplant))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            speed = 1f
        )
        LottieAnimation(
            modifier = modifier
                .offset(y = (15).dp, x = (73).dp)
                .size(250.dp),
            composition = composition,
            progress = { progress },
        )
    }
}

@Composable
fun ConfirmImageButton(
    modifier: Modifier = Modifier,
    onConfirmedClick: () -> Unit,
) {
    Row {
        Spacer(modifier = modifier.weight(1f))
        FloatingActionButton(
            modifier = modifier
                .padding(end = 24.dp),
            onClick = onConfirmedClick,
            shape = CircleShape,
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = "Add Image"
            )
        }
    }
}

@Composable
fun AddImageOptions(
    modifier: Modifier = Modifier,
    onChoosePictureUriRetrieved: (Uri?) -> Unit,
    onTakePictureUriRetrieved: (Uri?) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LottieAnimationContent(
            animationId = R.raw.addimageplant,
            modifier = modifier
                .size(250.dp)
                .align(Alignment.BottomStart)
                .padding(bottom = 16.dp)
                .offset(x = (-20).dp),
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
        ) {
            AttachFileIcon(
                onUriRetrieved = onChoosePictureUriRetrieved
            )
            TakePictureIcon(
                onUriRetrieved = onTakePictureUriRetrieved
            )
        }
    }
}

@Composable
fun AttachFileIcon(
    modifier: Modifier = Modifier,
    onUriRetrieved: (Uri?) -> Unit,
    tint: Color = MaterialTheme.colorScheme.onSecondary
) {

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            onUriRetrieved(uri)
        }
    )

    Icon(
        imageVector = Icons.Filled.AttachFile, contentDescription = null,
        modifier = modifier
            .padding(16.dp)
            .size(80.dp)
            .bounceClick()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                imagePicker.launch("image/*")
            },
        tint = tint
    )
}

@Composable
fun TakePictureIcon(
    modifier: Modifier = Modifier,
    onUriRetrieved: (Uri?) -> Unit,
    context: Context = LocalContext.current,
) {
    val uri = ComposeFileProvider.getImageUri(context)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onUriRetrieved(uri)
            }
        }
    )
    Icon(
        imageVector = Icons.Filled.PhotoCamera,
        contentDescription = null,
        modifier = modifier
            .padding(16.dp)
            .size(80.dp)
            .bounceClick()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                cameraLauncher.launch(uri)
            },
        tint = MaterialTheme.colorScheme.onSecondary
    )
}

@Composable
fun ImageScreenBackground(
    modifier: Modifier = Modifier
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color.Transparent,
            MaterialTheme.colorScheme.secondary
        ),
        start = Offset(0f, 0f),
        end = Offset(sizeImage.width.toFloat(), sizeImage.height.toFloat() / 2),
    )

    Box() {
        Image(
            painter = painterResource(id = R.drawable.imagefoodbackgr),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = modifier
                .onGloballyPositioned {
                    sizeImage = it.size
                }
                .fillMaxSize(),
            alpha = 0.35f
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(gradient)
        )
    }
}




