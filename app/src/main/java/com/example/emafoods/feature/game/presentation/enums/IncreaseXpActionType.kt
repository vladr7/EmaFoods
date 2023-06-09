package com.example.emafoods.feature.game.presentation.enums

enum class IncreaseXpActionType(val xp: Long) {
    ADD_RECIPE(xp = 50),
    ADD_REVIEW(xp = 1000),
    GENERATE_RECIPE(xp = 5),
    RECIPE_ACCEPTED(xp = 1500),
    ADMIN_ACCEPTS_DECLINES_RECIPE(xp = 50),
}

