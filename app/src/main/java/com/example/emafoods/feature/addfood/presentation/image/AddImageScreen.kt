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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.emafoods.R
import com.example.emafoods.core.presentation.composables.bounceClick
import com.example.emafoods.feature.addfood.data.composefileprovider.ComposeFileProvider
import com.example.emafoods.feature.addfood.presentation.AddFoodViewModel

@Composable
fun AddImageRoute(
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddFoodViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    if(state.hasImage) {
        onNextClick()
    } else {
        AddImageScreen(
            errorMessage = state.errorMessage,
            context = context,
            isLoading = state.isLoading,
            imageUri = state.imageUri,
            foodTitle = state.foodTitle,
            foodDescription = state.foodDescription,
            onNextClick = onNextClick,
            onUriRetrieved = { uri ->
                viewModel.updateHasImage(uri != null)
                viewModel.updateImageUri(uri)
            },
            modifier = modifier
        )
    }
}

@Composable
fun AddImageScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    errorMessage: String? = null,
    viewModel: AddFoodViewModel = hiltViewModel(),
    isLoading: Boolean = false,
    imageUri: Uri? = null,
    foodTitle: String,
    foodDescription: String,
    onNextClick: () -> Unit,
    onUriRetrieved: (Uri?) -> Unit
) {
    AddImageScreenBackground()
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
            TakePictureIcon()
        }
    }
}

@Composable
fun AttachFileIcon(
    modifier: Modifier = Modifier,
    onUriRetrieved: (Uri?) -> Unit,
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
        tint = MaterialTheme.colorScheme.onSecondary
    )
}

@Composable
fun TakePictureIcon(
    modifier: Modifier = Modifier,
) {

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
            },
        tint = MaterialTheme.colorScheme.onSecondary
    )
}

@Composable
fun AddImageScreenBackground(
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

@Composable
fun AddFoodScreenTemp(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    errorMessage: String? = null,
    viewModel: AddFoodViewModel = hiltViewModel(),
    isLoading: Boolean = false,
    hasImage: Boolean = false,
    imageUri: Uri? = null,
    foodTitle: String,
    foodDescription: String,
) {

    if (errorMessage != null) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val imagePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                viewModel.updateHasImage(uri != null)
                viewModel.updateImageUri(uri)
            }
        )

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                viewModel.updateHasImage(success)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.padding(24.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    val uri = ComposeFileProvider.getImageUri(context)
                    viewModel.updateImageUri(uri)
                    cameraLauncher.launch(uri)
                }) {
                    androidx.compose.material.Text(text = "Camera")
                }

                androidx.compose.material.Text(
                    "sau",
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )

                Button(onClick = {
                    imagePicker.launch("image/*")
                }) {
                    androidx.compose.material.Text(text = "Selecteaza din telefon")
                }
            }

            Spacer(modifier = Modifier.padding(16.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(150.dp)
                )
            }

            if (hasImage && imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),
                    contentDescription = "Selected image",
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_no_photography_24),
                    contentDescription = null,
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            OutlinedTextField(
                value = foodTitle,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp, top = 16.dp),
                onValueChange = {
                    if (it.length <= 40) {
                        viewModel.updateFoodTitle(it)
                    }
                },
                label = {
                    androidx.compose.material.Text(stringResource(R.string.title))
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                keyboardActions = KeyboardActions(
                    onDone = {

                    }
                ),
            )

            OutlinedTextField(
                value = foodDescription,
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(start = 32.dp, end = 32.dp, top = 16.dp),
                onValueChange = {
                    if (it.length <= 400) {
                        viewModel.updateFoodDescription(it)
                    }
                },
                label = {
                    androidx.compose.material.Text(stringResource(R.string.description))
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.addFood()
                    }
                ),
            )

            Spacer(modifier = Modifier.padding(32.dp))

        }
    }
}

