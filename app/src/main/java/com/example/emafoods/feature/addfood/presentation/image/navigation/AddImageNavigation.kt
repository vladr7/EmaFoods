package com.example.emafoods.feature.addfood.presentation.image.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.image.AddImageRoute

fun NavGraphBuilder.addImageScreen(onNextClick: () -> Unit) {
    composable(route = AddFoodDestinations.AddImage.route) {
        AddImageRoute(onNextClick)
    }
}
