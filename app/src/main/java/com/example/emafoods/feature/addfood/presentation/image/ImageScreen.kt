package com.example.emafoods.feature.addfood.presentation.image

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.R
import com.example.emafoods.core.extension.getCompressedImage
import com.example.emafoods.core.presentation.animations.bounceClick
import com.example.emafoods.feature.addfood.data.composefileprovider.ComposeFileProvider
import com.example.emafoods.feature.addfood.presentation.common.NextStepButton
import com.example.emafoods.feature.addfood.presentation.common.TitleWithBackground
import com.example.emafoods.feature.addfood.presentation.image.navigation.IMAGE_FROM_GALLERY_FLAG
import com.example.emafoods.feature.addfood.presentation.insert.InsertFoodImage
import com.example.emafoods.feature.addfood.presentation.insert.navigation.InsertFoodArguments
import kotlinx.coroutines.launch


@Composable
fun ImageRoute(
    onNextClicked: (InsertFoodArguments) -> Unit,
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
                InsertFoodArguments(
                    category = state.category,
                    uri = imageUri ?: IMAGE_FROM_GALLERY_FLAG,
                    title = state.title,
                )
            )
        },
        imageUri = Uri.parse(state.imageUri),
        title = state.title,
        onTitleChange = { title ->
            viewModel.updateTitle(title)
        }
    )
}

@Composable
fun ImageScreen(
    modifier: Modifier = Modifier,
    onChoosePictureUriRetrieved: (Uri?) -> Unit,
    onTakePictureUriRetrieved: (Uri?) -> Unit,
    hasTakePictureImage: Boolean,
    imageUri: Uri,
    onNextClicked: (String?) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit
) {
    ImageScreenBackground()
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column {
            TitleWithBackground(
                text = stringResource(id = R.string.add_image_title),
                fontSize = 26.sp,
                modifier = modifier
                    .padding(bottom = 8.dp, top = 20.dp),
            )
            InsertFoodImage(
                imageUri = imageUri,
                modifier = modifier,
                onUriChangedChoseFile = onChoosePictureUriRetrieved,
                onUriChangedTakePicture = onTakePictureUriRetrieved
            )
            TitleWithBackground(
                text = stringResource(R.string.title_recipe), fontSize = 26.sp,
                modifier = modifier
                    .padding(bottom = 4.dp)
                    .fillMaxWidth(),
            )
            TitleScreenInput(onTitleChange = onTitleChange, title = title,
                modifier = modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = modifier.weight(0.5f))
            ImageScreenNextStep(
                modifier
                    .weight(1f),
                hasTakePictureImage,
                onNextClicked,
                imageUri,
                title,
            )
            Spacer(modifier = modifier.weight(0.5f))
        }

    }
}

@Composable
private fun ImageScreenNextStep(
    modifier: Modifier,
    hasTakePictureImage: Boolean,
    onNextClicked: (String?) -> Unit,
    imageUri: Uri,
    title: String
) {
    Row(
        modifier = modifier
    ) {
        Spacer(modifier = modifier.weight(1f))
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NextStepButton(
                modifier = modifier
                    .padding(end = 20.dp, bottom = 20.dp),
                onConfirmedClick = {
                    if (hasTakePictureImage) {
                        onNextClicked(imageUri.toString())
                    } else {
                        onNextClicked(null)
                    }
                }, visible = imageUri.toString().isNotEmpty() && title.length >= 5
            )
        }
    }
}

@Composable
fun TitleScreenInput(
    modifier: Modifier = Modifier,
    onTitleChange: (String) -> Unit,
    title: String
) {
    val maxChars = 60
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val toastErrorMessage = stringResource(id = R.string.title_must_contain_min_chars_error)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = {
                if (it.length <= maxChars) {
                    onTitleChange(it)
                }
            },
            label = {
                if (title.length <= 5) {
                    Text(
                        text = stringResource(id = R.string.title_empty_input_label_text_min_chars),
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.title_recipe),
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth(),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily
            ),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = MaterialTheme.colorScheme.onSecondary,
                focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (title.length >= 5) {
                        focusManager.clearFocus()
                    } else {
                        Toast.makeText(context, toastErrorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            ),
        )
        Text(
            text = (maxChars - title.count()).toString(),
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 20.dp, end = 10.dp)
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
    modifier: Modifier = Modifier,
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            MaterialTheme.colorScheme.secondary
        ),
        startY = -1500f,
        endY = sizeImage.height.toFloat(),
    )

    Box() {
        Image(
            painter = painterResource(R.drawable.imagefoodbackgr),
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
                .alpha(0.95f)
                .background(gradient)
        )
    }
}
