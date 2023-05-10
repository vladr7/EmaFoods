package com.example.emafoods.feature.addfood.presentation.image

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.R
import com.example.emafoods.core.presentation.animations.bounceClick
import com.example.emafoods.feature.addfood.data.composefileprovider.ComposeFileProvider
import com.example.emafoods.feature.addfood.presentation.description.navigation.DescriptionArguments

@Composable
fun ImageRoute(
    onHasImage: (DescriptionArguments) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddImageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.hasImage) {
        onHasImage(
            DescriptionArguments(
                uriId = state.imageUri
            )
        )
    } else {
        ImageScreen(
            errorMessage = state.errorMessage,
            context = context,
            onUriRetrieved = { uri ->
                uri?.let {
                    viewModel.updateHasImage(true)
                    viewModel.updateImageUri(it.toString())
                }
            },
            modifier = modifier
        )
    }
}

@Composable
fun ImageScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    errorMessage: String? = null,
    imageUri: Uri? = null,
    onUriRetrieved: (Uri?) -> Unit
) {
    ImageScreenBackground()
    AddImageTitle()
    AddImageOptions(
        modifier = modifier,
        onUriRetrieved = onUriRetrieved
    )

}

@OptIn(ExperimentalTextApi::class)
@Composable
fun AddImageTitle(
    modifier: Modifier = Modifier,
) {

    val gradient = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondary,
            Color.Transparent
        ),
        startX = 300.0f,
        endX = 0.0f
    )

    Text(
        text = stringResource(id = R.string.add_image_title),
        fontSize = 36.sp,
        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
        fontWeight = FontWeight.Bold,
        style = TextStyle(
            brush = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.onSecondary,
                    MaterialTheme.colorScheme.onSecondary
                )
            )
        ),
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(20.dp, top = 40.dp, bottom = 40.dp)
            .background(
                brush = gradient
            )

    )

}

@Composable
fun AddImageOptions(
    modifier: Modifier = Modifier,
    onUriRetrieved: (Uri?) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
        ) {
            AttachFileIcon(
                onUriRetrieved = onUriRetrieved
            )
            TakePictureIcon(
                onUriRetrieved = onUriRetrieved
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
            .size(150.dp)
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
            .size(150.dp)
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




