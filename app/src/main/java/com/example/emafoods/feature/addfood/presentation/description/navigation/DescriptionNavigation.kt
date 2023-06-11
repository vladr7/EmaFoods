package com.example.emafoods.feature.addfood.presentation.description.navigation

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
import com.example.emafoods.feature.addfood.presentation.description.DescriptionRoute
import com.example.emafoods.feature.addfood.presentation.ingredients.navigation.IngredientsIdArg
import com.example.emafoods.feature.addfood.presentation.insert.navigation.InsertFoodArguments

const val UriIdArg = "UriIdArg"

data class DescriptionArguments(
    val uri: String,
    val category: String,
    val ingredientsList: String
) {
    constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) : this(
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[UriIdArg])
        ),
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[CategoryIdArg])
        ),
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[IngredientsIdArg])
        ),
    )
}

fun NavController.navigateToDescription(uriId: String, categoryId: String, ingredientsList: String) {
    this.popBackStack()
    val uri = Uri.encode(uriId)
    val category = Uri.encode(categoryId)
    val ingredients = Uri.encode(ingredientsList)
    this.navigate("${AddFoodDestinations.Description.route}/$uri&$category&$ingredients")
}

fun NavGraphBuilder.descriptionScreen(onConfirmedClick: (InsertFoodArguments) -> Unit) {
    composable(
        route = "${AddFoodDestinations.Description.route}/{$UriIdArg}&{$CategoryIdArg}&{$IngredientsIdArg}",
        arguments = listOf(
            navArgument(UriIdArg) {
                type = NavType.StringType
            },
            navArgument(CategoryIdArg) {
                type = NavType.StringType
            },
            navArgument(IngredientsIdArg) {
                type = NavType.StringType
            },
        ),
    ) {
        DescriptionRoute(onConfirmedClick = onConfirmedClick)
    }
}
