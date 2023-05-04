package com.example.emafoods.feature.addfood.presentation.title

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun TitleRoute(
    modifier : Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: TitleViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    TitleScreen()
}

@Composable
fun TitleScreen() {
    Text(text = "Title Screen")
}