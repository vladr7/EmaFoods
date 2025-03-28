package com.example.emafoods.feature.allfoods.presentation

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.R
import com.example.emafoods.core.presentation.animations.bounceClick
import com.example.emafoods.core.presentation.features.addfood.BasicTitle
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.addfood.presentation.category.CategoryScreenBackground
import com.example.emafoods.feature.addfood.presentation.ingredients.IngredientsScreen
import com.example.emafoods.feature.addfood.presentation.insert.IngredientsList
import com.example.emafoods.feature.allfoods.presentation.models.FilterCategoryType
import com.example.emafoods.feature.pending.presentation.EmptyDescriptionMessage
import com.example.emafoods.feature.pending.presentation.FoodDescription
import com.example.emafoods.feature.pending.presentation.FoodImage
import com.example.emafoods.feature.pending.presentation.FoodTitle
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllFoodsRoute(
    modifier: Modifier = Modifier,
    viewModel: AllFoodsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getAllFoods()
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { viewModel.onRefresh() }
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        if (!state.showEditIngredientsContent) {
            AllFoodsScreen(
                foods = state.foods,
                searchText = state.searchText,
                onSearchTextChanged = { viewModel.onSearchTextChange(it) },
                filterCategoryType = state.filterCategoryType,
                onDropDownItemClick = { viewModel.onDropDownItemClick(it) },
                onEditClick = { viewModel.onEditIngredients(it) },
                isAdmin = state.isAdmin,
                onDescriptionChanged = { description, food ->
                    viewModel.onDescriptionChanged(description, food)
                },
                onCancelDescriptionEditClick = { viewModel.onCancelDescriptionEditClick() },
                onSaveChangesDescriptionClick = { viewModel.onSaveChangesDescriptionClick() },
                onUriRetrieved = { uri, food ->
                    viewModel.onAdminChangedImage(uri, food)
                },
            )
        } else {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IngredientsScreen(
                    onConfirmedClick = { viewModel.onFinishedEditingIngredients() },
                    ingredients = state.ingredientsList,
                    onAddIngredientToList = { viewModel.addIngredientToList(it) },
                    onRemoveIngredientFromList = { viewModel.removeIngredientFromList(it) },
                    onSaveChangesIngredient = { viewModel.saveChangesIngredient(it) },
                    onShowedIngredientAlreadyAddedError = { viewModel.onShowedIngredientAlreadyAdded() },
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
        }
        // Add the pull-to-refresh indicator at the top center.
        PullRefreshIndicator(
            refreshing = state.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}



@Composable
fun AllFoodsScreen(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    foods: List<FoodViewData> = emptyList(),
    filterCategoryType: FilterCategoryType,
    onDropDownItemClick: (FilterCategoryType) -> Unit,
    onEditClick: (FoodViewData) -> Unit,
    isAdmin: Boolean,
    onDescriptionChanged: (String, FoodViewData) -> Unit,
    onCancelDescriptionEditClick: () -> Unit,
    onSaveChangesDescriptionClick: () -> Unit,
    onUriRetrieved: (Uri?, FoodViewData) -> Unit,
) {
    var dropDownFilterExpanded by remember { mutableStateOf(false) }

    CategoryScreenBackground(alpha = 0.50f)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchFoodBar(
                    searchText = searchText,
                    onSearchTextChanged = onSearchTextChanged,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                )
                FilterIcon(
                    modifier = Modifier
                        .bounceClick {
                            dropDownFilterExpanded = !dropDownFilterExpanded
                        }
                )
            }
            if (foods.isNotEmpty()) {
                TextNumberOfRecipesFound(numberOfRecipes = foods.size)
            }
            Spacer(modifier = Modifier.height(4.dp))
            if (foods.isEmpty()) {
                EmptyDescriptionMessage(
                    message = stringResource(id = R.string.no_foods_found),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            } else {
                FoodList(
                    foods = foods,
                    onEditIngredientsClick = {
                        onEditClick(it)
                    },
                    isAdmin = isAdmin,
                    onDescriptionChanged = onDescriptionChanged,
                    onCancelDescriptionEditClick = onCancelDescriptionEditClick,
                    onSaveChangesDescriptionClick = onSaveChangesDescriptionClick,
                    onUriRetrieved = onUriRetrieved
                )
            }
        }
        DropDownFilter(
            modifier = Modifier,
            filterCategoryType = filterCategoryType,
            expanded = dropDownFilterExpanded,
            onDismissRequest = { dropDownFilterExpanded = false }
        ) { filterCategoryType ->
            onDropDownItemClick(filterCategoryType)
            dropDownFilterExpanded = false
        }
    }
}

