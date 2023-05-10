package com.example.emafoods.feature.addfood.presentation.description.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.emafoods.core.presentation.stringdecoder.StringDecoder
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.description.DescriptionRoute

const val UriIdArg = "uriIdArg"

data class DescriptionArguments(
    val uriId: String,
) {
    constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) : this(
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[UriIdArg])
        ),
    )
}

fun NavController.navigateToDescription(uriId: String) {
    this.navigate("${AddFoodDestinations.Description.route}/$uriId")
}

fun NavGraphBuilder.descriptionScreen(onConfirmedClick: () -> Unit) {
    composable(
        route = "${AddFoodDestinations.Description.route}/{$UriIdArg}",
        arguments = listOf(
            navArgument(UriIdArg) { type = NavType.StringType },
        ),
    ) {
        DescriptionRoute(onConfirmedClick = onConfirmedClick)
    }
}
