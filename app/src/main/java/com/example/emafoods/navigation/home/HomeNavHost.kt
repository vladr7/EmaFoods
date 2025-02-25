package com.example.emafoods.navigation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.category.navigation.categoryScreen
import com.example.emafoods.feature.addfood.presentation.congratulation.navigation.congratulationScreen
import com.example.emafoods.feature.addfood.presentation.congratulation.navigation.navigateToCongratulation
import com.example.emafoods.feature.addfood.presentation.image.navigation.imageScreen
import com.example.emafoods.feature.addfood.presentation.image.navigation.navigateToImage
import com.example.emafoods.feature.addfood.presentation.insert.navigation.insertFoodScreen
import com.example.emafoods.feature.addfood.presentation.insert.navigation.navigateToInsertFood
import com.example.emafoods.feature.allfoods.navigation.allFoodsScreen
import com.example.emafoods.feature.game.presentation.navigation.gameScreen
import com.example.emafoods.feature.game.presentation.navigation.navigateToGame
import com.example.emafoods.feature.generatefood.navigation.generateFoodScreen
import com.example.emafoods.feature.pending.presentation.navigation.pendingFoodScreen
import com.example.emafoods.feature.profile.navigation.ProfileDestinations
import com.example.emafoods.feature.profile.presentation.navigation.profileScreen

@Composable
fun HomeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = HomeBottomDestination.AllFoods.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {

        navigation(
            route = HomeBottomDestination.AddFood.route,
            startDestination = AddFoodDestinations.Category.route
        ) {
            categoryScreen(
                onChoseCategory = { imageArguments ->
                    navController.navigateToImage(
                        categoryId = imageArguments.category
                    )
                }
            )
            imageScreen(
                onHasImage = { insertFoodArguments ->
                    navController.navigateToInsertFood(
                        uriId = insertFoodArguments.uri,
                        categoryId = insertFoodArguments.category,
                        title = insertFoodArguments.title
                    )
                }
            )
            insertFoodScreen(
                onSuccess = {
                    navController.navigateToCongratulation()
                },
                onBackPressed = {
                    navController.navigateUp()
                }
            )
            congratulationScreen(
                onInsertNewFoodClick = {
                    navController.navigateUp()
                }
            )
        }
        generateFoodScreen()
        pendingFoodScreen()
        allFoodsScreen()
        navigation(
            route = HomeBottomDestination.Profile.route,
            startDestination = ProfileDestinations.Profile.route
        ) {
            profileScreen(
                onGameClick = {
                    navController.navigateToGame()
                }
            )
            gameScreen()
        }
    }
}

