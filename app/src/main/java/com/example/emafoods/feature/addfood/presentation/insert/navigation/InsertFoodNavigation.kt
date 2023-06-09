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
import com.example.emafoods.feature.addfood.presentation.category.navigation.CategoryIdArg
import com.example.emafoods.feature.addfood.presentation.description.navigation.UriIdArg
import com.example.emafoods.feature.addfood.presentation.insert.InsertFoodRoute

const val DescriptionIdArg = "DescriptionIdArg"

class InsertFoodArguments(
    val uri: String,
    val description: String,
    val category: String
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
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[CategoryIdArg]),
        ),
    )
}

fun NavController.navigateToInsertFood(uriId: String, descriptionId: String, categoryId: String) {
    this.popBackStack()
    val uri = Uri.encode(uriId)
    val description = Uri.encode(descriptionId)
    val categoryEncoded = Uri.encode(categoryId)
    this.navigate("${AddFoodDestinations.InsertFood.route}/$uri&$description&$categoryEncoded")
}

fun NavGraphBuilder.insertFoodScreen(
    onSuccess: () -> Unit,
) {
    composable(
        route = "${AddFoodDestinations.InsertFood.route}/{$UriIdArg}&{$DescriptionIdArg}&{$CategoryIdArg}",
        arguments = listOf(
            navArgument(UriIdArg) { type = NavType.StringType },
            navArgument(DescriptionIdArg) { type = NavType.StringType },
            navArgument(CategoryIdArg) { type = NavType.StringType },
        ),
    ) {
        InsertFoodRoute(onSuccess = onSuccess)
    }
}