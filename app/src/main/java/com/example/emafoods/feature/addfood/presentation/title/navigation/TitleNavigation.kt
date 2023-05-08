package com.example.emafoods.feature.addfood.presentation.title.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.title.TitleRoute

fun NavGraphBuilder.titleScreen(
    onConfirmedClick: () -> Unit,
) {
    composable(route = AddFoodDestinations.Title.route) {
        TitleRoute(onConfirmedClick = onConfirmedClick)
    }
}