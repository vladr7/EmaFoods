package com.example.emafoods.navigation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.emafoods.feature.addfood.navigation.addFoodNavigationRoute
import com.example.emafoods.feature.addfood.navigation.addFoodScreen

@Composable
fun HomeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = addFoodNavigationRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        addFoodScreen()
    }
}