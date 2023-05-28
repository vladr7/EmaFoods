package com.example.emafoods.feature.game.presentation

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.R
import com.example.emafoods.feature.game.presentation.model.Permission

@Composable
fun GameRoute(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    GameScreen(
        modifier = modifier,
        userName = state.value.userName,
        userLevel = state.value.userLevel,
        displayXpAlert = state.value.displayXpAlert,
        xpList = state.value.listOfXpActions,
        levelDataList = state.value.listOfLevelPermission,
        onLevelClick = { levelPermission ->
            if (levelPermission.remainingXp <= 0) {
                Toast.makeText(
                    context,
                    "Ai deblocat deja acest level!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Mai ai nevoie de ${levelPermission.remainingXp} XP pentru a debloca ${levelPermission.level.uiString}!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        onIncreaseXpClick = {
            viewModel.onIncreaseXpClick()
        },
        onDismissXpAlertClick = {
            viewModel.onDismissXpAlertClick()
        }
    )
}

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    userName: String,
    userLevel: String,
    onLevelClick: (ViewDataLevelPermission) -> Unit,
    onIncreaseXpClick: () -> Unit,
    displayXpAlert: Boolean,
    onDismissXpAlertClick: () -> Unit,
    xpList: List<String>,
    levelDataList: List<ViewDataLevelPermission>,
) {
    GameBackground()
    if (displayXpAlert) {
        AlertListOfActionsToGainXp(
            title = "Primesti XP pentru urmatoarele actiuni:",
            onDismissClick = onDismissXpAlertClick,
            xpList = xpList,
            dismissText = "OK"
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GameHeader(
            modifier = modifier,
            userName = userName,
            userLevel = userLevel
        )
        LevelList(
            modifier = modifier,
            onLevelClick = onLevelClick,
            levelDataList = levelDataList
        )
        Spacer(modifier = modifier.weight(1f))
        IncreaseXpButton(
            modifier = modifier,
            onIncreaseXpClick = onIncreaseXpClick
        )
    }

}

@Composable
fun IncreaseXpButton(
    modifier: Modifier,
    onIncreaseXpClick: () -> Unit,
) {
    Button(
        onClick = onIncreaseXpClick,
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 36.dp)
            .fillMaxWidth()
    ) {

        Text(
            text = stringResource(id = R.string.how_to_gain_xp),
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .padding(8.dp)
        )
    }
}

@Composable
fun LevelItem(
    modifier: Modifier,
    levelData: ViewDataLevelPermission,
    onLevelClick: (ViewDataLevelPermission) -> Unit
) {
    val alpha = if (levelData.hasAccess) 1f else 0.5f

    Box(
        modifier = modifier
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .alpha(alpha)
                .clickable {
                    onLevelClick(levelData)
                }
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.tertiary
                        ),
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = levelData.level.uiString,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = modifier
                    .padding(bottom = 4.dp)
            )
            Column(
                modifier = modifier
                    .padding(start = 16.dp)
            ) {
                levelData.permissions.forEach { permission ->
                    PermissionItem(
                        modifier = modifier,
                        permission = permission
                    )
                }
            }
        }
    }

}

@Composable
fun PermissionItem(
    modifier: Modifier,
    permission: Permission
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val onSecondary = MaterialTheme.colorScheme.onSecondary
        val onTertiary = MaterialTheme.colorScheme.onTertiary
        Icon(
            imageVector = Icons.Filled.Circle,
            contentDescription = null,
            modifier = Modifier
                .size(16.dp)
                .padding(start = 4.dp)
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            Brush.horizontalGradient(
                                listOf(
                                    onSecondary,
                                    onTertiary
                                )
                            ), blendMode = BlendMode.SrcAtop
                        )
                    }
                },
        )
        Text(
            text = permission.value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun LevelList(
    modifier: Modifier,
    onLevelClick: (ViewDataLevelPermission) -> Unit,
    levelDataList: List<ViewDataLevelPermission>
) {
    Box(
        modifier = modifier
            .height(400.dp)
            .padding(16.dp)
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .verticalScroll(scrollState),
        ) {
            levelDataList.forEach { level ->
                LevelItem(
                    modifier = modifier,
                    levelData = level,
                    onLevelClick = onLevelClick
                )
            }
        }
            ScrollArrow(
                modifier = modifier
                    .offset(y = 16.dp)
                    .align(Alignment.BottomCenter)
                    .padding(top = 16.dp)
                    .alpha(0.8f)
                    .size(64.dp),
                visible = scrollState.value == 0
            )
    }
}

@Composable
fun ScrollArrow(
    modifier: Modifier,
    visible: Boolean = false
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(),
        exit = slideOutVertically(),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun GameHeader(
    modifier: Modifier,
    userName: String,
    userLevel: String
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
                text = userLevel,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Row {
                Text(
                    text = userName,
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

@Composable
fun GameBackground(
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

@Composable
fun AlertListOfActionsToGainXp(
    modifier: Modifier = Modifier,
    title: String,
    dismissText: String,
    onDismissClick: () -> Unit = {},
    xpList: List<String>
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        iconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        textContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = modifier,
        onDismissRequest = { onDismissClick() },
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
            )
        },
        confirmButton = {
            Text(
                text = dismissText,
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { onDismissClick() }
                    .padding(bottom = 16.dp, end = 16.dp)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                xpList.forEach { item ->
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Circle,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp)

                        )
                        Text(
                            text = item,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }

                }
            }
        },
    )
}