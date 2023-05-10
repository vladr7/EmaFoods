package com.example.emafoods.feature.addfood.presentation.insert.navigation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.emafoods.core.presentation.stringdecoder.StringDecoder
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.description.navigation.UriIdArg
import com.example.emafoods.feature.addfood.presentation.insert.InsertFoodRoute

const val DescriptionIdArg = "DescriptionIdArg"

class InsertFoodArguments(
    val uri: String,
    val description: String,
) {
    constructor(
        savedStateHandle: SavedStateHandle,
        stringDecoder: StringDecoder
    ) : this(
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[UriIdArg]),
        ),
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[DescriptionIdArg]),
        ),
    )
}

fun NavController.navigateToInsertFood(uriId: String, descriptionId: String) {
    val uri = Uri.encode(uriId)
    val description = Uri.encode(descriptionId)
    this.navigate("${AddFoodDestinations.InsertFood.route}/$uri&$description")
}

fun NavGraphBuilder.insertFoodScreen(
    onConfirmedClick: () -> Unit,
) {
    composable(
        route = "${AddFoodDestinations.InsertFood.route}/{$UriIdArg}&{$DescriptionIdArg}",
        arguments = listOf(
            navArgument(UriIdArg) { type = NavType.StringType },
            navArgument(DescriptionIdArg) { type = NavType.StringType },
        ),
    ) {
        InsertFoodRoute(onConfirmedClick = onConfirmedClick)
    }
}