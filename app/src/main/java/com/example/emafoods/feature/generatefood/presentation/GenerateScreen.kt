package com.example.emafoods.feature.generatefood.presentation

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.emafoods.core.presentation.common.alert.AlertDialog2Buttons
import com.example.emafoods.core.presentation.common.alert.LevelUpDialog
import com.example.emafoods.core.presentation.common.alert.XpIncreaseToast
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GenerateScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: GenerateViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    GenerateScreenBackground()
    GenerateScreen(
        generatedImagedRef = state.food.imageRef,
        modifier = modifier,
        onGenerateClick = {
            viewModel.onXpIncrease()
            viewModel.generateFoodEvent()
        },
        description = state.food.description,
        foodHasBeenGenerated = state.foodHasBeenGenerated,
        showXpIncreaseToast = state.showXpIncreaseToast,
        xpIncreased = state.xpIncreased,
        onToastShown = { viewModel.onXpIncreaseToastShown() },
        context = context,
        leveledUpEvent = state.leveledUpEvent,
        newLevel = state.newLevel,
        onDismissLevelUp = {
            viewModel.onDismissLevelUp()
            // todo: restart app
//            context.restartApp()
        },
        showRewardsAlert = state.showRewardsAlert,
        nrOfRewards = state.nrOfRewards,
        onDismissRewardsAlert = {
            viewModel.onDismissRewardsAlert()
        }
    )
}

@Composable
fun GenerateScreen(
    generatedImagedRef: String,
    onGenerateClick: () -> Unit,
    description: String,
    modifier: Modifier = Modifier,
    foodHasBeenGenerated: Boolean,
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
    if (showXpIncreaseToast) {
        XpIncreaseToast(
            increaseXpActionType = IncreaseXpActionType.GENERATE_RECIPE,
            onToastShown = onToastShown,
            context = context,
            customXP = xpIncreased
        )
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (!foodHasBeenGenerated) {
            WaitingCookAnimation()
        } else {
            GenerateImage(generatedImagedRef = generatedImagedRef, modifier = modifier)
            Divider(
                color = MaterialTheme.colorScheme.primary, thickness = 2.dp,
                modifier = modifier
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                    .alpha(0.2f),
            )
            GenerateDescription(modifier, description)
        }

        GenerateButton(
            modifier = modifier,
            onGenerateClick = onGenerateClick
        )
    }
}

@Composable
fun RewardsAcquiredAlert(
    nrOfRewards: Int,
    onDismiss: () -> Unit
) {
    val title = if(nrOfRewards == 1) stringResource(
        R.string.your_recipe_has_been_accepted,
        nrOfRewards,
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
fun WaitingCookAnimation(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(start = 32.dp, end = 32.dp, bottom = 32.dp)
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.waitingcook))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            speed = 1f
        )
        LottieAnimation(
            composition = composition,
            progress = { progress },
        )
    }
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

@Composable
fun GenerateButton(
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }

    Box(
        modifier = modifier
            .offset(
                y = if (offsetY.value < -100f) {
                    maxOf(-100f, offsetY.value).dp
                } else if (offsetY.value > 0f) {
                    minOf(offsetY.value, 0f).dp
                } else {
                    offsetY.value.dp
                }
            )
            .draggable(
                state = rememberDraggableState { delta ->
                    coroutineScope.launch {
                        offsetY.animateTo(offsetY.value + delta)
                    }
                },
                orientation = Orientation.Vertical,
                onDragStarted = {

                },
                onDragStopped = {
                    if (offsetY.value < -30f) {
                        onGenerateClick()
                    }
                    coroutineScope.launch {
                        offsetY.animateTo(0f)
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        ArcComposable(modifier = modifier)
    }

}

@Composable
private fun ArcComposable(modifier: Modifier = Modifier) {
    val color1 = MaterialTheme.colorScheme.primary
    val color2 = MaterialTheme.colorScheme.secondary
    val colorOnPrimary = MaterialTheme.colorScheme.onPrimary
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .zIndex(0f)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val formWidth = (canvasWidth * 2)
        val xPos = canvasWidth / 2

        drawArc(
            brush = Brush.verticalGradient(
                colors = listOf(
                    color1,
                    color2
                ),
            ),
            0f,
            -180f,
            useCenter = true,
            size = Size(formWidth, 1000f),
            topLeft = Offset(x = -xPos, y = canvasHeight - 200),
        )

        val handleWidth = 200f
        val handleHeight = 30f

        drawRoundRect(
            color = colorOnPrimary,
            topLeft = Offset(x = xPos - (handleWidth / 2), y = canvasHeight - 160),
            size = Size(handleWidth, handleHeight),
            cornerRadius = CornerRadius(50f, 50f)
        )
    }
}

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