package com.example.emafoods.feature.listfood.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.emafoods.feature.listfood.ListFoodRoute
import com.example.emafoods.navigation.home.HomeBottomDestination

fun NavController.navigateToListFood(navOptions: NavOptions? = null) {
    this.navigate(HomeBottomDestination.ListFood.route, navOptions)
}

fun NavGraphBuilder.listFoodScreen() {
    composable(route = HomeBottomDestination.ListFood.route) {
        ListFoodRoute()
    }
}
