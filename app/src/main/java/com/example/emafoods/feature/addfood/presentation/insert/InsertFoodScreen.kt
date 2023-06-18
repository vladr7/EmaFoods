package com.example.emafoods.feature.addfood.presentation.insert

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.emafoods.R
import com.example.emafoods.core.extension.getCompressedImage
import com.example.emafoods.core.presentation.animations.LoadingButton
import com.example.emafoods.core.presentation.animations.LottieAnimationContent
import com.example.emafoods.core.presentation.common.BackgroundTopToBot
import com.example.emafoods.core.presentation.features.addfood.BasicTitle
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import com.example.emafoods.feature.addfood.presentation.description.DescriptionScreenInput
import com.example.emafoods.feature.addfood.presentation.image.AttachFileIcon
import com.example.emafoods.feature.addfood.presentation.image.TakePictureIcon
import com.example.emafoods.feature.addfood.presentation.image.TitleScreenInput
import com.example.emafoods.feature.addfood.presentation.ingredients.IngredientsScreen
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import com.example.emafoods.feature.game.presentation.ScrollArrow
import com.example.emafoods.feature.generatefood.presentation.LoadingCookingAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun InsertFoodRoute(
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit,
    viewModel: InsertFoodViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val coroutine = rememberCoroutineScope()

    if (state.insertFoodSuccess) {
        viewModel.onXpIncrease()
        viewModel.resetState()
        onSuccess()
    } else {
        AnimatedVisibility(
            visible = state.showEditIngredientsContent,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            IngredientsScreen(
                onConfirmedClick = {
                    viewModel.onFinishedEditingIngredients()
                },
                ingredients = state.ingredientsList,
                onAddIngredientToList = {
                    viewModel.addIngredientToList(it)
                },
                onRemoveIngredientFromList = {
                    viewModel.removeIngredientFromList(it)
                },
                onSaveChangesIngredient = {
                    viewModel.saveChangesIngredient(it)
                },
                onShowedIngredientAlreadyAddedError = {
                    viewModel.onShowedIngredientAlreadyAdded()
                },
                showIngredientAlreadyAddedError = state.showIngredientAlreadyAddedError,
                showStepIndicator = false,
                onUpdateIngredientFocus = { ingredient, isFocused ->
                    viewModel.onUpdateIngredientFocus(ingredient, isFocused)
                },
                screenTitle = stringResource(id = R.string.edit_ingredients_title),
                screenTitlePaddingTop = 10,
                nextStepButtonText = stringResource(R.string.save_changes)
            )
        }
        AnimatedVisibility(
            visible = !state.showEditIngredientsContent,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            InsertFoodScreen(
                modifier = modifier,
                imageUri = state.imageUri,
                onDescriptionChange = { viewModel.updateDescription(it) },
                description = state.description,
                onInsertFoodClick = {
                    viewModel.insertFood(
                        description = state.description,
                        imageUri = state.imageUri,
                        ingredients = state.ingredientsList,
                        title = state.title
                    )
                },
                onUriChanged = { uri ->
                    coroutine.launch {
                        val compressedUri = uri?.getCompressedImage(context)
                        // todo add a loading
                        viewModel.onSelectedNewImage()
                        viewModel.updateImageUri(compressedUri)
                    }
                },
                loading = state.isLoading,
                ingredients = state.ingredientsList,
                onEditIngredientsClick = {
                    viewModel.onEditIngredients()
                },
                title = state.title,
                onTitleChange = { viewModel.updateTitle(it) },
            )
        }
    }
    if (state.errorMessage?.isNotEmpty() == true) {
        Toast.makeText(context, "${state.errorMessage}", Toast.LENGTH_LONG).show()
        viewModel.hideError()
    }

}

