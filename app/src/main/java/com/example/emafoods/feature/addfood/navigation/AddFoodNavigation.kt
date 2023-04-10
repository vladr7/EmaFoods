package com.example.emafoods.feature.addfood.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.emafoods.feature.addfood.AddFoodRoute

const val addFoodNavigationRoute = "add_food_route"

fun NavController.navigateToAddFood(navOptions: NavOptions? = null) {
    this.navigate(addFoodNavigationRoute, navOptions)
}

fun NavGraphBuilder.addFoodScreen() {
    composable(route = addFoodNavigationRoute) {
        AddFoodRoute()
    }
}
