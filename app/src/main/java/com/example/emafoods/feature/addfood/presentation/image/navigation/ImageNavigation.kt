package com.example.emafoods.feature.addfood.presentation.image.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.description.navigation.DescriptionArguments
import com.example.emafoods.feature.addfood.presentation.image.ImageRoute

fun NavGraphBuilder.imageScreen(onHasImage: (DescriptionArguments?) -> Unit) {
    composable(route = AddFoodDestinations.Image.route) {
        ImageRoute(onHasImage = onHasImage)
    }
}


