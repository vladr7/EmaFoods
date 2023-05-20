package com.example.emafoods.feature.profile.domain.model

enum class UserLevel(val string: String) {
    PLEB("Level 1"),
    ADD_DECLINE("Level 2"),
    READ_ALL_LIST("Level 3"),
    EDIT_LIST("Level 4"),
    FULL_ACCESS("Level 5"), ;

    companion object {
        fun fromString(userLevelString: String): UserLevel =
            when (userLevelString) {
                PLEB.string -> PLEB
                ADD_DECLINE.string -> ADD_DECLINE
                READ_ALL_LIST.string -> READ_ALL_LIST
                EDIT_LIST.string -> EDIT_LIST
                FULL_ACCESS.string -> FULL_ACCESS
                else -> {
                    throw IllegalArgumentException("UserLevel not found")
                }
            }

    }
}

