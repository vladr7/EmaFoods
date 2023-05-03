package com.example.emafoods.navigation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.navigation.addFoodScreen
import com.example.emafoods.feature.addfood.presentation.title.navigation.titleScreen
import com.example.emafoods.feature.generatefood.navigation.generateFoodScreen
import com.example.emafoods.feature.listfood.navigation.listFoodScreen

@Composable
fun HomeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = HomeBottomDestination.GenerateFood.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {

        addFoodGraph(
            onNextClick = {
                navController.navigate(AddFoodDestinations.AddTitle.route)
            },
            nestedGraph = {
                titleScreen(onBackClick = { navController.navigateUp() })
            }
        )
        listFoodScreen()
        generateFoodScreen()
    }
}

fun NavGraphBuilder.addFoodGraph(
    onNextClick: () -> Unit,
    nestedGraph: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = HomeBottomDestination.AddFood.route,
        startDestination = AddFoodDestinations.AddImage.route
    ) {
        addFoodScreen(onNextClick)
        nestedGraph()
    }
}