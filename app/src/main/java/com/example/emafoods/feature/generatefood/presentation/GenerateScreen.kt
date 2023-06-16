package com.example.emafoods.feature.generatefood.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.emafoods.R
import com.example.emafoods.core.presentation.animations.bounceClick
import com.example.emafoods.core.presentation.common.alert.AlertDialog2Buttons
import com.example.emafoods.core.presentation.common.alert.LevelUpDialog
import com.example.emafoods.core.presentation.common.alert.XpIncreaseToast
import com.example.emafoods.core.presentation.features.addfood.BasicTitle
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.addfood.presentation.category.CategoryChoices
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import com.example.emafoods.feature.addfood.presentation.category.OpenCategoryButton
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.presentation.ScrollArrow
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType
import com.example.emafoods.feature.pending.presentation.FoodItem
import com.example.emafoods.feature.profile.presentation.ProfileHeader
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun GenerateScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: GenerateViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    GenerateScreenBackground()
    GenerateScreen(
        modifier = modifier,
        onGenerateClick = {
            viewModel.onXpIncrease()
            viewModel.generateFoodEvent()
        },
        categorySelected = state.categorySelected,
        showXpIncreaseToast = state.showXpIncreaseToast,
        xpIncreased = state.xpIncreased,
        onToastShown = { viewModel.onXpIncreaseToastShown() },
        context = context,
        leveledUpEvent = state.leveledUpEvent,
        newLevel = state.newLevel,
        onDismissLevelUp = {
            viewModel.onDismissLevelUp()
        },
        showRewardsAlert = state.showRewardsAlert,
        nrOfRewards = state.nrOfRewards,
        onDismissRewardsAlert = {
            viewModel.onDismissRewardsAlert()
        },
        showCategories = state.showCategories,
        onCategoryClick = {
            viewModel.onCategoryClick()
        },
        onChooseCategoryClick = { categoryType ->
            viewModel.onCategorySelected(categoryType)
        },
        food = state.currentFood,
        showEmptyListToast = state.showEmptyListToast,
        onShowedEmptyListToast = {
            viewModel.onShowedEmptyListToast()
        },
        categoryDropdownExpanded = state.categoryDropdownExpanded,
        onDismissCategoryDropDown = {
            viewModel.onDismissCategoryDropDown()
        },
        onClickCategoryDropDown = {
            viewModel.onClickCategoryDropDown()
        },
        onDropDownItemClick = { categoryType ->
            viewModel.onCategorySelected(categoryType)
            viewModel.onDismissCategoryDropDown()
        },
        onPreviousButtonClick = {
            viewModel.onPreviousButtonClick()
        },
        previousButtonVisible = state.previousButtonVisible,
    )
}

