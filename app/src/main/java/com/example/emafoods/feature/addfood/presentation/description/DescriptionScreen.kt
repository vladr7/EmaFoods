package com.example.emafoods.feature.addfood.presentation.description

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.R
import com.example.emafoods.core.presentation.animations.LottieAnimationContent
import com.example.emafoods.core.presentation.common.BackgroundTopToBot
import com.example.emafoods.feature.addfood.presentation.common.TitleWithBackground
import com.example.emafoods.feature.addfood.presentation.common.NextStepButton
import com.example.emafoods.feature.addfood.presentation.common.StepIndicator
import com.example.emafoods.feature.addfood.presentation.insert.navigation.InsertFoodArguments

@Composable
fun DescriptionRoute(
    modifier: Modifier = Modifier,
    onConfirmedClick: (InsertFoodArguments) -> Unit,
    viewModel: DescriptionViewModel = hiltViewModel(),
) {
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
                    category = state.category,
                    ingredientsList = state.ingredientsListSerialized
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
    Box(modifier = modifier.fillMaxSize()) {
        StepIndicator(
            modifier = modifier
                .align(Alignment.TopStart),
            step = 3
        )
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TitleWithBackground(
                text = stringResource(id = R.string.description_screen_title),
                modifier = modifier
                    .padding(top = 35.dp)
            )
            DescriptionScreenInput(
                modifier = modifier,
                onDescriptionChange = onDescriptionChange,
                description = description
            )
            Row {
                Spacer(modifier = modifier.weight(1f))
                NextStepButton(
                    modifier = modifier
                        .padding(end = 24.dp),
                    onConfirmedClick = onConfirmedClick,
                    visible = showNextButton
                )
            }
            Spacer(modifier = modifier.weight(1f))
            LottieAnimationContent(
                animationId = R.raw.descriptionplants,
                modifier = modifier
                    .offset(y = 85.dp)
            )
        }
    }
}

@Composable
fun DescriptionScreenInput(
    modifier: Modifier = Modifier,
    onDescriptionChange: (String) -> Unit,
    description: String
) {
    val maxChars = 1000

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 350.dp)
            .padding(top = 10.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
    ) {
        OutlinedTextField(
            value = description,
            onValueChange = {
                if (it.length <= maxChars) {
                    onDescriptionChange(it)
                }
            },
            label = {
                if (description.length <= 10) {
                    Text(
                        text = stringResource(id = R.string.description_empty_input_label_text_min_chars),
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.description_empty_input_label_text),
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
        )
        Text(
            text = (maxChars - description.count()).toString(),
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )
    }
}




