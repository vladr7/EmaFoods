package com.example.emafoods.feature.addfood.presentation.title

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
fun TitleRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: TitleViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    TitleScreen(
        modifier = modifier,
        onTitleChange = { title ->
            viewModel.onTitleChange(title)
        },
        title = state.title
    )
}

@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    onTitleChange: (String) -> Unit,
    title: String
) {

    TitleScreenBackground()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TitleScreenTitle(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            title = stringResource(id = R.string.title_screen_title)
        )
        TitleScreenInput(
            modifier = modifier,
            onTitleChange = onTitleChange,
            title = title
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun TitleScreenTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        text = title,
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
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleScreenInput(
    modifier: Modifier = Modifier,
    onTitleChange: (String) -> Unit,
    title: String
) {
    OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        label = {
            Text(
                text = stringResource(id = R.string.title_input_label_text),
                color = MaterialTheme.colorScheme.onSecondary,
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
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
        maxLines = 1
    )
}

@Composable
fun TitleScreenBackground(
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
            painter = painterResource(id = R.drawable.titlebackground),
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
