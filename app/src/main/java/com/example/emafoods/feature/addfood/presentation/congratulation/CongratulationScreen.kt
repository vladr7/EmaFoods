package com.example.emafoods.feature.addfood.presentation.congratulation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CongratulationRoute(
    modifier: Modifier = Modifier,
    viewModel: CongratulationViewModel = hiltViewModel(),
) {

    CongratulationScreen(
        modifier = modifier,
    )
}

@Composable
fun CongratulationScreen(
    modifier: Modifier = Modifier,
) {

}