package com.example.emafoods.feature.addfood.presentation.ingredients.navigation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.emafoods.core.presentation.stringdecoder.StringDecoder
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.category.navigation.CategoryIdArg
import com.example.emafoods.feature.addfood.presentation.description.navigation.DescriptionArguments
import com.example.emafoods.feature.addfood.presentation.description.navigation.UriIdArg
import com.example.emafoods.feature.addfood.presentation.ingredients.IngredientsRoute

data class IngredientsArguments(
    val uri: String,
    val category: String,
) {
    constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) : this(
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[UriIdArg])
        ),
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[CategoryIdArg])
        ),
    )
}

fun NavController.navigateToIngredients(uriId: String, categoryId: String) {
    this.popBackStack()
    val uri = Uri.encode(uriId)
    val category = Uri.encode(categoryId)
    this.navigate("${AddFoodDestinations.Ingredients.route}/$uri&$category")
}

fun NavGraphBuilder.ingredientsScreen(onConfirmedClick: (DescriptionArguments) -> Unit) {
    composable(
        route = "${AddFoodDestinations.Ingredients.route}/{$UriIdArg}&{$CategoryIdArg}",
        arguments = listOf(
            navArgument(UriIdArg) {
                type = NavType.StringType
            },
            navArgument(CategoryIdArg) {
                type = NavType.StringType
            },
        ),
    ) {
        IngredientsRoute(onConfirmedClick = onConfirmedClick)
    }
}