package com.example.emafoods.feature.allfoods.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.R
import com.example.emafoods.core.presentation.features.addfood.BasicTitle
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.addfood.presentation.category.CategoryScreenBackground
import com.example.emafoods.feature.addfood.presentation.insert.IngredientsReadOnlyContent
import com.example.emafoods.feature.pending.presentation.EmptyDescriptionMessage
import com.example.emafoods.feature.pending.presentation.FoodAuthor
import com.example.emafoods.feature.pending.presentation.FoodDescription
import com.example.emafoods.feature.pending.presentation.FoodImage
import com.example.emafoods.feature.pending.presentation.FoodTitle


@Composable
fun AllFoodsRoute(
    modifier: Modifier = Modifier,
    viewModel: AllFoodsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AllFoodsScreen(
        modifier = modifier,
        foods = state.foods,
        searchText = state.searchText,
        onSearchTextChanged = { viewModel.onSearchTextChange(it) }
    )
}

@Composable
fun AllFoodsScreen(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    foods: List<FoodViewData> = emptyList(),
) {
    CategoryScreenBackground(alpha = 0.50f)
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        SearchFoodBar(searchText = searchText, onSearchTextChanged = onSearchTextChanged)
        Spacer(modifier = Modifier.height(4.dp))
        if(foods.isEmpty()) {
            EmptyDescriptionMessage(
                message = stringResource(id = R.string.no_foods_found),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        } else {
            FoodList(foods = foods)
        }
    }
}

@Composable
fun FoodList(foods: List<FoodViewData>) {
    LazyColumn(content = {
        items(foods.size) { index ->
            val food = foods[index]
            FoodItemList(
                food = food,
            )
        }
    })
}

@Composable
fun FoodItemList(
    food: FoodViewData,
    modifier: Modifier = Modifier,
) {
    val color by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.secondary, label = ""
    )

    var expanded by remember { mutableStateOf(false) }

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
            FoodImage(imageUri = food.imageRef)
            FoodTitle(text = food.title)
            AnimatedVisibility(visible = expanded) {
                Column {
                    if (food.ingredients.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            IngredientsReadOnlyContent(
                                ingredients = food.ingredients,
                                onEditClick = {

                                },
                                isEditButtonVisible = false,
                            )
                            FoodAuthor(
                                author = food.author,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            BasicTitle(
                                modifier = Modifier,
                                text = stringResource(id = R.string.description_title)
                            )
                        }
                    }
                    if (food.description.isNotEmpty()) {
                        FoodDescription(description = food.description)
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
private fun SearchFoodBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp)
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
                .padding(start = 16.dp, end = 40.dp, top = 12.dp, bottom = 12.dp),
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        )

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