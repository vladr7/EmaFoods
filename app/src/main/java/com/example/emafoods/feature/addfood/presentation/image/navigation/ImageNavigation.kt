package com.example.emafoods.feature.addfood.presentation.image.navigation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.emafoods.core.presentation.stringdecoder.StringDecoder
import com.example.emafoods.feature.addfood.navigation.AddFoodDestinations
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import com.example.emafoods.feature.addfood.presentation.category.navigation.CategoryIdArg
import com.example.emafoods.feature.addfood.presentation.image.ImageRoute
import com.example.emafoods.feature.addfood.presentation.insert.navigation.InsertFoodArguments

const val IMAGE_FROM_GALLERY_FLAG = "IMAGE_FROM_GALLERY_FLAG"
const val TitleIdArg = "TitleArg"
const val UriIdArg = "UriIdArg"

data class ImageArguments(
    val category: String,
) {
    constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) : this(
        stringDecoder.decodeString(
            checkNotNull(savedStateHandle[CategoryIdArg])
        ),
    )
}

fun NavController.navigateToImage(categoryId: String) {
    val category = Uri.encode(categoryId)
    this.navigate("${AddFoodDestinations.Image.route}/$category")
}


fun NavGraphBuilder.imageScreen(onHasImage: (InsertFoodArguments) -> Unit) {
    composable(
        route = "${AddFoodDestinations.Image.route}/{$CategoryIdArg}",
        arguments = listOf(
            navArgument(CategoryIdArg) {
                defaultValue = CategoryType.MAIN_DISH.string
                type = NavType.StringType
            }
        )
    ) {
        ImageRoute(onNextClicked = onHasImage)
    }
}

