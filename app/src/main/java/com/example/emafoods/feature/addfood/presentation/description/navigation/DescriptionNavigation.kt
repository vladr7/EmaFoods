package com.example.emafoods.feature.addfood.presentation.description.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.description.DescriptionRoute

fun NavGraphBuilder.descriptionScreen(onNextClick: () -> Unit) {
    composable(route = AddFoodDestinations.AddDescription.route) {
        DescriptionRoute(onNextClick)
    }
}
