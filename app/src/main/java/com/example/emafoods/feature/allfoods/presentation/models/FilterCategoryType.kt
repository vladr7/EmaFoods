package com.example.emafoods.feature.allfoods.presentation.models

import com.example.emafoods.feature.addfood.presentation.category.CategoryType

enum class FilterCategoryType(val string: String) {
    BREAKFAST("BREAKFAST"),
    MAIN_DISH("MAIN_DISH"),
    SOUP("SOUP"),
    DESSERT("DESSERT"),
    ALL("ALL");

    companion object {
        fun fromString(string: String): FilterCategoryType {
            return when (string) {
                "BREAKFAST" -> BREAKFAST
                "MAIN_DISH" -> MAIN_DISH
                "SOUP" -> SOUP
                "DESSERT" -> DESSERT
                "ALL" -> ALL
                else -> MAIN_DISH
            }
        }
    }
}

fun CategoryType.toFilterCategoryType(): FilterCategoryType {
    return when (this) {
        CategoryType.BREAKFAST -> FilterCategoryType.BREAKFAST
        CategoryType.MAIN_DISH -> FilterCategoryType.MAIN_DISH
        CategoryType.SOUP -> FilterCategoryType.SOUP
        CategoryType.DESSERT -> FilterCategoryType.DESSERT
    }
}