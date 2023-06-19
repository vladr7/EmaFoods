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
import com.example.emafoods.feature.addfood.presentation.image.navigation.TitleIdArg
import com.example.emafoods.feature.addfood.presentation.image.navigation.UriIdArg
import com.example.emafoods.feature.addfood.presentation.insert.InsertFoodRoute

class InsertFoodArguments(
    val uri: String,
    val category: String,
    val title: String
) {
    constructor(
        savedStateHandle: SavedStateHandle,
        stringDecoder: StringDecoder
    ) : this(
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[UriIdArg]),
        ),
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[CategoryIdArg]),
        ),
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[TitleIdArg]),
        ),
    )
}

fun NavController.navigateToInsertFood(
    uriId: String,
    categoryId: String,
    title: String
) {
    this.popBackStack()
    val uri = Uri.encode(uriId)
    val categoryEncoded = Uri.encode(categoryId)
    val titleEncoded = Uri.encode(title)
    this.navigate("${AddFoodDestinations.InsertFood.route}/$uri&$categoryEncoded&$titleEncoded")
}

fun NavGraphBuilder.insertFoodScreen(
    onSuccess: () -> Unit,
    onBackPressed: () -> Unit,
) {
    composable(
        route = "${AddFoodDestinations.InsertFood.route}/{$UriIdArg}&{$CategoryIdArg}&{$TitleIdArg}",
        arguments = listOf(
            navArgument(UriIdArg) {
                type = NavType.StringType
            },
            navArgument(CategoryIdArg) {
                type = NavType.StringType
            },
            navArgument(TitleIdArg) {
                type = NavType.StringType
            },
        ),
    ) {
        InsertFoodRoute(onSuccess = onSuccess, onBackPressed = onBackPressed)
    }
}