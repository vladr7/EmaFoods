package com.example.emafoods.feature.addfood.presentation.category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.R
import com.example.emafoods.core.presentation.animations.bounceClick
import com.example.emafoods.feature.addfood.presentation.common.AddRecipeTitle
import com.example.emafoods.feature.addfood.presentation.image.navigation.ImageArguments

@Composable
fun CategoryRoute(
    modifier: Modifier = Modifier,
    onNextClicked: (ImageArguments) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    CategoryScreen(
        modifier = modifier,
        onNextClicked = onNextClicked,
    )
}

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    onNextClicked: (ImageArguments) -> Unit
) {
    var showCategories by remember { mutableStateOf(false) }
    CategoryScreenBackground()
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = modifier
                .align(Alignment.TopStart)
        ) {
            AddRecipeTitle(
                text = stringResource(R.string.add_a_new_recipe),
                modifier = modifier
            )
            AnimatedVisibility(
                visible = !showCategories,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AddRecipeTitle(
                    text = stringResource(R.string.choose_a_category), fontSize = 24.sp,
                    modifier = modifier
                        .padding(start = 30.dp)
                )
            }
        }
        OpenCategoryButton(
            modifier = modifier
                .align(Alignment.Center),
            onClick = { showCategories = !showCategories }
        )
        CategoryChoices(
            modifier = modifier
                .align(Alignment.Center),
            onNextClicked = onNextClicked,
            showCategories = showCategories,
        )
    }
}

@Composable
fun OpenCategoryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = R.drawable.restaurantmenu),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(90.dp)
            .bounceClick(
                onClick = onClick
            )
    )
}

@Composable
fun CategoryChoices(
    modifier: Modifier = Modifier,
    onNextClicked: (ImageArguments) -> Unit,
    showCategories: Boolean,
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
    ) {
        LeftCategory(showCategories, screenWidth, modifier, onNextClicked)
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxHeight()
        ) {
            TopCategory(
                showCategories, screenHeight, modifier,
                onNextClicked
            )
            BottomCategory(
                showCategories, screenHeight, modifier, onNextClicked
            )
        }
        RightCategory(showCategories, screenWidth, modifier, onNextClicked)
    }
}

@Composable
private fun RowScope.LeftCategory(
    showCategories: Boolean,
    screenWidth: Dp,
    modifier: Modifier,
    onNextClicked: (ImageArguments) -> Unit
) {
    AnimatedVisibility(
        visible = showCategories,
        enter = fadeIn() +
                slideInHorizontally {
                    screenWidth.value.toInt() / 2
                },
        exit = slideOutHorizontally(
            targetOffsetX = {
                screenWidth.value.toInt() / 2
            }
        ) + fadeOut()
    ) {
        CategoryItem(
            modifier = modifier,
            image = R.drawable.breakfast,
            categoryType = CategoryType.BREAKFAST,
            onClick = {
                onNextClicked(
                    ImageArguments(
                        category = CategoryType.BREAKFAST.string
                    )
                )
            }
        )
    }
}

@Composable
private fun RowScope.RightCategory(
    showCategories: Boolean,
    screenWidth: Dp,
    modifier: Modifier,
    onNextClicked: (ImageArguments) -> Unit
) {
    AnimatedVisibility(
        visible = showCategories,
        enter = fadeIn()
                + slideInHorizontally {
            -screenWidth.value.toInt() / 2
        },
        exit = slideOutHorizontally(
            targetOffsetX = {
                -screenWidth.value.toInt() / 2
            }
        ) + fadeOut()
    ) {
        CategoryItem(
            modifier = modifier,
            image = R.drawable.dessert,
            categoryType = CategoryType.DESSERT,
            onClick = {
                onNextClicked(
                    ImageArguments(
                        category = CategoryType.DESSERT.string
                    )
                )
            }
        )
    }
}

@Composable
private fun ColumnScope.BottomCategory(
    showCategories: Boolean,
    screenHeight: Dp,
    modifier: Modifier,
    onNextClicked: (ImageArguments) -> Unit
) {
    AnimatedVisibility(
        visible = showCategories,
        enter = fadeIn() + slideInVertically(
            initialOffsetY = {
                -screenHeight.value.toInt() / 2
            }
        ),
        exit = slideOutVertically(
            targetOffsetY = {
                -screenHeight.value.toInt() / 2
            }
        ) + fadeOut()
    ) {
        CategoryItem(
            modifier = modifier,
            image = R.drawable.soup,
            categoryType = CategoryType.SOUP,
            onClick = {
                onNextClicked(
                    ImageArguments(
                        category = CategoryType.SOUP.string
                    )
                )
            }
        )
    }
}

@Composable
private fun ColumnScope.TopCategory(
    showCategories: Boolean,
    screenHeight: Dp,
    modifier: Modifier,
    onNextClicked: (ImageArguments) -> Unit
) {
    AnimatedVisibility(
        visible = showCategories,
        enter = fadeIn() + slideInVertically(
            initialOffsetY = {
                screenHeight.value.toInt() / 2
            }
        ),
        exit = slideOutVertically(
            targetOffsetY = {
                screenHeight.value.toInt() / 2
            }
        ) + fadeOut()
    ) {
        CategoryItem(
            modifier = modifier,
            image = R.drawable.pasta,
            categoryType = CategoryType.MAIN_DISH,
            onClick = {
                onNextClicked(
                    ImageArguments(
                        category = CategoryType.MAIN_DISH.string
                    )
                )
            }
        )
    }
}

@Composable
fun CategoryItem(
    modifier: Modifier,
    categoryType: CategoryType,
    image: Int,
    onClick: () -> Unit
) {
    val text = when (categoryType) {
        CategoryType.BREAKFAST -> stringResource(R.string.breakfast)
        CategoryType.MAIN_DISH -> stringResource(R.string.main_dish)
        CategoryType.SOUP -> stringResource(R.string.soup)
        CategoryType.DESSERT -> stringResource(R.string.dessert)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp)
                .bounceClick(
                    onClick = onClick
                )
        )
    }
}

@Composable
fun CategoryScreenBackground(
    modifier: Modifier = Modifier
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color.Transparent,
            MaterialTheme.colorScheme.secondary
        ),
//        start = Offset(0f, 0f),
//        end = Offset(sizeImage.width.toFloat(), sizeImage.height.toFloat() / 2),
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