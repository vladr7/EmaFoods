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
import com.example.emafoods.feature.addfood.presentation.insert.navigation.InsertFoodArguments

const val UriIdArg = "UriIdArg"

data class DescriptionArguments(
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

fun NavController.navigateToDescription(uriId: String, categoryId: String) {
    this.popBackStack()
    val uri = Uri.encode(uriId)
    val category = Uri.encode(categoryId)
    this.navigate("${AddFoodDestinations.Description.route}/$uri&$category")
}

fun NavGraphBuilder.descriptionScreen(onConfirmedClick: (InsertFoodArguments) -> Unit) {
    composable(
        route = "${AddFoodDestinations.Description.route}/{$UriIdArg}&{$CategoryIdArg}",
        arguments = listOf(
            navArgument(UriIdArg) {
                type = NavType.StringType
            },
            navArgument(CategoryIdArg) {
                type = NavType.StringType
            },
        ),
    ) {
        DescriptionRoute(onConfirmedClick = onConfirmedClick)
    }
}
