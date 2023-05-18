package com.example.emafoods.feature.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.emafoods.R

@Composable
fun ProfileRoute(
    modifier: Modifier = Modifier,
) {

    ProfileScreen(
        modifier = modifier,
        onReviewClick = {

        },
        onLevelUpClick = {

        },
        onSignOutClick = {

        }
    )
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onReviewClick: () -> Unit,
    onLevelUpClick: () -> Unit,
    onSignOutClick: () -> Unit,
) {
    ProfileBackground()
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileHeader()
        ProfileReview(onReviewClick = onReviewClick)
        Spacer(modifier = Modifier.weight(1f))
        ProfileLevelUp(onLevelUpClick = onLevelUpClick)
        ProfileSignOut(onSignOutClick = onSignOutClick)
    }
}

@Composable
fun ProfileSignOut(
    modifier: Modifier = Modifier,
    onSignOutClick: () -> Unit
) {
    Button(
        onClick = onSignOutClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 30.dp, top = 20.dp),
    ) {
        Text(
            text = "Sign Out",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProfileLevelUp(
    modifier: Modifier = Modifier,
    onLevelUpClick: () -> Unit,
) {
    Button(
        onClick = onLevelUpClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
    ) {
        Text(
            text = "Level Up",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 36.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.profilepic1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = "Salut!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Row {
                Text(
                    text = "Emanuela Maria",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(
                                    Brush.horizontalGradient(
                                        listOf(
                                            Color.Red,
                                            Color.White
                                        )
                                    ), blendMode = BlendMode.SrcAtop
                                )
                            }
                        },
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileReview(
    modifier: Modifier = Modifier,
    onReviewClick: () -> Unit
) {
    Card(
        onClick = onReviewClick,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth()
            .alpha(0.9f)
    ) {
        Row(
            modifier = modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.reviewhand),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "Care e ideea ta de a imbunatati aplicatia?",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Te apreciem!", style = MaterialTheme.typography.bodySmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .graphicsLayer(alpha = 0.99f)
                            .drawWithCache {
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color.White,
                                                Color.Red,
                                            )
                                        ), blendMode = BlendMode.SrcAtop
                                    )
                                }
                            },
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileBackground(
    modifier: Modifier = Modifier
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondary),
        startY = sizeImage.height.toFloat(),
        endY = 0f
    )

    Box() {
        Image(
            painter = painterResource(id = R.drawable.profilebackground),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = modifier
                .onGloballyPositioned {
                    sizeImage = it.size
                }
                .fillMaxSize()
                .alpha(0.5f),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0.9f)
                .background(gradient)
        )
    }
}