package com.example.emafoods.feature.pending.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SwipeLeft
import androidx.compose.material.icons.filled.SwipeRight
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.emafoods.R
import com.example.emafoods.core.presentation.animations.LottieAnimationContent
import com.example.emafoods.core.presentation.animations.bounceClick
import com.example.emafoods.core.presentation.features.addfood.BasicTitle
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import com.example.emafoods.feature.addfood.presentation.insert.CategoryTypeRow
import com.example.emafoods.feature.addfood.presentation.insert.IngredientsReadOnlyContent
import com.example.emafoods.feature.game.presentation.ScrollArrow
import com.example.emafoods.feature.generatefood.presentation.LoadingCookingAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


@Composable
fun PendingFoodRoute(
    modifier: Modifier = Modifier,
    viewModel: PendingFoodViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    PendingFoodScreen(
        modifier = modifier,
        food = state.currentFood,
        error = state.error,
        showError = state.showError,
        onSwipeLeft = {
            viewModel.onSwipeLeft()
        },
        onSwipeRight = {
            viewModel.onSwipeRight()
        },
        context = context,
        onErrorShown = {
            viewModel.onResetMessageStates()
        },
        showMovedSuccessfully = state.showMovedSuccessfully,
        showDeletedSuccessfully = state.showDeleteSuccessfully,
        ingredientsList = state.currentFood.ingredients,
    )
}

@Composable
fun PendingFoodScreen(
    modifier: Modifier = Modifier,
    food: FoodViewData,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    error: String,
    context: Context,
    showError: Boolean,
    onErrorShown: () -> Unit,
    showMovedSuccessfully: Boolean,
    showDeletedSuccessfully: Boolean,
    ingredientsList: List<IngredientViewData>,
) {
    if (showError) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        onErrorShown()
    }
    if (showMovedSuccessfully) {
        Toast.makeText(
            context,
            stringResource(R.string.recipe_has_been_accepted),
            Toast.LENGTH_SHORT
        ).show()
        onErrorShown()
    }
    if (showDeletedSuccessfully) {
        Toast.makeText(
            context,
            stringResource(R.string.recipe_has_been_declined), Toast.LENGTH_SHORT
        ).show()
        onErrorShown()
    }
    val scrollState = rememberScrollState()

    PendingFoodBackground(imageId = R.drawable.pendingbackground)
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .verticalScroll(scrollState)
        ) {
            println("vlad: food description: ${food.description}")
            FoodItem(
                food = food,
                modifier = modifier,
                ingredientsList = ingredientsList,
                isCategoryTypeVisible = food.id.isNotEmpty()
            )
            if (ingredientsList.isNotEmpty()) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    PendingSwipeTips(
                        modifier = modifier
                    )
                    PendingSwipe(
                        modifier = modifier,
                        onSwipeRight = onSwipeRight,
                        onSwipeLeft = onSwipeLeft,
                    )
                }
            } else {
                val random: Float = 0.5f + Random.nextFloat() * (0.8f - 0.5f)
                LottieAnimationContent(
                    animationId = R.raw.cutedancingchicken,
                    speed = random,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }
        }
        if(ingredientsList.isNotEmpty() && scrollState.canScrollForward) {
            ScrollArrow(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 10.dp)
                    .size(80.dp),
                visible = scrollState.value == 0,
            )
        }
    }
}

@Composable
fun FoodItem(
    food: FoodViewData,
    modifier: Modifier = Modifier,
    ingredientsList: List<IngredientViewData>,
    isCategoryTypeVisible: Boolean = false,
) {
    val color by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.secondary, label = ""
    )

    Card(
        modifier = modifier
            .heightIn(max = 5500.dp)
            .padding(16.dp),
        elevation = 8.dp, shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
                .background(color)
        ) {
            FoodImage(imageUri = food.imageRef)
            if(isCategoryTypeVisible) {
                CategoryTypeRow(categoryType = food.categoryType)
            }
            if (ingredientsList.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IngredientsReadOnlyContent(
                        ingredients = ingredientsList,
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
            if(food.description.isNotEmpty()) {
                FoodDescription(description = food.description)
            } else {
                EmptyDescriptionMessage()
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun EmptyDescriptionMessage(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.for_the_moment_there_are_no_more_pending_foods),
        fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        textAlign = TextAlign.Left,
        color = MaterialTheme.colorScheme.onSecondary,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 250.dp)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 25.dp, end = 20.dp, top = 5.dp, bottom = 10.dp
            ),
    )
}