@Composable
fun GenerateScreen(
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier,
    categorySelected: Boolean,
    showXpIncreaseToast: Boolean,
    onToastShown: () -> Unit,
    context: Context,
    xpIncreased: Long,
    leveledUpEvent: Boolean,
    newLevel: UserLevel?,
    onDismissLevelUp: () -> Unit,
    showRewardsAlert: Boolean,
    nrOfRewards: Int,
    onDismissRewardsAlert: () -> Unit,
    showCategories: Boolean,
    onCategoryClick: () -> Unit,
    onChooseCategoryClick: (CategoryType) -> Unit,
    food: FoodViewData,
    showEmptyListToast: Boolean,
    onShowedEmptyListToast: () -> Unit,
    onDismissCategoryDropDown: () -> Unit,
    categoryDropdownExpanded: Boolean,
    onClickCategoryDropDown: () -> Unit,
    onDropDownItemClick: (CategoryType) -> Unit,
    onPreviousButtonClick: () -> Unit,
    previousButtonVisible: Boolean
) {
    if (showRewardsAlert) {
        RewardsAcquiredAlert(
            nrOfRewards = nrOfRewards,
            onDismiss = onDismissRewardsAlert
        )
    }
    if (leveledUpEvent && !showRewardsAlert) {
        LevelUpDialog(
            newLevel = newLevel,
            onDismiss = onDismissLevelUp,
        )
    }
    if (showXpIncreaseToast && !leveledUpEvent && !showRewardsAlert) {
        XpIncreaseToast(
            increaseXpActionType = IncreaseXpActionType.GENERATE_RECIPE,
            onToastShown = onToastShown,
            context = context,
            customXP = xpIncreased
        )
    }
    if (showEmptyListToast) {
        Toast.makeText(
            context,
            stringResource(R.string.recipes_could_not_be_found),
            Toast.LENGTH_SHORT
        ).show()
        onShowedEmptyListToast()
    }

    val scrollState = rememberScrollState()
    var showFilterAndButtons by remember { mutableStateOf(false) }
    val scrollVisibilityThreshold = 80
    showFilterAndButtons = scrollState.value < scrollVisibilityThreshold

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {

        if (!categorySelected) {
            WaitingGenerateFoodContent(
                showCategories = showCategories,
                onShowCategoryClick = onCategoryClick,
                onChooseCategoryClick = onChooseCategoryClick,
            )

        } else {
            FoodItem(
                modifier = modifier
                    .verticalScroll(scrollState),
                food = food,
                ingredientsList = food.ingredients,
            )
            PreviousGenerateButton(
                modifier = modifier
                    .align(Alignment.CenterStart),
                onPreviousButtonClick = onPreviousButtonClick,
                previousButtonVisible = (previousButtonVisible && showFilterAndButtons)
            )
            GenerateButton(
                onGenerateClick = onGenerateClick,
                modifier = modifier
                    .align(Alignment.CenterEnd),
                visible = showFilterAndButtons
            )
            CategoryDropDown(
                modifier = modifier,
                expanded = categoryDropdownExpanded,
                onDismissRequest = onDismissCategoryDropDown,
                onClickCategoryDropDown = onClickCategoryDropDown,
                onDropDownItemClick = onDropDownItemClick,
                visible = showFilterAndButtons
            )
        }
        ScrollArrow(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .offset(y = 10.dp)
                .size(80.dp),
            visible = showFilterAndButtons,
            color = MaterialTheme.colorScheme.primaryContainer
        )
    }
}

@Composable
fun PreviousGenerateButton(
    modifier: Modifier = Modifier,
    onPreviousButtonClick: () -> Unit,
    previousButtonVisible: Boolean
) {
    val offsetXHideButton = (-40).dp
    Box(
        modifier = modifier
            .offset(x = offsetXHideButton)
            .bounceClick {
                onPreviousButtonClick()
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = previousButtonVisible,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            PreviousButtonArcComposable(
                modifier = modifier,
                offsetXHideButton = offsetXHideButton
            )
        }
    }
}

@Composable
private fun PreviousButtonArcComposable(
    modifier: Modifier = Modifier,
    offsetXHideButton: Dp,
) {
    val color1 = MaterialTheme.colorScheme.primaryContainer
    val color2 = MaterialTheme.colorScheme.primary
    val colorOnPrimary = MaterialTheme.colorScheme.onPrimaryContainer
    Canvas(
        modifier = modifier
            .height(500.dp)
            .width(70.dp + abs(offsetXHideButton.value).dp)
            .zIndex(0f)
            .alpha(0.8f)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val xPosArc =
            -canvasWidth - 80f

        drawArc(
            brush = Brush.verticalGradient(
                colors = listOf(
                    color1,
                    color2
                ),
            ),
            startAngle = -90f,
            sweepAngle = 180f,
            useCenter = true,
            size = Size(canvasWidth * 2, canvasHeight),
            topLeft = Offset(x = xPosArc, y = 0f),
        )

        val arrowWidth = 30f
        val arrowHeight = 100f
        val xPosArrow = canvasWidth - 110f
        val arrowPath = Path().let {
            it.moveTo(x = xPosArrow, y = canvasHeight / 2 - arrowHeight / 2) // Top
            it.lineTo(x = xPosArrow - arrowWidth + 15f, y = canvasHeight / 2) // Left
            it.lineTo(x = xPosArrow, y = canvasHeight / 2 + arrowHeight / 2) // Bottom
            it.lineTo(x = xPosArrow - arrowWidth, y = canvasHeight / 2) // Far left
            it.close()
            it
        }
        drawPath(
            path = arrowPath,
            color = colorOnPrimary,
        )
    }
}

@Composable
fun GenerateButton(
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean
) {
    val offsetXHideButton = 25.dp
    Box(
        modifier = modifier
            .offset(x = offsetXHideButton)
            .bounceClick {
                onGenerateClick()
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            GenerateArcComposable(
                modifier = modifier,
                offsetXHideButton = offsetXHideButton
            )
        }
    }
}

