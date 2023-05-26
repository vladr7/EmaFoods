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
import com.example.emafoods.feature.addfood.presentation.description.DescriptionRoute
import com.example.emafoods.feature.addfood.presentation.insert.navigation.InsertFoodArguments

const val UriIdArg = "UriIdArg"

data class DescriptionArguments(
    val uri: String,
) {
    constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) : this(
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[UriIdArg])
        ),
    )
}

fun NavController.navigateToDescription(uriId: String) {
    this.popBackStack()
    val uri = Uri.encode(uriId)
    this.navigate("${AddFoodDestinations.Description.route}/$uri")
}

fun NavGraphBuilder.descriptionScreen(onConfirmedClick: (InsertFoodArguments) -> Unit) {
    composable(
        route = "${AddFoodDestinations.Description.route}/{$UriIdArg}",
        arguments = listOf(
            navArgument(UriIdArg) {
                type = NavType.StringType
            },
        ),
    ) {
        DescriptionRoute(onConfirmedClick = onConfirmedClick)
    }
}
