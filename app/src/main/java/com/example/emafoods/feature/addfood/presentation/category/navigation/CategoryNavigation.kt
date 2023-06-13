package com.example.emafoods.feature.addfood.presentation.category.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.category.CategoryRoute
import com.example.emafoods.feature.addfood.presentation.image.navigation.ImageArguments

const val CategoryIdArg = "CategoryIdArg"

fun NavGraphBuilder.categoryScreen(onChoseCategory: (ImageArguments) -> Unit) {
    composable(route = AddFoodDestinations.Category.route) {
        CategoryRoute(onNextClicked = onChoseCategory)
    }
}


