package com.example.emafoods.feature.addfood.presentation.description

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.R
import com.example.emafoods.core.presentation.common.BackgroundTopToBot
import com.example.emafoods.core.presentation.features.addfood.TitleComponent
import com.example.emafoods.feature.addfood.presentation.insert.navigation.InsertFoodArguments

@Composable
fun DescriptionRoute(
    modifier: Modifier = Modifier,
    onConfirmedClick: (InsertFoodArguments) -> Unit,
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
        onConfirmedClick = {
            onConfirmedClick(
                InsertFoodArguments(
                    description = state.description,
                    uri = state.uri,
                )
            )
        },
        showNextButton = state.showNextButton
    )
}

@Composable
fun DescriptionScreen(
    modifier: Modifier = Modifier,
    onDescriptionChange: (String) -> Unit,
    onConfirmedClick: () -> Unit,
    description: String,
    showNextButton: Boolean,
) {
    BackgroundTopToBot(
        imageId = R.drawable.descriptionbackgr
    )
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TitleComponent(
            text = stringResource(id = R.string.description_screen_title),
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 40.dp, bottom = 12.dp, end = 20.dp)
        )
        DescriptionScreenInput(
            modifier = modifier,
            onDescriptionChange = onDescriptionChange,
            description = description
        )
        DescriptionScreenNextButton(
            modifier = modifier,
            onConfirmedClick = onConfirmedClick,
            showNextButton = showNextButton
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionScreenInput(
    modifier: Modifier = Modifier,
    onDescriptionChange: (String) -> Unit,
    description: String
) {
    val maxChars = 300

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .padding(top = 0.dp, start = 24.dp, end = 24.dp, bottom = 16.dp),
    ) {
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
                .fillMaxSize(),
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
        Text(
            text = (maxChars - description.count()).toString(),
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        )
    }
}

@Composable
fun DescriptionScreenNextButton(
    modifier: Modifier = Modifier,
    onConfirmedClick: () -> Unit,
    showNextButton: Boolean = false,
) {
    AnimatedVisibility(visible = showNextButton,
        enter = slideInHorizontally {
            it
        },
        exit = slideOutHorizontally(
            targetOffsetX = { it }
        )
    ) {
        Row {
            Spacer(modifier = modifier.weight(1f))
            FloatingActionButton(
                modifier = modifier
                    .padding(end = 24.dp),
                onClick = onConfirmedClick,
                shape = CircleShape,
            ) {
                Icon(imageVector = Icons.Rounded.Check, contentDescription = "Add Description")
            }
        }
    }
}


