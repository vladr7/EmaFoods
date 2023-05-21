package com.example.emafoods.feature.game.presentation.enums

enum class IncreaseXpActionType(val xp: Int) {
    ADD_RECIPE(xp = 200),
    ADD_REVIEW(xp = 1000),
    GENERATE_RECIPE(xp = 5000),
    RECIPE_ACCEPTED(xp = 500),
    FIRST_TIME_OPENING_APP(xp = 200),
    ADMIN_ACCEPTS_DECLINES_RECIPE(xp = 50),
}

