package com.example.emafoods.feature.addfood.presentation.congratulation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.congratulation.CongratulationRoute

fun NavGraphBuilder.congratulationScreen() {
    composable(route = AddFoodDestinations.Congratulation.route) {
        CongratulationRoute()
    }
}

fun NavController.navigateToCongratulation() {
    this.navigate(AddFoodDestinations.Congratulation.route)
}