@Composable
fun FoodList(
    foods: List<FoodViewData>,
    onEditIngredientsClick: (FoodViewData) -> Unit,
    onDescriptionChanged: (String, FoodViewData) -> Unit,
    isAdmin: Boolean,
    onCancelDescriptionEditClick: () -> Unit,
    onSaveChangesDescriptionClick: () -> Unit,
    onUriRetrieved: (Uri?, FoodViewData) -> Unit,
) {
    LazyColumn(content = {
        items(foods.size) { index ->
            val food = foods[index]
            FoodItemList(
                food = food,
                onEditIngredientsClick = {
                    onEditIngredientsClick(food)
                },
                isAdmin = isAdmin,
                onDescriptionChanged = {
                    onDescriptionChanged(it, food)
                },
                onCancelDescriptionEditClick = onCancelDescriptionEditClick,
                onSaveChangesDescriptionClick = onSaveChangesDescriptionClick,
                onUriRetrieved = {
                    onUriRetrieved(it, food)
                }
            )
        }
    })
}

@Composable
fun ColumnScope.TextNumberOfRecipesFound(
    numberOfRecipes: Int,
    modifier: Modifier = Modifier,
) {
    val animatedNumber by animateIntAsState(targetValue = numberOfRecipes)

    var scale by remember { mutableStateOf(1f) }

    LaunchedEffect(key1 = numberOfRecipes) {
        scale = 1.1f
        delay(300)
        scale = 1f
    }

    val animatedScale by animateFloatAsState(targetValue = scale)

    Text(
        text = stringResource(R.string.number_of_recipes_found, animatedNumber),
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSecondary,
        modifier = modifier
            .align(Alignment.Start)
            .padding(start = 24.dp, top = 2.dp)
            .graphicsLayer(scaleX = animatedScale, scaleY = animatedScale)
    )
}

@Composable
fun FilterIcon(
    modifier: Modifier = Modifier,
) {
    val brush = Brush.radialGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondary,
            Color.Transparent,
        ),
        radius = 80f,
    )
    Icon(
        imageVector = Icons.Default.FilterList,
        contentDescription = "Category",
        tint = Color.White,
        modifier = modifier
            .background(
                brush = brush,
            )
            .size(40.dp)
    )
}

