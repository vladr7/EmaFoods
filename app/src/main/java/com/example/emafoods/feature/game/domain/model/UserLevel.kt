package com.example.emafoods.feature.game.domain.model

enum class UserLevel(val string: String, val xp: Int) {
    LEVEL_1(string = "Nivel 1", xp = 0), // pleb
    LEVEL_2(string = "Nivel 2", xp = 10000), // read all list
    LEVEL_3(string = "Nivel 3", xp = 20000), // add/ decline
    LEVEL_4(string = "Nivel 4", xp = 30000), // edit pending food before added
    LEVEL_5(string = "Nivel 5", xp = 100000),;  // suprize

    companion object {
        fun fromString(userLevelString: String): UserLevel =
            when (userLevelString) {
                LEVEL_1.string -> LEVEL_1
                LEVEL_2.string -> LEVEL_2
                LEVEL_3.string -> LEVEL_3
                LEVEL_4.string -> LEVEL_4
                LEVEL_5.string -> LEVEL_5
                else -> {
                    throw IllegalArgumentException("UserLevel not found")
                }
            }

    }
}

