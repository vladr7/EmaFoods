package com.example.emafoods.feature.pending.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.emafoods.feature.pending.presentation.PendingFoodRoute
import com.example.emafoods.navigation.home.HomeBottomDestination

fun NavGraphBuilder.pendingFoodScreen() {
    composable(route = HomeBottomDestination.PendingFood.route) {
        PendingFoodRoute()
    }
}