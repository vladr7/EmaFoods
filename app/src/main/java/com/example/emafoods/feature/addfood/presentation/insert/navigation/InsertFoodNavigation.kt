package com.example.emafoods.feature.addfood.presentation.insert.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.insert.InsertFoodRoute

fun NavGraphBuilder.insertFoodScreen(
    onSuccess: () -> Unit,
) {
    composable(route = AddFoodDestinations.InsertFood.route) {
        InsertFoodRoute(onSuccess = onSuccess)
    }
}