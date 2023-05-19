package com.example.emafoods.feature.profile.presentation.game.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.emafoods.feature.profile.navigation.ProfileDestinations
import com.example.emafoods.feature.profile.presentation.game.GameRoute

fun NavController.navigateToGame(navOptions: NavOptions? = null) {
    this.navigate(ProfileDestinations.Game.route, navOptions)
}

fun NavGraphBuilder.gameScreen() {
    composable(route = ProfileDestinations.Game.route) {
        GameRoute()
    }
}
