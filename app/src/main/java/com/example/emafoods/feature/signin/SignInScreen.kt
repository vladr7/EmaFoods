package com.example.emafoods.feature.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emafoods.R
import com.google.relay.compose.RowScopeInstanceImpl.weight

@Composable
fun SignInRoute(
    modifier: Modifier = Modifier,
) {
    SignInScreen(
        modifier = modifier
    )
}

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        contentScale = ContentScale.FillHeight,
        modifier = modifier
            .fillMaxSize(),
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        SignInTopBar()
        Spacer(modifier = Modifier.size(300.dp))
        SignInBottomBar()
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun SignInTopBar(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(top = 32.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.vegetables1), contentDescription = null,
            modifier = modifier
                .size(150.dp, 150.dp)
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                ),
        )
        Text(
            text = "EmaFoods",
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
        )
    }
}

@Composable
fun SignInBottomBar() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier.size(300.dp, 50.dp)
    ) {
        Text(text = "Sign In")
    }
}