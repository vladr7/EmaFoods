package com.example.emafoods.navigation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.congratulation.navigation.congratulationScreen
import com.example.emafoods.feature.addfood.presentation.congratulation.navigation.navigateToCongratulation
import com.example.emafoods.feature.addfood.presentation.insert.navigation.insertFoodScreen
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
            startDestination = AddFoodDestinations.InsertFood.route
        ) {
//            imageScreen(
//                onHasImage = {
//                    navController.navigateToDescription(
//                        it.uriId
//                    )
//                }
//            )
//            descriptionScreen(
//                onConfirmedClick = {
//                    navController.navigateToInsertFood(
//                        uriId = it.uri,
//                        descriptionId = it.description
//                    )
//                },
//            )
            insertFoodScreen(
                onSuccess = {
                    navController.navigateToCongratulation()
                }
            )
            congratulationScreen(
                onInsertNewFoodClick = {
                    navController.navigateUp()
                }
            )
        }
        listFoodScreen()
        generateFoodScreen()
    }
}

