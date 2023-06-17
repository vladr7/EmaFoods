package com.example.emafoods.feature.addfood.presentation.image

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.emafoods.core.presentation.animations.bounceClick
import com.example.emafoods.feature.addfood.data.composefileprovider.ComposeFileProvider
import com.example.emafoods.feature.addfood.presentation.common.TitleWithBackground
import com.example.emafoods.feature.addfood.presentation.common.NextStepButton
import com.example.emafoods.feature.addfood.presentation.common.StepIndicator
import com.example.emafoods.feature.addfood.presentation.image.navigation.IMAGE_FROM_GALLERY_FLAG
import com.example.emafoods.feature.addfood.presentation.ingredients.navigation.IngredientsArguments
import com.example.emafoods.feature.addfood.presentation.insert.InsertFoodImage
import kotlinx.coroutines.launch


@Composable
fun ImageRoute(
    onNextClicked: (IngredientsArguments) -> Unit,
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
                IngredientsArguments(
                    category = state.category,
                    uri = imageUri ?: IMAGE_FROM_GALLERY_FLAG,
                )
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
        TitleWithBackground(text = stringResource(id = R.string.add_image_title))
        InsertFoodImage(
            imageUri = imageUri,
            modifier = modifier,
            onUriChangedChoseFile = onChoosePictureUriRetrieved,
            onUriChangedTakePicture = onTakePictureUriRetrieved
        )
        Row {
            Spacer(modifier = modifier.weight(1f))
            Column(
                modifier = modifier,
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            ) {
                NextStepButton(
                    modifier = modifier,
                    onConfirmedClick = {
                        if (hasTakePictureImage) {
                            onNextClicked(imageUri.toString())
                        } else {
                            onNextClicked(null)
                        }
                    }, visible = imageUri.toString().isNotEmpty()
                )
                HangingPlantAnimation(
                    visible = imageUri.toString().isNotEmpty(),
                    modifier = modifier
                )
            }
        }

    }
}


@Composable
fun HangingPlantAnimation(
    modifier: Modifier = Modifier,
    visible: Boolean,
) {
    AnimatedVisibility(visible = visible,
        enter = slideInHorizontally {
            it
        },
        exit = slideOutHorizontally(
            targetOffsetX = { it }
        )
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.hangingplant))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            speed = 1f
        )
        LottieAnimation(
            modifier = modifier
                .offset(y = ((-20).dp))
                .size(250.dp),
            composition = composition,
            progress = { progress },
        )
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




