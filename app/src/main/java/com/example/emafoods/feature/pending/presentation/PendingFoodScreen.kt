package com.example.emafoods.feature.pending.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    )
}

@Composable
fun PendingFoodScreen(
    modifier: Modifier = Modifier,
    food: FoodViewData,
) {
    FoodItem(
        food = food,
    )
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