@Composable
fun InsertFoodScreen(
    imageUri: Uri?,
    modifier: Modifier = Modifier,
    onDescriptionChange: (String) -> Unit,
    description: String,
    onInsertFoodClick: () -> Unit,
    onUriChanged: (Uri?) -> Unit,
    loading: Boolean,
    ingredients: List<IngredientViewData>,
    onEditIngredientsClick: () -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
) {
    BackgroundTopToBot(
        imageId = R.drawable.descriptionbackgr
    )
    val scrollState = rememberScrollState()
    LaunchedEffect(scrollState.maxValue) {
        scrollState.scrollTo(scrollState.maxValue)
    }
    Box(modifier = modifier) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .heightIn(max = 5000.dp)
        ) {
            InsertFoodImage(
                imageUri = imageUri,
                modifier = modifier
                    .padding(top = 20.dp),
                onUriChangedChoseFile = onUriChanged,
                onUriChangedTakePicture = onUriChanged,
            )
            TitleScreenInput(
                onTitleChange = onTitleChange,
                title = title
            )
            IngredientsReadOnlyContent(
                modifier = modifier,
                ingredients = ingredients,
                onEditClick = onEditIngredientsClick,
                isContentVisible = ingredients.isNotEmpty()
            )
            if (ingredients.isNotEmpty()) {
                Spacer(modifier = modifier.height(16.dp))
                BasicTitle(
                    modifier = modifier,
                    text = stringResource(id = R.string.description_title)
                )
                DescriptionScreenInput(
                    modifier = modifier,
                    onDescriptionChange = onDescriptionChange,
                    description = description
                )
                Spacer(modifier = modifier.height(16.dp))
                AddRecipeButton(
                    modifier,
                    onInsertFoodClick,
                    loading
                )
            }
        }
        ScrollArrow(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .offset(y = 10.dp)
                .size(80.dp),
            visible = scrollState.value == 0 && scrollState.canScrollForward && scrollState.maxValue > 100,
        )
    }
}

@Composable
fun IngredientsReadOnlyContent(
    modifier: Modifier = Modifier,
    ingredients: List<IngredientViewData>,
    onEditClick: () -> Unit,
    isEditButtonVisible: Boolean = true,
    isContentVisible: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 500.dp)
    ) {
        if (isContentVisible) {
            IngredientsNotEmptyContent(modifier, isEditButtonVisible, onEditClick, ingredients)
        } else {
            IngredientsEmptyContent(
                modifier,
                onAddIngredientsClick = onEditClick
            )
        }
    }
}

@Composable
fun IngredientsEmptyContent(
    modifier: Modifier,
    onAddIngredientsClick: () -> Unit
) {
    Button(
        onClick = onAddIngredientsClick,
        modifier = modifier
            .padding(start = 20.dp)
            .height(50.dp)
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        Text(
            text = stringResource(id = R.string.add_ingredients),
            modifier = modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun IngredientsNotEmptyContent(
    modifier: Modifier,
    isEditButtonVisible: Boolean,
    onEditClick: () -> Unit,
    ingredients: List<IngredientViewData>
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTitle(
                modifier = modifier
                    .padding(bottom = 8.dp),
                text = stringResource(id = R.string.ingredients_list_title)
            )
            if (isEditButtonVisible) {
                EditIngredientsButton(
                    onClick = onEditClick
                )
            }
        }
        ingredients.forEach { ingredient ->
            IngredientReadOnlyItem(
                modifier = modifier,
                ingredientName = ingredient.name,
                measurement = ingredient.measurement.toString()
            )
            Divider(
                modifier = modifier
                    .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 4.dp)
                    .alpha(0.5f)
            )
        }
    }
}

