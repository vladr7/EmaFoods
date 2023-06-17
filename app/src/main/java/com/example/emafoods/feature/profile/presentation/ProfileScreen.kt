package com.example.emafoods.feature.profile.presentation

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiNature
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.R
import com.example.emafoods.core.extension.restartApp
import com.example.emafoods.core.presentation.animations.LottieAnimationContent
import com.example.emafoods.core.presentation.animations.bounceClick
import com.example.emafoods.core.presentation.common.alert.AlertDialog2Buttons
import com.example.emafoods.core.presentation.common.alert.LevelUpDialog
import com.example.emafoods.core.presentation.common.alert.XpIncreaseToast
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType
import com.example.emafoods.feature.profile.domain.models.ProfileImage
import com.google.android.play.core.review.ReviewManagerFactory

@Composable
fun ProfileRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onGameClick: () -> Unit
) {
    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val manager = ReviewManagerFactory.create(context)

    ProfileScreen(
        modifier = modifier,
        onReviewClick = {
            viewModel.onReviewClick()
            // can be tested after app is published (or in internal testing)
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    val flow = activity?.let { manager.launchReviewFlow(it, reviewInfo) }
                    flow?.addOnCompleteListener {
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                        viewModel.onXpIncrease()
                    }
                } else {
                    // There was some problem, log or handle the error code.
                }
            }
        },
        onLevelUpClick = {
            viewModel.onLevelUpClick()
            onGameClick()
        },
        onSignOutClick = {
            viewModel.onSignOutClick()
        },
        onDismiss = {
            viewModel.onDismissSignOutAlert()
        },
        onConfirm = {
            viewModel.onConfirmSignOut()
            activity?.finish()
        },
        userName = state.userName,
        showSignOutAlert = state.showSignOutAlert,
        showXpIncreaseToast = state.showXpIncreaseToast,
        context = context,
        onIncreaseXpToastShown = {
            viewModel.onXpIncreaseToastShown()
        },
        streaks = state.streaks,
        newLevel = state.newLevel,
        leveledUpEvent = state.leveledUpEvent,
        onDismissLevelUp = {
            viewModel.onDismissLevelUp()
            context.restartApp()
        },
        onProfileImageClick = {
            viewModel.onProfileImageClick(it)
        },
        profileImage = state.profileImage,
    )
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onReviewClick: () -> Unit,
    onLevelUpClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    userName: String,
    showSignOutAlert: Boolean,
    showXpIncreaseToast: Boolean,
    onIncreaseXpToastShown: () -> Unit,
    context: Context,
    streaks: Int,
    newLevel: UserLevel?,
    leveledUpEvent: Boolean,
    onDismissLevelUp: () -> Unit,
    onProfileImageClick: (ProfileImage) -> Unit,
    profileImage: ProfileImage,
) {
    if (leveledUpEvent) {
        LevelUpDialog(
            newLevel = newLevel,
            onDismiss = onDismissLevelUp,
        )
    }
    if (showXpIncreaseToast) {
        XpIncreaseToast(
            increaseXpActionType = IncreaseXpActionType.ADD_REVIEW,
            onToastShown = onIncreaseXpToastShown,
            context = context,
        )
    }
    ProfileBackground()
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showSignOutAlert) {
            SignOutAlert(
                onDismiss = onDismiss,
                onConfirm = onConfirm
            )
        }
        ProfileHeader(
            userName = userName,
            streaks = streaks,
            profileImage = profileImage,
            onProfileImageClick = onProfileImageClick
        )
        ProfileReview(onReviewClick = onReviewClick)
        Spacer(modifier = Modifier.weight(1f))
        ProfileLevelUp(onLevelUpClick = onLevelUpClick)
        ProfileSignOut(onSignOutClick = onSignOutClick)
    }
}

@Composable
fun SignOutAlert(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog2Buttons(
        modifier = modifier,
        title = stringResource(R.string.logout_popup_title),
        dismissText = stringResource(R.string.logout_popup_cancel),
        confirmText = stringResource(R.string.logout_button),
        onDismissClick = onDismiss,
        onConfirmClick = onConfirm
    )
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
            text = stringResource(R.string.logout_button),
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
            text = stringResource(R.string.level_up_button),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    userName: String,
    streaks: Int,
    profileTopPadding: Dp = 36.dp,
    onProfileImageClick: (ProfileImage) -> Unit,
    profileImage: ProfileImage
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        FireStreaks(modifier, streaks)

        Row(
            modifier = modifier
                .padding(start = 16.dp, end = 16.dp, top = profileTopPadding, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = profileImage.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .bounceClick {
                        onProfileImageClick(profileImage)
                    }
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = stringResource(R.string.profile_hello),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                ProfileUserName(userName)

            }
        }
    }
}

@Composable
private fun ProfileUserName(userName: String) {
    Row {
        Text(
            text = "$userName!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Icon(
            imageVector = Icons.Filled.EmojiNature,
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
                                    Color.Yellow,
                                    Color.White
                                )
                            ), blendMode = BlendMode.SrcAtop
                        )
                    }
                },
        )
    }
}

@Composable
private fun BoxScope.FireStreaks(modifier: Modifier, streaks: Int) {
    Row(
        modifier = modifier
            .align(Alignment.TopEnd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$streaks", style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center,
            modifier = modifier
                .offset(y = 5.dp)
        )
        LottieAnimationContent(animationId = R.raw.firestreak, modifier = modifier.size(35.dp))
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
                    text = stringResource(R.string.review_description),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.review_bold_description),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
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