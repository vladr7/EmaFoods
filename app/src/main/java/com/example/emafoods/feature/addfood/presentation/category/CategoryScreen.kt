package com.example.emafoods.feature.addfood.presentation.category

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.emafoods.feature.addfood.presentation.image.navigation.ImageArguments

@Composable
fun CategoryRoute(
    modifier: Modifier = Modifier,
    onNextClicked: (ImageArguments?) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel(),
) {
    CategoryScreen(
        modifier = modifier,
        onNextClicked = onNextClicked,
    )
}

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    onNextClicked: (ImageArguments?) -> Unit
) {
    Button(onClick = {
        onNextClicked(
            ImageArguments(
                category = CategoryType.DESSERT.string
            )
        )
    }) {
        Text(text = "CategoryScreen")
    }
}
