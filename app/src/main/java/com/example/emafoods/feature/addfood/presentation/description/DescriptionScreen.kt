package com.example.emafoods.feature.addfood.presentation.description

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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

@Composable
fun DescriptionRoute(
    modifier: Modifier = Modifier,
    onConfirmedClick: () -> Unit,
    viewModel: DescriptionViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    DescriptionScreen(
        modifier = modifier,
        onDescriptionChange = { description ->
            viewModel.onDescriptionChange(description)
        },
        description = state.description,
        onConfirmedClick = onConfirmedClick
    )
}

@Composable
fun DescriptionScreen(
    modifier: Modifier = Modifier,
    onDescriptionChange: (String) -> Unit,
    onConfirmedClick: () -> Unit,
    description: String,
) {
    DescriptionScreenBackground()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DescriptionScreenTitle(
            modifier = modifier
                .fillMaxWidth(),
            description = stringResource(id = R.string.description_screen_title)
        )
        DescriptionScreenInput(
            modifier = modifier,
            onDescriptionChange = onDescriptionChange,
            description = description
        )
        DescriptionScreenNextButton(
            modifier = modifier,
            onConfirmedClick = onConfirmedClick,
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun DescriptionScreenTitle(
    modifier: Modifier = Modifier,
    description: String,
) {
    Text(
        text = description,
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
            .padding(start = 20.dp, top = 40.dp, bottom = 12.dp, end = 20.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionScreenInput(
    modifier: Modifier = Modifier,
    onDescriptionChange: (String) -> Unit,
    description: String
) {
    val maxChars = 300
    OutlinedTextField(
        value = description,
        onValueChange = {
            if (it.length <= maxChars) {
                onDescriptionChange(it)
            }
        },
        label = {
            if (description.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.description_empty_input_label_text),
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            } else {
                Text(
                    text = stringResource(id = R.string.description_input_label_text),
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .padding(top = 0.dp, start = 24.dp, end = 24.dp, bottom = 16.dp),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
            cursorColor = MaterialTheme.colorScheme.onSecondary,
        ),
    )
    Row(
        modifier = modifier
            .padding(end = 36.dp)
            .offset(y = (-52).dp),
    ) {
        Spacer(modifier = modifier.weight(1f))
        Text(
            text = (maxChars - description.count()).toString(),
            color = MaterialTheme.colorScheme.onSecondary,
        )
    }

}

@Composable
fun DescriptionScreenNextButton(
    modifier: Modifier = Modifier,
    onConfirmedClick: () -> Unit,
) {
    Row {
        Spacer(modifier = modifier.weight(1f))
        FloatingActionButton(
            modifier = modifier
                .padding(end = 24.dp)
                .offset(y = (-12).dp),
            onClick = onConfirmedClick,
            shape = CircleShape,
        ) {
            Icon(imageVector = Icons.Rounded.Check, contentDescription = "Add Description")
        }
    }
}


@Composable
fun DescriptionScreenBackground(
    modifier: Modifier = Modifier
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            MaterialTheme.colorScheme.secondary
        ),
        startY = sizeImage.height.toFloat(),
        endY = 900.0f,
    )

    Box() {
        Image(
            painter = painterResource(id = R.drawable.descriptionbackgr),
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
                .alpha(0.90f)
                .background(gradient)
        )
    }
}

