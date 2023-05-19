package com.example.emafoods.feature.profile.presentation.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.emafoods.feature.profile.navigation.ProfileDestinations
import com.example.emafoods.feature.profile.presentation.profile.ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(ProfileDestinations.Profile.route, navOptions)
}

fun NavGraphBuilder.profileScreen(onGameClick: () -> Unit) {
    composable(route = ProfileDestinations.Profile.route) {
        ProfileRoute(onGameClick = onGameClick)
    }
}
