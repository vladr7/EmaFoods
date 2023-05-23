package com.example.emafoods.feature.game.domain.model

enum class UserLevel(val string: String, val xp: Int) {
    LEVEL_1(string = "Level 1", xp = 0), // pleb
    LEVEL_2(string = "Level 2", xp = 10000), // add/ decline
    LEVEL_3(string = "Level 3", xp = 20000), // read all list
    LEVEL_4(string = "Level 4", xp = 30000), // edit list
    LEVEL_5(string = "Level 5", xp = 100000),;  // full access

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

