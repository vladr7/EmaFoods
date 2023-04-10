package com.example.emafoods.navigation.signin

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.emafoods.feature.signin.navigation.signInNavigationRoute
import com.example.emafoods.feature.signin.navigation.signInScreen

@Composable
fun SignInNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = signInNavigationRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        signInScreen()
    }
}