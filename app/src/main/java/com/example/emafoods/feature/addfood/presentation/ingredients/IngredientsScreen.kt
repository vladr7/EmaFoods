package com.example.emafoods.feature.addfood.presentation.ingredients

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.R
import com.example.emafoods.feature.addfood.presentation.category.CategoryScreenBackground
import com.example.emafoods.feature.addfood.presentation.common.AddRecipeTitle
import com.example.emafoods.feature.addfood.presentation.common.StepIndicator
import com.example.emafoods.feature.addfood.presentation.description.navigation.DescriptionArguments

@Composable
fun IngredientsRoute(
    modifier: Modifier = Modifier,
    viewModel: IngredientsViewModel = hiltViewModel(),
    onConfirmedClick: (DescriptionArguments) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    IngredientsScreen(
        modifier = modifier,
        onConfirmedClick = {
            onConfirmedClick(
                DescriptionArguments(
                    uri = state.uriId,
                    category = state.categoryType.string,
                )
            )
        }
    )
}

@Composable
fun IngredientsScreen(
    modifier: Modifier = Modifier,
    onConfirmedClick: () -> Unit
) {
    var ingredientValue by remember { mutableStateOf("") }
    var measurementValue by remember { mutableStateOf("") }

    val pattern = remember { Regex("^\\d+\$") }

    CategoryScreenBackground()
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        StepIndicator(
            modifier = modifier
                .align(Alignment.TopStart),
            step = 2
        )
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            AddRecipeTitle(
                text = stringResource(R.string.add_ingredients),
                modifier = modifier
                    .padding(top = 50.dp)
            )
            IngredientCard(
                modifier = modifier,
                ingredientText = ingredientValue,
                measurementValue = measurementValue,
                onIngredientValueChange = {
                    ingredientValue = it
                },
                onMeasurementValueChange = { newValue ->
                    if ((newValue.matches(pattern) && newValue.length <= 4) || newValue.isEmpty()) {
                        measurementValue = newValue
                    }
                }
            )
        }

        NextStepIngredientsButton(
            modifier = modifier
                .padding(bottom = 24.dp, end = 24.dp)
                .align(Alignment.BottomEnd)
        ) {
            onConfirmedClick()
        }
    }
}

@Composable
fun IngredientCard(
    modifier: Modifier = Modifier,
    onIngredientValueChange: (String) -> Unit,
    ingredientText: String,
    onMeasurementValueChange: (String) -> Unit,
    measurementValue: String
) {
    val focusRequester = remember { FocusRequester() }
    var isFocusable by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(12.dp)

        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Ingredient(
                    modifier = modifier,
                    ingredientText = ingredientText,
                    onValueChange = {
                        onIngredientValueChange(it)
                    },
                    focusRequester = focusRequester,
                    onFocused = {
                        isFocusable = it
                    }
                )
                Spacer(modifier = modifier.padding(4.dp))
                Measurement(
                    modifier,
                    measurementValue = measurementValue,
                    onValueChange = {
                        onMeasurementValueChange(it)
                    },
                    focusRequester = focusRequester,
                    onFocused = {
                        isFocusable = it
                    }
                )
            }

        }
        AnimatedVisibility(visible = isFocusable) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                RemoveIngredientButton(
                    onClick = { /*TODO*/ },
                )
                ConfirmIngredientsButton(
                    isEnabled = ingredientText.isNotEmpty() && measurementValue.isNotEmpty(),
                    modifier = modifier.padding(start = 20.dp),
                    onClick = { /*TODO*/ },
                )
            }
        }
    }
}

@Composable
fun ConfirmIngredientsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean
) {
    val color = if (isEnabled) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
    }
    val onColor = if (isEnabled) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f)
    }
    FloatingActionButton(
        onClick = {
            if (isEnabled) {
                onClick()
            }
        },
        modifier = modifier,
        shape = CircleShape,
        contentColor = onColor,
        containerColor = color,
        content = {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = onColor
            )
        },
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
    )
}

@Composable
fun RemoveIngredientButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        content = {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = null
            )
        }
    )
}

@Composable
private fun RowScope.Ingredient(
    ingredientText: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    focusRequester: FocusRequester,
    onFocused: (Boolean) -> Unit,
) {
    val color = MaterialTheme.colorScheme.onSecondaryContainer
    OutlinedTextField(
        value = ingredientText,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(text = stringResource(R.string.ingredient_hint))
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onFocused(true)
                } else {
                    onFocused(false)
                }
            }
            .weight(0.7f),
        textStyle = TextStyle(
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily
        ),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = color,
            focusedBorderColor = color,
            unfocusedBorderColor = color,
        ),
    )
}

@Composable
private fun RowScope.Measurement(
    modifier: Modifier,
    measurementValue: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onFocused: (Boolean) -> Unit,
    ) {
    val color = MaterialTheme.colorScheme.onSecondaryContainer
    OutlinedTextField(
        value = measurementValue,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(text = stringResource(R.string.measurement_hint))
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onFocused(true)
                } else {
                    onFocused(false)
                }
            }
            .weight(0.3f),
        textStyle = TextStyle(
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily
        ),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = color,
            focusedBorderColor = color,
            unfocusedBorderColor = color,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun NextStepIngredientsButton(
    modifier: Modifier = Modifier,
    onConfirmedClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onConfirmedClick,
        shape = CircleShape,
    ) {
        Icon(
            imageVector = Icons.Rounded.Check,
            contentDescription = "Add ingredients"
        )
    }
}