@Composable
fun CategoryTypeRow(
    modifier: Modifier = Modifier,
    categoryType: CategoryType,
) {
    when (categoryType) {
        CategoryType.BREAKFAST -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painterResource(id = R.drawable.breakfast),
                    contentDescription = null,
                    modifier = modifier
                        .size(50.dp)
                        .padding(start = 20.dp, end = 4.dp)
                )
                Text(
                    modifier = modifier
                        .padding(start = 4.dp, end = 20.dp),
                    text = stringResource(id = R.string.breakfast),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        CategoryType.MAIN_DISH -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painterResource(id = R.drawable.pasta),
                    contentDescription = null,
                    modifier = modifier
                        .size(50.dp)
                        .padding(start = 20.dp, end = 4.dp)
                )
                Text(
                    modifier = modifier
                        .padding(start = 4.dp, end = 20.dp),
                    text = stringResource(id = R.string.main_dish),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        CategoryType.SOUP -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painterResource(id = R.drawable.soup),
                    contentDescription = null,
                    modifier = modifier
                        .size(50.dp)
                        .padding(start = 20.dp, end = 4.dp)
                )
                Text(
                    modifier = modifier
                        .padding(start = 4.dp, end = 20.dp),
                    text = stringResource(id = R.string.soup),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        CategoryType.DESSERT -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painterResource(id = R.drawable.dessert),
                    contentDescription = null,
                    modifier = modifier
                        .size(50.dp)
                        .padding(start = 20.dp, end = 4.dp)
                )
                Text(
                    modifier = modifier
                        .padding(start = 4.dp, end = 20.dp),
                    text = stringResource(id = R.string.dessert),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EditIngredientsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .clickable { onClick() }
            .padding(end = 20.dp, top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = modifier
                .padding(start = 20.dp, end = 4.dp),
            text = stringResource(id = R.string.edit_ingredients),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondary,
            fontWeight = FontWeight.Bold
        )
        Icon(
            modifier = modifier
                .size(32.dp),
            imageVector = Icons.Filled.Edit,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun IngredientReadOnlyItem(
    modifier: Modifier = Modifier,
    ingredientName: String,
    measurement: String,
) {
    var ingredientNameDisplay by remember {
        mutableStateOf("")
    }

    var measurementDisplay by remember {
        mutableStateOf("")
    }

    LaunchedEffect(
        key1 = ingredientName,
    ) {
        ingredientName.forEachIndexed { charIndex, _ ->
            ingredientNameDisplay = ingredientName
                .substring(
                    startIndex = 0,
                    endIndex = charIndex + 1,
                )
            delay(2)
        }
    }

    LaunchedEffect(
        key1 = measurement,
    ) {
        measurement.forEachIndexed { charIndex, _ ->
            measurementDisplay = measurement
                .substring(
                    startIndex = 0,
                    endIndex = charIndex + 1,
                )
            delay(2)
        }
    }

    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .padding(start = 20.dp, end = 8.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Filled.Circle,
            contentDescription = null,
            modifier = modifier
                .size(22.dp)
                .padding(end = 8.dp),
            tint = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                ) {
                    append(ingredientNameDisplay)
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary,
                        shadow = Shadow(
                            color = Color.Yellow,
                            blurRadius = 4f,
                            offset = Offset(0f, 0f)
                        )
                    ),
                ) {
                    append(" $measurementDisplay")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary,
                        shadow = Shadow(
                            color = Color.Yellow,
                            blurRadius = 4f,
                            offset = Offset(0f, 0f)
                        )
                    )
                ) {
                    append(" gr/ml")
                }
            }
        )

    }
}

@Composable
private fun AddRecipeButton(
    modifier: Modifier,
    onInsertFoodClick: () -> Unit,
    loading: Boolean
) {
    Row(
        modifier = modifier
            .padding(bottom = 32.dp, end = 24.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        LottieAnimationContent(
            animationId = R.raw.insertfoodplant,
            modifier = modifier
                .size(50.dp)
                .padding(end = 8.dp),
            color = MaterialTheme.colorScheme.onSecondary,
            speed = 0.3f,
        )
        LoadingButton(
            onClick = onInsertFoodClick,
            loading = loading,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add recipe",
                    tint = MaterialTheme.colorScheme.onSecondary,
                )
                Text(
                    stringResource(R.string.add_recipe),
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            }
        }
    }
}

@Composable
fun InsertFoodImage(
    imageUri: Uri?,
    modifier: Modifier = Modifier,
    onUriChangedChoseFile: (Uri?) -> Unit,
    onUriChangedTakePicture: (Uri?) -> Unit
) {
    val context = LocalContext.current
    val hasImage = imageUri != null && imageUri.toString().isNotEmpty()
    var alreadyShowedErrorImage by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            .shadow(
                elevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
                clip = true
            )
    ) {
        SubcomposeAsyncImage(
            modifier = modifier,
            model = ImageRequest.Builder(context)
                .data(imageUri)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            loading = {
                LoadingCookingAnimation()
            },
            error = {
                // todo undo this
//                if (!alreadyShowedErrorImage) {
//                    alreadyShowedErrorImage = true
//                    Toast.makeText(
//                        context,
//                        stringResource(R.string.error_loading_picture),
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
            }
        )
        Column(
            modifier = modifier
                .align(Alignment.TopEnd),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .padding(top = 8.dp)
            ) {
                if (!hasImage) {
                    Text(
                        text = stringResource(R.string.choose_from_gallery),
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontWeight = FontWeight.Bold,
                    )
                }
                AttachFileIcon(
                    onUriRetrieved = {
                        onUriChangedChoseFile(it)
                    },
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.secondary,
                                    Color.Transparent
                                )
                            )
                        ),
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (!hasImage) {
                    Text(
                        text = stringResource(R.string.take_a_picture),
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontWeight = FontWeight.Bold,
                    )
                }
                TakePictureIcon(
                    onUriRetrieved = {
                        onUriChangedTakePicture(it)
                    },
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.secondary,
                                    Color.Transparent
                                )
                            )
                        ),
                )
            }
        }

    }
}




