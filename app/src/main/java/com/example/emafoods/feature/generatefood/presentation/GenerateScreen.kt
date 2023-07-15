package com.example.emafoods.feature.generatefood.presentation

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.emafoods.MainActivity
import com.example.emafoods.R
import com.example.emafoods.core.presentation.animations.bounceClick
import com.example.emafoods.core.presentation.common.alert.AlertDialog2Buttons
import com.example.emafoods.core.presentation.common.alert.LevelUpDialog
import com.example.emafoods.core.presentation.common.alert.XpIncreaseToast
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.addfood.presentation.category.CategoryChoices
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import com.example.emafoods.feature.addfood.presentation.category.OpenCategoryButton
import com.example.emafoods.feature.addfood.presentation.common.TitleWithBackground
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.presentation.ScrollArrow
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType
import com.example.emafoods.feature.pending.presentation.FoodItem
import com.example.emafoods.feature.profile.domain.models.ProfileImage
import com.example.emafoods.feature.profile.presentation.EnterNewUsernameDialog
import com.example.emafoods.feature.profile.presentation.ProfileHeader
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun GenerateScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: GenerateViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = (LocalContext.current as? MainActivity)
    BackHandler {
        if(state.categorySelected) {
            viewModel.onBackClick()
        } else {
            activity?.finish()
        }
    }

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
        userName = state.userName,
        nrOfFireStreaks = state.nrOfFireStreaks,
        profileImage = state.profileImage,
        onProfileImageClick = {
            viewModel.onProfileImageClick(it)
        },
        previousButtonVisible = state.previousButtonVisible,
        onBackClick = {
            viewModel.onBackClick()
        },
        onUserNameClick = {
            viewModel.onShowNewUsernameDialog()
        },
        showNewUsernameDialog = state.showNewUsernameDialog,
        onDismissNewUsernameDialog = {
            viewModel.onDismissNewUsernameDialog()
        },
        onConfirmNewUsernameDialog = {
            viewModel.onConfirmNewUsernameDialog(it)
        }
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
    userName: String,
    nrOfFireStreaks: Int,
    profileImage: ProfileImage,
    onProfileImageClick: (ProfileImage) -> Unit,
    previousButtonVisible: Boolean,
    onBackClick: () -> Unit,
    onUserNameClick: () -> Unit,
    showNewUsernameDialog: Boolean,
    onDismissNewUsernameDialog: () -> Unit,
    onConfirmNewUsernameDialog: (String) -> Unit,
) {
    if (showNewUsernameDialog) {
        EnterNewUsernameDialog(
            onDismissClick = onDismissNewUsernameDialog,
            onConfirmClick = onConfirmNewUsernameDialog
        )
    }
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
            xpIncreased = xpIncreased
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
    var generateButtonsAlpha by remember { mutableStateOf(1f) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {

        if (!categorySelected) {
            WaitingGenerateFoodContent(
                showCategories = showCategories,
                onShowCategoryClick = onCategoryClick,
                onChooseCategoryClick = onChooseCategoryClick,
                userName = userName,
                nrOfFireStreaks = nrOfFireStreaks,
                profileImage = profileImage,
                onProfileImageClick = onProfileImageClick,
                onUserNameClick = onUserNameClick
            )
        } else {
            FoodItem(
                modifier = modifier
                    .verticalScroll(scrollState),
                food = food,
                ingredientsList = food.ingredients,
            )
            GenerateButtons(
                modifier,
                onPreviousButtonClick,
                onGenerateClick = {
                    if (generateButtonsAlpha >= 0.7f) {
                        generateButtonsAlpha -= 0.1f
                    }
                    onGenerateClick()
                },
                alpha = generateButtonsAlpha,
                previousButtonVisible = previousButtonVisible
            )
            CategoryDropDown(
                modifier = modifier,
                expanded = categoryDropdownExpanded,
                onDismissRequest = onDismissCategoryDropDown,
                onClickCategoryDropDown = onClickCategoryDropDown,
                onDropDownItemClick = onDropDownItemClick,
                visible = showFilterAndButtons,
                categoryType = food.categoryType
            )
            BackButton(
                modifier = modifier
                    .align(Alignment.TopStart)
                    .padding(start = 5.dp, top = 5.dp),
                onClick = {
                    onBackClick()
                },
                visible = showFilterAndButtons && categorySelected
            )
        }
        ScrollArrow(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .offset(y = 10.dp)
                .size(80.dp)
                .alpha(generateButtonsAlpha - 0.4f),
            visible = showFilterAndButtons && categorySelected && scrollState.canScrollForward,
            color = MaterialTheme.colorScheme.primaryContainer,
            onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        )
    }
}

@Composable
fun BackButton(
    modifier: Modifier,
    onClick: () -> Unit,
    visible: Boolean
) {
    val brush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondary,
            Color.Transparent
        )
    )
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(),
        exit = slideOutVertically(),
        modifier = modifier
    ) {
        IconButton(
            modifier = modifier
                .size(80.dp),
            onClick = onClick
        ) {
            Icon(
                modifier = modifier
                    .background(brush = brush)
                    .size(40.dp),
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
private fun BoxScope.GenerateButtons(
    modifier: Modifier,
    onPreviousButtonClick: () -> Unit,
    onGenerateClick: () -> Unit,
    alpha: Float = 1f,
    previousButtonVisible: Boolean,
) {
    PreviousGenerateButton(
        modifier = modifier
            .align(Alignment.CenterStart)
            .alpha(alpha),
        onPreviousButtonClick = onPreviousButtonClick,
        previousButtonVisible = previousButtonVisible
    )
    GenerateButton(
        onGenerateClick = onGenerateClick,
        modifier = modifier
            .align(Alignment.CenterEnd)
            .alpha(alpha),
    )
}

@Composable
fun PreviousGenerateButton(
    modifier: Modifier = Modifier,
    onPreviousButtonClick: () -> Unit,
    previousButtonVisible: Boolean,
) {
    val offsetXInitial = (-40).dp
    val coroutineScope = rememberCoroutineScope()
    val offsetXAnimation = remember { Animatable(offsetXInitial.value) }
    val threshold = -30f

    Box(
        modifier = modifier
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    coroutineScope.launch {
                        offsetXAnimation.animateTo(offsetXAnimation.value + delta)
                    }
                },
                onDragStopped = {
                    coroutineScope.launch {
                        if (offsetXAnimation.value > threshold) {
                            onPreviousButtonClick()
                            offsetXAnimation.animateTo(offsetXInitial.value)
                        } else {
                            offsetXAnimation.animateTo(offsetXInitial.value)
                        }
                    }
                }
            )
            .bounceClick {
                onPreviousButtonClick()
            }
            .offset(
                x = if (offsetXAnimation.value > -10f)
                    (-10).dp
                else
                    offsetXAnimation.value.dp
            ),

        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = previousButtonVisible) {
            PreviousButtonArcComposable(
                modifier = modifier,
                offsetXHideButton = offsetXInitial
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
            .width(30.dp + abs(offsetXHideButton.value).dp)
            .zIndex(0f)
            .alpha(0.8f)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val xPosArc =
            -canvasWidth + 20f + offsetXHideButton.value

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
        val xPosArrow = canvasWidth + offsetXHideButton.value
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
) {
    val offsetXInitial = 30.dp
    val coroutineScope = rememberCoroutineScope()
    val offsetXAnimation = remember { Animatable(offsetXInitial.value) }
    val threshold = 28f

    Box(
        modifier = modifier
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    coroutineScope.launch {
                        offsetXAnimation.animateTo(offsetXAnimation.value + delta)
                    }
                },
                onDragStopped = {
                    coroutineScope.launch {
                        if (offsetXAnimation.value < threshold) {
                            onGenerateClick()
                            offsetXAnimation.animateTo(offsetXInitial.value)
                        } else {
                            offsetXAnimation.animateTo(offsetXInitial.value)
                        }
                    }
                }
            )
            .bounceClick {
                onGenerateClick()
            }
            .offset(
                x = if (offsetXAnimation.value < 0f)
                    (0).dp
                else
                    offsetXAnimation.value.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        GenerateArcComposable(
            modifier = modifier,
            offsetXHideButton = offsetXInitial
        )
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
            .width(30.dp + abs(offsetXHideButton.value).dp)
            .zIndex(0f)
            .alpha(0.8f)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val xPosArc = canvasWidth - 180f + offsetXHideButton.value
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
        val xPosArrow = canvasWidth - 160f + offsetXHideButton.value
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
    categoryType: CategoryType,
    visible: Boolean
) {
    val brush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondary,
            Color.Transparent
        )
    )
    Column(
        modifier = modifier
            .align(Alignment.TopEnd)
            .padding(top = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(visible = visible) {
            FilterTextAndIcon(
                onClickCategoryDropDown = onClickCategoryDropDown
            )
            DropdownMenu(
                modifier = modifier
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
                expanded = expanded,
                onDismissRequest = onDismissRequest
            ) {
                DropdownMenuItem(
                    modifier = Modifier
                        .background(color = if (categoryType == CategoryType.MAIN_DISH)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.primaryContainer),
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
                        .background(color = if (categoryType == CategoryType.DESSERT)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.primaryContainer),
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
                        .background(color = if (categoryType == CategoryType.SOUP)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.primaryContainer),
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
                        .background(color = if (categoryType == CategoryType.BREAKFAST)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.primaryContainer),
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
fun FilterTextAndIcon(
    modifier: Modifier = Modifier,
    onClickCategoryDropDown: () -> Unit,
) {
    val brush = Brush.radialGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondary,
            Color.Transparent,
        ),
        radius = 80f,
    )
    Column(
        modifier = modifier
            .bounceClick(onClick = onClickCategoryDropDown)
            .background(brush = brush)
            .padding(end = 15.dp, start = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.filter),
            fontSize = 16.sp,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondary,
            style = TextStyle(
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.secondary,
                    blurRadius = 10f,
                )
            ),
            textAlign = TextAlign.Center,
            modifier = modifier
                .background(
                    brush = brush,
                )
        )

        Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = "Category",
            tint = Color.White,
            modifier = Modifier
                .background(
                    brush = brush,
                )
                .size(50.dp)
        )
    }
}

@Composable
fun BoxScope.WaitingGenerateFoodContent(
    modifier: Modifier = Modifier,
    showCategories: Boolean,
    onShowCategoryClick: () -> Unit,
    onChooseCategoryClick: (CategoryType) -> Unit,
    userName: String,
    nrOfFireStreaks: Int,
    onProfileImageClick: (ProfileImage) -> Unit,
    profileImage: ProfileImage,
    onUserNameClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileHeader(
            userName = userName,
            streaks = nrOfFireStreaks,
            profileTopPadding = 20.dp,
            onProfileImageClick = onProfileImageClick,
            profileImage = profileImage,
            onUsernameClick = onUserNameClick
        )
    }
    AnimatedVisibility(
        visible = !showCategories,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
            .align(Alignment.Center)
    ) {
        TitleWithBackground(
            text = stringResource(id = R.string.generate_title),
            modifier = modifier
                .padding(bottom = 160.dp, top = 20.dp),
            fontSize = 20.sp,
        )
    }
    OpenCategoryButton(
        modifier = modifier
            .align(Alignment.Center),
        onClick = onShowCategoryClick,
        animationVisible = !showCategories,
    )
    CategoryChoices(
        onChooseCategoryClick = onChooseCategoryClick,
        showCategories = showCategories
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