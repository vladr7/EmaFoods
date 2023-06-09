package com.example.emafoods.feature.addfood.presentation.category

enum class CategoryType(val string: String) {
    BREAKFAST("BREAKFAST"),
    MAIN_DISH("MAIN_DISH"),
    SOUP("SOUP"),
    DESSERT("DESSERT");

    companion object {
        fun fromString(string: String): CategoryType {
            return when (string) {
                "BREAKFAST" -> BREAKFAST
                "MAIN_DISH" -> MAIN_DISH
                "SOUP" -> SOUP
                "DESSERT" -> DESSERT
                else -> MAIN_DISH
            }
        }
    }
}