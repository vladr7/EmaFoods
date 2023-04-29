package com.example.emafoods.feature.generatefood.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.emafoods.feature.generatefood.GenerateScreenRoute
import com.example.emafoods.navigation.home.HomeBottomDestination

fun NavController.navigateToGenerateFood(navOptions: NavOptions? = null) {
    this.navigate(HomeBottomDestination.GenerateFood.route, navOptions)
}

fun NavGraphBuilder.generateFoodScreen() {
    composable(route = HomeBottomDestination.GenerateFood.route) {
        GenerateScreenRoute()
    }
}
