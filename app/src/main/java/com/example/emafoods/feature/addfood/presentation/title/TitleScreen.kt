package com.example.emafoods.feature.addfood.presentation.title

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun TitleRoute(
    onBackClick: () -> Unit
) {
    TitleScreen()
}

@Composable
fun TitleScreen() {
    Text(text = "Title Screen")
}