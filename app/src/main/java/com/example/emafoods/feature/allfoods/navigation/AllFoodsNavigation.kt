package com.example.emafoods.feature.allfoods.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.emafoods.feature.allfoods.presentation.AllFoodsRoute
import com.example.emafoods.navigation.home.HomeBottomDestination

fun NavGraphBuilder.allFoodsScreen() {
    composable(route = HomeBottomDestination.AllFoods.route) {
        AllFoodsRoute()
    }
}