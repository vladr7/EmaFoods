package com.example.emafoods.feature.signin.presentation

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.emafoods.R
import com.example.emafoods.navigation.home.EmaFoodsNavigation
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@Composable
fun SignInRoute(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    val signInFailedText = stringResource(R.string.sign_in_failed)
    val signInRequestCode = 1
    val authResultLauncher =
        rememberLauncherForActivityResult(contract = AuthResultContract()) { task ->
            try {
                val account = task?.getResult(ApiException::class.java)
                if (account == null) {
                    Toast.makeText(context, signInFailedText, Toast.LENGTH_SHORT).show()
                    viewModel.updateSignInLoading(false)
                } else {
                    coroutineScope.launch {
                        viewModel.signIn(
                            idToken = account.idToken ?: ""
                        )
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(context, signInFailedText, Toast.LENGTH_SHORT).show()
                viewModel.updateSignInLoading(false)
            }
        }

    SignInScreen(
        modifier = modifier,
        onSignInClick = {
            viewModel.updateSignInLoading(true)
            authResultLauncher.launch(signInRequestCode)
        },
        signInLoading = state.signInLoading,
    )

    if (state.signInSuccess) {
        EmaFoodsNavigation(
            userLevel = state.userLevel,
        )
    }
}

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit,
    signInLoading: Boolean,
) {
    SignInBackground(modifier = modifier)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = modifier.weight(0.05f))
        SignInTopBar()
        Spacer(modifier = modifier.weight(0.2f))
        SignInButton(
            modifier = modifier,
            loadingText = stringResource(R.string.signing_in),
            isLoading = signInLoading,
            icon = painterResource(id = R.drawable.ic_google_logo),
            onClick = {
                onSignInClick()
            },
        )
        Spacer(modifier = modifier.weight(0.2f))
    }
}

@Composable
fun SignInBackground(
    modifier: Modifier = Modifier
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.secondary),
        startY = sizeImage.height.toFloat() / 3,
        endY = sizeImage.height.toFloat()
    )

    Box() {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = modifier
                .onGloballyPositioned {
                    sizeImage = it.size
                }
                .fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(gradient)
        )
    }
}

@Composable
fun SignInTopBar(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.chefgirlcircle), contentDescription = null,
            modifier = modifier
                .size(150.dp, 150.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape),
        )
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 36.sp,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.onSurface
                    )
                )
            ),
            textAlign = TextAlign.Center,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.login_subtitle),
            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.tertiary,
                    )
                )
            ),
            textAlign = TextAlign.Center,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
    }
}

@Composable
fun SignInButton(
    modifier: Modifier = Modifier,
    loadingText: String = stringResource(R.string.sigining_in),
    icon: Painter,
    isLoading: Boolean = false,
    shape: Shape = RoundedCornerShape(8.dp),
    borderColor: Color = Color.LightGray,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    progressIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .clickable(
                enabled = !isLoading,
                onClick = onClick
            )
            .shadow(elevation = 16.dp),
        shape = shape,
        border = BorderStroke(width = 1.dp, color = borderColor),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 24.dp,
                    top = 20.dp,
                    bottom = 20.dp
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = "SignInButton",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            SignInButtonText(isLoading, loadingText)
            if (isLoading) {
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
            }
        }
    }
}

@Composable
fun SignInButtonText(
    isLoading: Boolean,
    loadingText: String
) {
    if (isLoading) {
        Text(
            text = loadingText,
            fontSize = 18.sp
        )
    } else {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.typography.bodyLarge.color)) {
                    append(stringResource(id = R.string.login_button_text))
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4285F4)
                    )
                ) {
                    append("G")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDB4437)
                    )
                ) {
                    append("o")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF4B400)
                    )
                ) {
                    append("o")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4285F4)
                    )
                ) {
                    append("g")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F9D58)
                    )
                ) {
                    append("l")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDB4437)
                    )
                ) {
                    append("e")
                }
            },
            fontSize = 18.sp
        )
    }

}

