package com.example.emafoods.feature.addfood.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.emafoods.feature.addfood.presentation.AddFoodRoute
import com.example.emafoods.navigation.home.HomeBottomDestination


fun NavController.navigateToAddFood(navOptions: NavOptions? = null) {
    this.navigate(HomeBottomDestination.AddFood.route, navOptions)
}

fun NavGraphBuilder.addFoodScreen() {
    composable(route = HomeBottomDestination.AddFood.route) {
        AddFoodRoute()
    }
}
