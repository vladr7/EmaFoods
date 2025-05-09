package com.example.emafoods.feature.pending.presentation

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.TextField
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
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
import com.example.emafoods.feature.addfood.presentation.image.AttachFileIcon
import com.example.emafoods.feature.addfood.presentation.image.TakePictureIcon
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import com.example.emafoods.feature.addfood.presentation.insert.CategoryTypeRow
import com.example.emafoods.feature.addfood.presentation.insert.IngredientsList
import com.example.emafoods.feature.allfoods.presentation.EditPencil
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
        showMovedFailed = state.showMovedFailed,
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
    showMovedFailed: Boolean,
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
    if(showMovedFailed) {
        Toast.makeText(
            context,
            stringResource(R.string.recipe_added_failed), Toast.LENGTH_SHORT
        ).show()
        onErrorShown()
    }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

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
            FoodItem(
                food = food,
                modifier = modifier,
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
        if (ingredientsList.isNotEmpty() && scrollState.canScrollForward && scrollState.maxValue > 100) {
            ScrollArrow(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 10.dp)
                    .size(80.dp),
                visible = scrollState.value == 0,
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }
            )
        }
    }
}

@Composable
fun FoodItem(
    food: FoodViewData,
    modifier: Modifier = Modifier,
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
                showFoodAuthor = true,
                isFoodNew = false,
            )
            if (isCategoryTypeVisible) {
                CategoryTypeRow(categoryType = food.categoryType)
            }
            FoodTitle(text = food.title)
            if (food.ingredients.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IngredientsList(
                        ingredients = food.ingredients,
                        onEditClick = {

                        },
                        isEditButtonVisible = false,
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
                    message = stringResource(id = R.string.for_the_moment_there_are_no_more_pending_foods)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun FoodTitle(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 20.sp,
) {
    Text(
        text = text,
        fontSize = fontSize,
        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Start,
        color = MaterialTheme.colorScheme.onSecondary,
        style = TextStyle(
            shadow = Shadow(
                color = Color.Yellow,
                blurRadius = 4f,
                offset = Offset(1f, 1f)
            )
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp, top = 10.dp)
    )
}

@Composable
fun EmptyDescriptionMessage(
    modifier: Modifier = Modifier,
    message: String,
) {
    Text(
        text = message,
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
    author: String,
    showFoodAuthor: Boolean,
) {
    Row(
        modifier = modifier
            .width(200.dp)
            .padding(
                start = 25.dp, end = 20.dp, bottom = 15.dp
            ),
        horizontalArrangement = Arrangement.End
    ) {
        AnimatedVisibility(visible = showFoodAuthor) {
            Text(
                text = " $author ",
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSecondary,
                style = TextStyle(
                    background = Color.White.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
fun FoodImage(
    modifier: Modifier = Modifier,
    imageUri: String,
    author: String,
    showFoodAuthor: Boolean,
    isFoodNew: Boolean,
    showImageEditButton: Boolean = false,
    onUriRetrieved: (Uri?) -> Unit = {},
    ) {
    var showImagePickers by remember { mutableStateOf(false) }
    var newUriChangedByAdmin by remember { mutableStateOf(Uri.EMPTY) }

    Box(modifier = modifier) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(10.dp)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(8.dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    if (newUriChangedByAdmin != Uri.EMPTY) newUriChangedByAdmin else
                        imageUri.ifEmpty { R.drawable.radish }
                )
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            loading = {
                LoadingCookingAnimation()
            }
        )
        if (isFoodNew) {
            NewFoodTag(
                modifier = Modifier
                    .align(Alignment.TopEnd)
            )
        }
        AnimatedVisibility(visible = showImageEditButton && !showImagePickers) {
            EditPencil(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        showImagePickers = true
                    }
                    .align(Alignment.BottomStart)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,
                                Color.Transparent
                            ),
                            radius = 60f
                        )
                    )
            )
        }
        AnimatedVisibility(visible = showImagePickers) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                AttachFileIcon(
                    onUriRetrieved = {
                        newUriChangedByAdmin = it
                        onUriRetrieved(it)
                    },
                    modifier = Modifier.size(80.dp) .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,
                                Color.Transparent
                            ),
                            radius = 90f
                        )
                    )

                )
                TakePictureIcon(
                    onUriRetrieved = {
                        newUriChangedByAdmin = it
                        onUriRetrieved(it)
                    },
                    modifier = Modifier.size(80.dp) .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,
                                Color.Transparent
                            ),
                            radius = 90f
                        )
                    )
                )
            }
        }
        FoodAuthor(
            author = author,
            modifier = Modifier
                .align(Alignment.BottomEnd),
            showFoodAuthor = showFoodAuthor
        )
    }
}

@Composable
fun NewFoodTag(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = R.drawable.new_icon),
        contentDescription = null,
        modifier = modifier
            .height(70.dp)
            .width(70.dp)
            .padding(10.dp)
    )
}

@Composable
fun FoodDescription(
    description: String,
    modifier: Modifier = Modifier,
    isEditable: Boolean = false,
    onDescriptionChange: (String) -> Unit = {},
) {
    if (!isEditable) {
        var descriptionDisplay by remember {
            mutableStateOf("")
        }
        val minHeight = 0.6f * description.length

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
                .heightIn(min = minHeight.dp, max = 1500.dp)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 25.dp, end = 20.dp, top = 5.dp, bottom = 10.dp
                ),
        )
    } else {
        TextField(
            value = description,
            onValueChange = {
                onDescriptionChange(it)
            },
            textStyle = TextStyle(
                fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onSecondary,
            ),
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp, max = 1500.dp)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 25.dp, end = 20.dp, top = 5.dp, bottom = 10.dp
                ),
        )
    }
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