@Composable
private fun GenerateArcComposable(
    modifier: Modifier = Modifier,
    offsetXHideButton: Dp,
) {
    val color1 = MaterialTheme.colorScheme.primaryContainer
    val color2 = MaterialTheme.colorScheme.primary
    val colorOnPrimary = MaterialTheme.colorScheme.onPrimaryContainer
    Canvas(
        modifier = modifier
            .height(500.dp)
            .width(70.dp + abs(offsetXHideButton.value).dp)
            .zIndex(0f)
            .alpha(0.8f)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val xPosArc = canvasWidth - 180f
        drawArc(
            brush = Brush.verticalGradient(
                colors = listOf(
                    color1,
                    color2
                ),
            ),
            startAngle = 90f,
            sweepAngle = 180f,
            useCenter = true,
            size = Size(canvasWidth * 2, canvasHeight),
            topLeft = Offset(x = xPosArc, y = 0f),
        )

        val arrowWidth = 30f
        val arrowHeight = 100f
        val xPosArrow = canvasWidth - 140f
        val arrowPath = Path().let {
            it.moveTo(x = xPosArrow, y = canvasHeight / 2 - arrowHeight / 2) // Top
            it.lineTo(x = xPosArrow + arrowWidth - 15f, y = canvasHeight / 2) // Right
            it.lineTo(x = xPosArrow, y = canvasHeight / 2 + arrowHeight / 2) // Bottom
            it.lineTo(x = xPosArrow + arrowWidth, y = canvasHeight / 2) // Far right
            it.close()
            it
        }
        drawPath(
            path = arrowPath,
            color = colorOnPrimary,
        )
    }
}

@Composable
fun BoxScope.CategoryDropDown(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onClickCategoryDropDown: () -> Unit,
    onDropDownItemClick: (CategoryType) -> Unit,
    visible: Boolean
) {
    Column(
        modifier = modifier
            .align(Alignment.TopEnd)
            .padding(top = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = visible) {
            IconButton(
                onClick = onClickCategoryDropDown,
                modifier = Modifier
                    .size(100.dp)
                    .bounceClick()
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Category",
                    tint = Color.White,
                    modifier = Modifier
                        .size(50.dp)
                )
            }
            DropdownMenu(
                modifier = Modifier,
                expanded = expanded,
                onDismissRequest = onDismissRequest
            ) {
                DropdownMenuItem(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primaryContainer),
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
                        onDropDownItemClick(CategoryType.MAIN_DISH)
                    })
                Divider()
                DropdownMenuItem(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primaryContainer),
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
                        onDropDownItemClick(CategoryType.DESSERT)
                    })
                Divider()
                DropdownMenuItem(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primaryContainer),
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
                        onDropDownItemClick(CategoryType.SOUP)
                    })
                Divider()
                DropdownMenuItem(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primaryContainer),
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
                        onDropDownItemClick(CategoryType.BREAKFAST)
                    })
            }
        }
    }
}

@Composable
fun BoxScope.WaitingGenerateFoodContent(
    modifier: Modifier = Modifier,
    showCategories: Boolean,
    onShowCategoryClick: () -> Unit,
    onChooseCategoryClick: (CategoryType) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileHeader(
            userName = "Vlad Ricean",
            streaks = 5,
            profileTopPadding = 20.dp,
        )
    }
    AnimatedVisibility(
        visible = !showCategories,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
            .align(Alignment.Center)
    ) {
        GenerateTitle(
            modifier = modifier
                .padding(bottom = 140.dp)
        )
    }
    OpenCategoryButton(
        modifier = modifier
            .align(Alignment.Center),
        onClick = onShowCategoryClick,
    )
    CategoryChoices(
        onChooseCategoryClick = onChooseCategoryClick,
        showCategories = showCategories
    )
}

@Composable
fun GenerateTitle(
    modifier: Modifier = Modifier
) {
    BasicTitle(
        modifier = modifier,
        text = stringResource(id = R.string.generate_title),
        fontSize = 18.sp,
    )
}

@Composable
fun RewardsAcquiredAlert(
    nrOfRewards: Int,
    onDismiss: () -> Unit
) {
    val title = if (nrOfRewards == 1) stringResource(
        R.string.your_recipe_has_been_accepted,
        nrOfRewards * IncreaseXpActionType.RECIPE_ACCEPTED.xp
    )
    else stringResource(
        R.string.your_receipes_have_been_accepted,
        nrOfRewards,
        nrOfRewards * IncreaseXpActionType.RECIPE_ACCEPTED.xp
    )
    AlertDialog2Buttons(
        title = title,
        confirmText = "YAY!",
        onConfirmClick = { onDismiss() },
    )
}

