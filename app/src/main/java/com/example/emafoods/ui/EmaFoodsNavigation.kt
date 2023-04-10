package com.example.emafoods.ui

import androidx.compose.runtime.Composable
import com.example.emafoods.navigation.home.HomeNavHost

@Composable
fun EmaFoodsNavigation(
    emaFoodsAppState: EmaFoodsAppState = rememberEmaFoodsAppState()
) {
    val navController = emaFoodsAppState.navController
    HomeNavHost(navController = navController)
}
