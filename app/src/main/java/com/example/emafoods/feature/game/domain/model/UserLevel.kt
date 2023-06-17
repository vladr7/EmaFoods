package com.example.emafoods.feature.game.domain.model

enum class UserLevel(val string: String, val xp: Int, val uiString: String) {
    LEVEL_1(string = "LEVEL_1", uiString = "Nivel 1", xp = 0), // pleb
    LEVEL_2(string = "LEVEL_2", uiString = "Nivel 2", xp = 20000), // more avatar icons
    LEVEL_3(string = "LEVEL_3", uiString = "Nivel 3", xp = 100000); // suprize

    companion object {
        fun fromString(userLevelString: String): UserLevel =
            when (userLevelString) {
                LEVEL_1.string -> LEVEL_1
                LEVEL_2.string -> LEVEL_2
                LEVEL_3.string -> LEVEL_3
                else -> {
                    LEVEL_1
                }
            }
    }
}

