package com.example.emafoods.feature.pending.presentation

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.emafoods.R
import com.example.emafoods.core.presentation.models.FoodViewData
import kotlinx.coroutines.launch

@Composable
fun PendingFoodRoute(
    modifier: Modifier = Modifier,
    viewModel: PendingFoodViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    PendingFoodScreen(
        modifier = modifier,
        food = state.food,
        onSwipeLeft = {
            viewModel.onSwipeLeft()
            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
        },
        onSwipeRight = {
            Toast.makeText(context, "Accept", Toast.LENGTH_SHORT).show()
            viewModel.onSwipeRight()
        }
    )
}

@Composable
fun PendingFoodScreen(
    modifier: Modifier = Modifier,
    food: FoodViewData,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FoodItem(
            food = food,
            modifier = modifier,
        )
        Spacer(modifier = Modifier.weight(1f))
        PendingSwipe(
            modifier = modifier,
            onSwipeRight = onSwipeRight,
            onSwipeLeft = onSwipeLeft,
        )
        Spacer(modifier = Modifier.weight(1f))
    }

}

@Composable
fun FoodItem(
    food: FoodViewData,
    modifier: Modifier = Modifier,
) {
    val color by animateColorAsState(
        targetValue = MaterialTheme.colors.secondary
    )

    Card(
        modifier = Modifier
            .padding(16.dp),
        elevation = 8.dp, shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
                .background(color)
        ) {
            PendingFoodImage(imageUri = food.imageRef)
            PendingFoodDescription(description = food.description)
            PendingFoodAuthor(author = food.author)
        }
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
            .padding(26.dp),
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
fun RejectFood() {
    Icon(
        Icons.Filled.Delete, contentDescription = "Reject Food",
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
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
    println("vlad: ${offsetX.value}")

    Icon(
        Icons.Filled.LunchDining, contentDescription = "Swipable Food",
        modifier = Modifier
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
            .padding(70.dp)
            .height(50.dp)
            .width(50.dp)
    )
}

@Composable
fun AcceptFood() {
    Icon(
        Icons.Filled.Favorite, contentDescription = "Accept Food",
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun PendingFoodAuthor(
    modifier: Modifier = Modifier,
    author: String
) {
    Row {
        Spacer(modifier = modifier.weight(1f))
        Text(
            text = author,
            fontFamily = androidx.compose.material3.MaterialTheme.typography.bodyMedium.fontFamily,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(
                        androidx.compose.material3.MaterialTheme.colorScheme.onSecondary,
                        androidx.compose.material3.MaterialTheme.colorScheme.onSecondary,
                    )
                )
            ),
            fontSize = 12.sp,
            textAlign = TextAlign.Left,
            modifier = modifier
                .padding(
                    start = 25.dp, end = 20.dp, top = 10.dp, bottom = 10.dp
                )
        )
    }

}

@Composable
fun PendingFoodImage(
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
            .data(imageUri.ifEmpty { R.drawable.cutecelebrationbackgr })
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        loading = {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(100.dp)
            )
        }
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun PendingFoodDescription(
    description: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = description,
        fontFamily = androidx.compose.material3.MaterialTheme.typography.titleSmall.fontFamily,
        fontWeight = FontWeight.Bold,
        style = TextStyle(
            brush = Brush.linearGradient(
                colors = listOf(
                    androidx.compose.material3.MaterialTheme.colorScheme.onSecondary,
                    androidx.compose.material3.MaterialTheme.colorScheme.onSecondary,
                )
            )
        ),
        fontSize = 16.sp,
        textAlign = TextAlign.Left,
        modifier = modifier
            .padding(
                start = 25.dp, end = 20.dp, top = 10.dp, bottom = 10.dp
            )
    )
}