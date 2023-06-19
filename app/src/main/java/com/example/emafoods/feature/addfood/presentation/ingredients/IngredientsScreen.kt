package com.example.emafoods.feature.addfood.presentation.ingredients

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.R
import com.example.emafoods.core.presentation.features.addfood.BasicTitle
import com.example.emafoods.feature.addfood.presentation.category.CategoryScreenBackground
import com.example.emafoods.feature.addfood.presentation.common.NextStepButton
import com.example.emafoods.feature.addfood.presentation.common.StepIndicator
import com.example.emafoods.feature.addfood.presentation.common.TitleWithBackground
import com.example.emafoods.feature.addfood.presentation.description.navigation.DescriptionArguments
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData

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
                    ingredientsList = viewModel.serializedIngredients(),
                )
            )
        },
        ingredients = state.ingredientsList,
        onAddIngredientToList = { ingredient ->
            viewModel.addIngredientToList(ingredient)
        },
        onRemoveIngredientFromList = { ingredient ->
            viewModel.removeIngredientFromList(ingredient)
        },
        onSaveChangesIngredient = { ingredient ->
            viewModel.saveChangesIngredient(ingredient)
        },
        showIngredientAlreadyAddedError = state.showIngredientAlreadyAddedError,
        onShowedIngredientAlreadyAddedError = {
            viewModel.onShowedIngredientAlreadyAdded()
        },
        onUpdateIngredientFocus = { ingredient, isFocused ->
            viewModel.onUpdateIngredientFocus(ingredient, isFocused)
        },
    )
}

@Composable
fun IngredientsScreen(
    modifier: Modifier = Modifier,
    onConfirmedClick: () -> Unit,
    ingredients: List<IngredientViewData>,
    onAddIngredientToList: (IngredientViewData) -> Unit,
    onRemoveIngredientFromList: (IngredientViewData) -> Unit,
    onSaveChangesIngredient: (IngredientViewData) -> Unit,
    showIngredientAlreadyAddedError: Boolean,
    onShowedIngredientAlreadyAddedError: () -> Unit,
    showStepIndicator: Boolean = true,
    onUpdateIngredientFocus: (IngredientViewData, Boolean) -> Unit,
    screenTitle: String = stringResource(R.string.add_ingredients),
    screenTitlePaddingTop: Int = 50,
    nextStepButtonText: String = stringResource(R.string.next_step),
) {
    var shouldShowIngredientCard by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var isIngredientsListFocused by remember { mutableStateOf(false) }
    isIngredientsListFocused = ingredients.any { it.isFocused }

    HandleIngredientToasts(
        showIngredientAlreadyAddedError = showIngredientAlreadyAddedError,
        onShowedIngredientAlreadyAddedError = onShowedIngredientAlreadyAddedError,
    )

    CategoryScreenBackground()
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if(showStepIndicator) {
            StepIndicator(
                modifier = modifier
                    .align(Alignment.TopStart),
                step = 2
            )
        }
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(visible = !isIngredientsListFocused) {
                TitleWithBackground(
                    text = screenTitle,
                    modifier = modifier
                        .padding(top = screenTitlePaddingTop.dp, bottom = 8.dp)
                )
            }
            AnimatedVisibility(visible = !shouldShowIngredientCard && !isIngredientsListFocused) {
                AddNewIngredientButton(
                    modifier = modifier,
                    onClick = {
                        shouldShowIngredientCard = true
                    }
                )
            }
            AnimatedVisibility(visible = shouldShowIngredientCard) {
                IngredientCard(
                    modifier = modifier,
                    onRemoveIngredientClick = {
                        shouldShowIngredientCard = false
                    },
                    onConfirmIngredientClick = { ingredientViewData ->
                        shouldShowIngredientCard = false
                        onAddIngredientToList(ingredientViewData)
                    },
                    addFoodText = stringResource(R.string.add_food),
                )
            }
            AnimatedVisibility(visible = !isIngredientsListFocused && ingredients.isNotEmpty()) {
                BasicTitle(
                    modifier = modifier
                        .padding(top = 24.dp),
                    text = stringResource(R.string.ingredients_list_title),
                )
            }
            IngredientsEditableList(
                modifier = modifier,
                ingredients = ingredients,
                onRemoveIngredientClick = { ingredient ->
                    onRemoveIngredientFromList(ingredient)
                },
                onConfirmIngredientClick = { ingredient ->
                    onSaveChangesIngredient(ingredient)
                    focusManager.clearFocus()
                },
                onFocusChanged = onUpdateIngredientFocus
            )
        }
        NextStepButton(
            modifier = modifier
                .padding(bottom = 24.dp, end = 24.dp)
                .align(Alignment.BottomEnd),
            onConfirmedClick = onConfirmedClick,
            visible = ingredients.isNotEmpty(),
            text = nextStepButtonText,
        )
    }
}

