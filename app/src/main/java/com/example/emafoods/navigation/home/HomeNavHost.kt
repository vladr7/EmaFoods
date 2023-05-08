package com.example.emafoods.navigation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.description.navigation.descriptionScreen
import com.example.emafoods.feature.addfood.presentation.image.navigation.imageScreen
import com.example.emafoods.feature.addfood.presentation.title.navigation.titleScreen
import com.example.emafoods.feature.generatefood.navigation.generateFoodScreen
import com.example.emafoods.feature.listfood.navigation.listFoodScreen

@Composable
fun HomeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = HomeBottomDestination.AddFood.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {

        navigation(
            route = HomeBottomDestination.AddFood.route,
            startDestination = AddFoodDestinations.AddDescription.route
        ) {
            imageScreen(
                onHasImage = { navController.navigate(AddFoodDestinations.AddTitle.route) })
            titleScreen(
                onConfirmedClick = { navController.navigate(AddFoodDestinations.AddDescription.route) },
            )
            descriptionScreen(
                onConfirmedClick = { },
            )
        }
        listFoodScreen()
        generateFoodScreen()
    }
}