@Composable
fun PendingSwipeTips(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SwipeLeftTip()
        SwipeRightTip()
    }
}

@Composable
fun PendingSwipe(
    modifier: Modifier = Modifier,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 26.dp, end = 26.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RejectFood()
        SwipableFood(
            onSwipeLeft = onSwipeLeft,
            onSwipeRight = onSwipeRight,
        )
        AcceptFood()
    }
}

@Composable
fun SwipeLeftTip() {
    Icon(
        Icons.Filled.SwipeLeft, contentDescription = "Swipe Left",
        modifier = Modifier
            .height(30.dp)
            .width(30.dp)
            .alpha(0.5f),
        tint = MaterialTheme.colorScheme.onSecondary
    )
}

@Composable
fun SwipeRightTip() {
    Icon(
        Icons.Filled.SwipeRight, contentDescription = "Swipe Right",
        modifier = Modifier
            .height(30.dp)
            .width(30.dp)
            .alpha(0.5f),
        tint = MaterialTheme.colorScheme.onSecondary
    )
}

@Composable
fun RejectFood() {
    Icon(
        Icons.Filled.Close, contentDescription = "Reject Food",
        modifier = Modifier
            .height(50.dp)
            .width(50.dp),
        tint = MaterialTheme.colorScheme.onSecondary
    )
}

@Composable
fun SwipableFood(
    modifier: Modifier = Modifier,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }

    Image(
        painterResource(id = R.drawable.foodswipe), contentDescription = "Swipable Food",
        modifier = Modifier
            .bounceClick()
            .offset(
                x = (offsetX.value.toInt() * 2).dp,
            )
            .draggable(
                state = rememberDraggableState { delta ->
                    coroutineScope.launch {
                        offsetX.animateTo(offsetX.value + delta)
                    }
                },
                orientation = Orientation.Horizontal,
                onDragStarted = {

                },
                onDragStopped = {
                    if ((offsetX.value.toInt() * 2) < -80f) {
                        onSwipeLeft()
                    }
                    if ((offsetX.value.toInt() * 2) > 80f) {
                        onSwipeRight()
                    }
                    coroutineScope.launch {
                        offsetX.animateTo(0f)
                    }
                }
            )
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondary,
                        Color.Transparent
                    )
                ),
                alpha = 0.5f,
            )
            .padding(20.dp)
            .height(70.dp)
            .width(70.dp),
    )
}

@Composable
fun AcceptFood() {
    Icon(
        Icons.Filled.Check, contentDescription = "Accept Food",
        modifier = Modifier
            .height(50.dp)
            .width(50.dp),
        tint = MaterialTheme.colorScheme.onSecondary
    )
}

@Composable
fun FoodAuthor(
    modifier: Modifier = Modifier,
    author: String
) {
    Text(
        text = author,
        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        textAlign = TextAlign.Left,
        color = MaterialTheme.colorScheme.onSecondary,
        modifier = modifier
            .padding(
                start = 25.dp, end = 20.dp,
            )
    )
}

@Composable
fun FoodImage(
    modifier: Modifier = Modifier,
    imageUri: String,
) {
    SubcomposeAsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(10.dp)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(8.dp)),
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUri.ifEmpty { R.drawable.radish })
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        loading = {
            LoadingCookingAnimation()
        }
    )
}

@Composable
fun FoodDescription(
    description: String,
    modifier: Modifier = Modifier
) {
    var descriptionDisplay by remember {
        mutableStateOf("")
    }

    LaunchedEffect(
        key1 = description,
    ) {
        description.forEachIndexed { charIndex, _ ->
            descriptionDisplay = description
                .substring(
                    startIndex = 0,
                    endIndex = charIndex + 1,
                )
            delay(2)
        }
    }

    Text(
        text = descriptionDisplay,
        fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        textAlign = TextAlign.Left,
        color = MaterialTheme.colorScheme.onSecondary,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 250.dp)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 25.dp, end = 20.dp, top = 5.dp, bottom = 10.dp
            ),
    )
}

@Composable
fun PendingFoodBackground(
    modifier: Modifier = Modifier,
    imageId: Int,
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            MaterialTheme.colorScheme.secondary
        ),
        startY = 900f,
        endY = sizeImage.height.toFloat(),
    )

    Box() {
        Image(
            painter = painterResource(id = imageId),
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