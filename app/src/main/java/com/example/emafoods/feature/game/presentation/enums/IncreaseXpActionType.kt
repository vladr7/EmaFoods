package com.example.emafoods.feature.game.presentation.enums

enum class IncreaseXpActionType(val xp: Int) {
    ADD_RECIPE(xp = 50),
    ADD_REVIEW(xp = 1000),
    GENERATE_RECIPE(xp = 10000),
    RECIPE_ACCEPTED(xp = 15000),
    ADMIN_ACCEPTS_DECLINES_RECIPE(xp = 50),
}

