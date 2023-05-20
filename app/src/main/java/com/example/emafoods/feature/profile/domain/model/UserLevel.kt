package com.example.emafoods.feature.profile.domain.model

enum class UserLevel(val level: String) {
    PLEB("Level 1"),
    ADD_DECLINE("Level 2"),
    READ_ALL_LIST("Level 3"),
    EDIT_LIST("Level 4"),
    FULL_ACCESS("Level 5"),
}