@Composable
fun LoadingCookingAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cookinganimation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = 3f
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}

@Composable
fun GenerateDescription(
    modifier: Modifier,
    description: String,
) {
    var textToDisplay by remember {
        mutableStateOf("")
    }

    LaunchedEffect(
        key1 = description
    ) {
        description.forEachIndexed { charIndex, _ ->
            textToDisplay = description
                .substring(
                    startIndex = 0,
                    endIndex = charIndex + 1,
                )
            delay(2)
        }
    }

    Text(
        text = textToDisplay,
        fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
        fontWeight = FontWeight.Bold,
        style = TextStyle(
            brush = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.onSecondary,
                    MaterialTheme.colorScheme.onSecondary,
                )
            )
        ),
        fontSize = 16.sp,
        textAlign = TextAlign.Left,
        modifier = modifier
            .heightIn(max = 300.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(start = 25.dp, end = 20.dp, top = 10.dp)
    )


}

@Composable
fun GenerateImage(
    generatedImagedRef: String,
    modifier: Modifier
) {

    SubcomposeAsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(20.dp)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(8.dp)),
        model = ImageRequest.Builder(LocalContext.current)
            .data(generatedImagedRef)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        loading = {
            LoadingCookingAnimation()
        },
        error = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(20.dp)
                    .shadow(elevation = 16.dp, shape = RoundedCornerShape(8.dp)),
            ) {
                Text(text = "Error loading image ${it.result.throwable.message}")
            }
        }
    )
}

//@Composable
//fun GenerateButton(
//    onGenerateClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val coroutineScope = rememberCoroutineScope()
//    val offsetY = remember { Animatable(0f) }
//    val threshold = -20f
//    Box(
//        modifier = modifier
//            .offset(
//                y = if (offsetY.value < -100f) {
//                    maxOf(-100f, offsetY.value).dp
//                } else if (offsetY.value > 0f) {
//                    minOf(offsetY.value, 0f).dp
//                } else {
//                    offsetY.value.dp
//                }
//            )
//            .draggable(
//                state = rememberDraggableState { delta ->
//                    coroutineScope.launch {
//                        offsetY.animateTo(offsetY.value + delta)
//                    }
//                },
//                orientation = Orientation.Vertical,
//                onDragStarted = {
//
//                },
//                onDragStopped = {
//                    if (offsetY.value < threshold) {
//                        onGenerateClick()
//                    }
//                    coroutineScope.launch {
//                        offsetY.animateTo(0f)
//                    }
//                },
//            ),
//        contentAlignment = Alignment.Center
//    ) {
////        ArcComposable(
////            modifier = modifier,
////            exceededThreshold = offsetY.value < threshold
////        )
//    }
//
//}

//@Composable
//private fun ArcComposable(
//    modifier: Modifier = Modifier,
//    exceededThreshold: Boolean = false,
//) {
//    val color1 =
//        if (!exceededThreshold) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
//    val color2 = MaterialTheme.colorScheme.secondary
//    val colorOnPrimary = MaterialTheme.colorScheme.onPrimary
//    Canvas(
//        modifier = modifier
//            .fillMaxSize()
//            .zIndex(0f)
//    ) {
//        val canvasWidth = size.width
//        val canvasHeight = size.height
//        val formWidth = (canvasWidth * 2)
//        val xPos = canvasWidth / 2
//
//        drawArc(
//            brush = Brush.verticalGradient(
//                colors = listOf(
//                    color1,
//                    color2
//                ),
//            ),
//            0f,
//            -180f,
//            useCenter = true,
//            size = Size(formWidth, 1000f),
//            topLeft = Offset(x = -xPos, y = canvasHeight - 200),
//        )
//
//        val handleWidth = 200f
//        val handleHeight = 30f
//
//        drawRoundRect(
//            color = colorOnPrimary,
//            topLeft = Offset(x = xPos - (handleWidth / 2), y = canvasHeight - 160),
//            size = Size(handleWidth, handleHeight),
//            cornerRadius = CornerRadius(50f, 50f)
//        )
//    }
//}

@Composable
fun GenerateScreenBackground(
    modifier: Modifier = Modifier,
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            MaterialTheme.colorScheme.secondary
        ),
        startY = sizeImage.height.toFloat(),
        endY = 900f,
    )

    Box() {
        Image(
            painter = painterResource(R.drawable.generetebk),
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