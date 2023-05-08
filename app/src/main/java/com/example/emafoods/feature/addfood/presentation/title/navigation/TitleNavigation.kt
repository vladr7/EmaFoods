package com.example.emafoods.feature.addfood.presentation.title.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.title.TitleRoute

fun NavGraphBuilder.titleScreen(
    onBackClick: () -> Unit,
) {
    composable(route = AddFoodDestinations.AddTitle.route) {
        TitleRoute(onBackClick = onBackClick)
    }
}