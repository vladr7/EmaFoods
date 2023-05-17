package com.example.emafoods.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.emafoods.feature.profile.presentation.ProfileRoute
import com.example.emafoods.navigation.home.HomeBottomDestination

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(HomeBottomDestination.Profile.route, navOptions)
}

fun NavGraphBuilder.profileScreen() {
    composable(route = HomeBottomDestination.Profile.route) {
        ProfileRoute()
    }
}