@Composable
fun FoodItemList(
    food: FoodViewData,
    modifier: Modifier = Modifier,
    onEditIngredientsClick: () -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCancelDescriptionEditClick: () -> Unit,
    onSaveChangesDescriptionClick: () -> Unit,
    isAdmin: Boolean,
    onUriRetrieved: (Uri?) -> Unit,
) {
    val color by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.secondary, label = ""
    )

    var expanded by remember { mutableStateOf(false) }
    var isDescriptionEditable by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .heightIn(max = 5500.dp)
            .padding(16.dp)
            .clickable { expanded = expanded.not() },
        elevation = 8.dp, shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(color)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
        ) {
            FoodImage(
                imageUri = food.imageRef,
                author = food.author,
                showFoodAuthor = expanded,
                isFoodNew = food.isNew,
                showImageEditButton = isAdmin && expanded,
                onUriRetrieved = onUriRetrieved,
            )
            FoodTitle(text = food.title)
            AnimatedVisibility(visible = expanded) {
                Column {
                    if (food.ingredients.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            IngredientsList(
                                ingredients = food.ingredients,
                                onEditClick = {
                                    onEditIngredientsClick()
                                },
                                isEditButtonVisible = isAdmin,
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            BasicTitle(
                                modifier = Modifier,
                                text = stringResource(id = R.string.description_title)
                            )
                            if (!isDescriptionEditable) {
                                EditPencil(
                                    modifier = Modifier
                                        .clickable {
                                            isDescriptionEditable = isDescriptionEditable.not()
                                        }
                                        .padding(start = 8.dp, end = 20.dp)
                                )
                            } else {
                                Close(
                                    modifier = Modifier
                                        .clickable {
                                            onCancelDescriptionEditClick()
                                            isDescriptionEditable = isDescriptionEditable.not()
                                        }
                                )
                                Checkmark(
                                    modifier = Modifier
                                        .clickable {
                                            onSaveChangesDescriptionClick()
                                            isDescriptionEditable = isDescriptionEditable.not()
                                        }
                                        .padding(start = 13.dp, end = 8.dp)
                                )
                            }
                        }
                    }
                    if (food.description.isNotEmpty()) {
                        FoodDescription(
                            description = food.description,
                            onDescriptionChange = onDescriptionChanged,
                            isEditable = isDescriptionEditable
                        )
                    } else {
                        EmptyDescriptionMessage(
                            message = stringResource(id = R.string.no_description_message)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun EditPencil(
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier
            .size(32.dp),
        imageVector = Icons.Filled.Edit,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSecondary
    )
}

@Composable
fun Checkmark(
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier
            .size(32.dp),
        imageVector = Icons.Filled.Done,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSecondary
    )
}

@Composable
fun Close(
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier
            .size(32.dp),
        imageVector = Icons.Filled.Close,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSecondary
    )
}

@Composable
private fun SearchFoodBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
    ) {
        if (searchText.isEmpty()) {
            Text(
                text = stringResource(R.string.search_food_hint),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp)
                    .alpha(ContentAlpha.medium),
                fontSize = 18.sp
            )
        }

        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        )

        if (searchText.isNotEmpty()) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
                    .clickable {
                        onSearchTextChanged("")
                    }
            )
        } else {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
            )
        }
    }
}

@Composable
fun BoxScope.DropDownFilter(
    modifier: Modifier = Modifier,
    filterCategoryType: FilterCategoryType,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDropDownItemClick: (FilterCategoryType) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(top = 75.dp, end = 10.dp)
            .align(Alignment.TopEnd)
    ) {
        DropdownMenu(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer),
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            DropdownMenuItem(
                modifier = modifier
                    .background(
                        color = if (filterCategoryType == FilterCategoryType.ALL)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.primaryContainer
                    ),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.allfoodsicon),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.all_foods),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }, onClick = {
                    onDropDownItemClick(FilterCategoryType.ALL)
                })
            Divider()
            DropdownMenuItem(
                modifier = Modifier
                    .background(
                        color = if (filterCategoryType == FilterCategoryType.MAIN_DISH)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.primaryContainer
                    ),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.spaghetti),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.main_dish_dropdown),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }, onClick = {
                    onDropDownItemClick(FilterCategoryType.MAIN_DISH)
                })
            Divider()
            DropdownMenuItem(
                modifier = Modifier
                    .background(
                        color = if (filterCategoryType == FilterCategoryType.DESSERT)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.primaryContainer
                    ),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.dessert),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.dessert),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }, onClick = {
                    onDropDownItemClick(FilterCategoryType.DESSERT)
                })
            Divider()
            DropdownMenuItem(
                modifier = Modifier
                    .background(
                        color = if (filterCategoryType == FilterCategoryType.SOUP)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.primaryContainer
                    ),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.soup),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.soup),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }, onClick = {
                    onDropDownItemClick(FilterCategoryType.SOUP)
                })
            Divider()
            DropdownMenuItem(
                modifier = Modifier
                    .background(
                        color = if (filterCategoryType == FilterCategoryType.BREAKFAST)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.primaryContainer
                    ),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.breakfast),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.breakfast),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }, onClick = {
                    onDropDownItemClick(FilterCategoryType.BREAKFAST)
                })
        }
    }
}