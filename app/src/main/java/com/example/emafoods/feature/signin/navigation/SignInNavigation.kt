package com.example.emafoods.feature.signin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.emafoods.feature.signin.presentation.SignInRoute

const val signInNavigationRoute = "sign_in_route"

fun NavController.navigateToSignIn(navOptions: NavOptions? = null) {
    this.navigate(signInNavigationRoute, navOptions)
}

fun NavGraphBuilder.signInScreen() {
    composable(route = signInNavigationRoute) {
        SignInRoute()
    }
}
