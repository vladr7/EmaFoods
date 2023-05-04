package com.example.emafoods.feature.addfood.presentation.title

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.feature.addfood.presentation.image.AddImageViewModel


@Composable
fun TitleRoute(
    onBackClick: () -> Unit,
    viewModel: AddImageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    println("vlad: TitleRoute: state: $state")
    TitleScreen()
}

@Composable
fun TitleScreen() {
    Text(text = "Title Screen")
}