@Composable
fun HandleIngredientToasts(
    showIngredientAlreadyAddedError: Boolean,
    onShowedIngredientAlreadyAddedError: () -> Unit
) {
    if (showIngredientAlreadyAddedError) {
        Toast.makeText(
            LocalContext.current,
            stringResource(R.string.ingredient_already_added_error),
            Toast.LENGTH_LONG
        ).show()
        onShowedIngredientAlreadyAddedError()
    }
}

@Composable
fun IngredientsEditableList(
    modifier: Modifier = Modifier,
    ingredients: List<IngredientViewData>,
    onRemoveIngredientClick: (IngredientViewData) -> Unit,
    onConfirmIngredientClick: (IngredientViewData) -> Unit,
    onFocusChanged: (IngredientViewData, Boolean) -> Unit,
) {
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
    ) {
        items(
            ingredients,
            key = { ingredient ->
                ingredient.id
            },
        ) { ingredient ->
            IngredientCard(
                modifier = modifier,
                ingredient = ingredient,
                onRemoveIngredientClick = {
                    onRemoveIngredientClick(ingredient)
                },
                onConfirmIngredientClick = {
                    onConfirmIngredientClick(it)
                },
                onFocusChanged = onFocusChanged
            )
        }
    }
}

@Composable
fun AddNewIngredientButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = null,
            modifier = modifier
                .padding(8.dp),
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = stringResource(R.string.add_new_ingredient),
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier.padding(12.dp),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun IngredientCard(
    modifier: Modifier = Modifier,
    onRemoveIngredientClick: () -> Unit,
    onConfirmIngredientClick: (IngredientViewData) -> Unit,
    ingredient: IngredientViewData =
        IngredientViewData(
            name = "",
            measurement = 0L,
        ),
    onFocusChanged: (IngredientViewData, Boolean) -> Unit = { _, _ -> },
    addFoodText: String = stringResource(id = R.string.save)
) {
    val pattern = remember { Regex("^\\d+\$") }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    var ingredientValue by remember { mutableStateOf(ingredient.name) }
    var measurementValue by remember {
        mutableStateOf(
            if (ingredient.measurement == 0L) "" else ingredient.measurement.toString()
        )
    }

    Column(
        modifier = modifier
            .padding(6.dp)
    ) {
        RemoveAndConfirmIngredientButtons(
            isFocused,
            modifier,
            onRemoveIngredientClick,
            ingredientValue,
            measurementValue,
            onConfirmIngredientClick,
            ingredient,
            addFoodText
        )
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
                    ingredientText = ingredientValue,
                    onValueChange = {
                        ingredientValue = it
                    },
                    focusRequester = focusRequester,
                    onFocused = { focused ->
                        isFocused = focused
                        onFocusChanged(ingredient, focused)
                    }
                )
                Spacer(modifier = modifier.padding(4.dp))
                Measurement(
                    modifier,
                    measurementValue = measurementValue,
                    onValueChange = { newValue ->
                        if ((newValue.matches(pattern) && newValue.length <= 4) || newValue.isEmpty()) {
                            measurementValue = newValue
                        }
                    },
                    focusRequester = focusRequester,
                    onFocused = { focused ->
                        isFocused = focused
                        onFocusChanged(ingredient, focused)
                    }
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.RemoveAndConfirmIngredientButtons(
    isFocused: Boolean,
    modifier: Modifier,
    onRemoveIngredientClick: () -> Unit,
    ingredientValue: String,
    measurementValue: String,
    onConfirmIngredientClick: (IngredientViewData) -> Unit,
    ingredient: IngredientViewData,
    addFoodText: String
) {
    AnimatedVisibility(visible = isFocused) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            RemoveIngredientButton(
                onClick = onRemoveIngredientClick,
            )
            ConfirmIngredientsButton(
                isEnabled = ingredientValue.isNotEmpty() && measurementValue.isNotEmpty(),
                modifier = modifier.padding(start = 20.dp),
                onClick = {
                    onConfirmIngredientClick(
                        IngredientViewData(
                            id = ingredient.id,
                            name = ingredientValue,
                            measurement = measurementValue.toLong()
                        )
                    )
                },
                text = addFoodText
            )
        }
    }
}

@Composable
fun ConfirmIngredientsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean,
    text: String
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
        modifier = modifier
            .width(76.dp),
        shape = CircleShape,
        contentColor = onColor,
        containerColor = color,
        content = {
            Text(
                text = text,
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
        modifier = modifier
            .width(76.dp),
        shape = CircleShape,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        content = {
            Text(
                text = stringResource(R.string.remove),
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



