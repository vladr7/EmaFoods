package com.example.emafoods.feature.profile.presentation.game

import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Favorite
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
import com.example.emafoods.R
import com.example.emafoods.feature.profile.presentation.game.model.Level
import com.example.emafoods.feature.profile.presentation.game.model.Permission

@Composable
fun GameRoute(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    GameScreen(
        modifier = modifier,
        userName = "User Name",
        userLevel = "Nivel 1",
        onLevelClick = { level ->
            if (level.remainingXpNeeded == 0) {
                Toast.makeText(
                    context,
                    "Ai deblocat nivelul ${level.levelNumber}!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Mai ai nevoie de ${level.remainingXpNeeded} XP pentru a debloca acest nivel!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
}

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    userName: String,
    userLevel: String,
    onLevelClick: (Level) -> Unit
) {
    GameBackground()
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
            onLevelClick = onLevelClick
        )
        Spacer(modifier = modifier.weight(1f))
        LevelUpButton(
            modifier = modifier
        )
    }

}

@Composable
fun LevelUpButton(modifier: Modifier) {
    Button(
        onClick = { },
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
    level: Level,
    onLevelClick: (Level) -> Unit
) {
    val alpha = if (level.hasAccess) 1f else 0.5f

    Box(
        modifier = modifier
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .alpha(alpha)
                .clickable {
                    onLevelClick(level)
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
                text = level.levelNumber.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = modifier
                    .padding(bottom = 4.dp)
            )
            Column(
                modifier = modifier
                    .padding(start = 16.dp)
            ) {
                level.permissions.forEach { permission ->
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
    levels: List<Level> = listOf(
        Level(
            levelNumber = 1,
            remainingXpNeeded = 100,
            hasAccess = true,
            permissions = listOf(
                Permission.MAIN_LIST_VISIBLE,
                Permission.EDIT_MAIN,
            )
        ),
        Level(
            levelNumber = 1,
            remainingXpNeeded = 100,
            hasAccess = true,
            permissions = listOf(
                Permission.MAIN_LIST_VISIBLE,
                Permission.EDIT_MAIN,
            )
        ),
        Level(
            levelNumber = 1,
            remainingXpNeeded = 100,
            hasAccess = true,
            permissions = listOf(
                Permission.MAIN_LIST_VISIBLE,
                Permission.EDIT_MAIN,
            )
        ),
        Level(
            levelNumber = 1,
            remainingXpNeeded = 100,
            hasAccess = true,
            permissions = listOf(
                Permission.MAIN_LIST_VISIBLE,
                Permission.EDIT_MAIN,
            )
        ),
    ),
    onLevelClick: (Level) -> Unit
) {
    Column(
        modifier = modifier
            .height(400.dp)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        levels.forEach { level ->
            LevelItem(
                modifier = modifier,
                level = level,
                onLevelClick = onLevelClick
            )
        }
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