package com.example.emafoods.feature.addfood.presentation.congratulation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.emafoods.feature.addfood.presentation.insert.InsertFoodButton

@Composable
fun CongratulationRoute(
    modifier: Modifier = Modifier,
    onInsertNewFoodClick: () -> Unit,
    viewModel: CongratulationViewModel = hiltViewModel(),
) {

    CongratulationScreen(
        modifier = modifier,
        onInsertFoodClick = onInsertNewFoodClick
    )
}

@Composable
fun CongratulationScreen(
    modifier: Modifier = Modifier,
    onInsertFoodClick: () -> Unit,
) {

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        InsertFoodButton(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            onInsertFoodClick = onInsertFoodClick
        )
    }
}

