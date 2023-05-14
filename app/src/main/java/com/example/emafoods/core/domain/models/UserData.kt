package com.example.emafoods.core.domain.models

data class UserData (
    val email : String,
    val displayName : String,
    val userType : UserType = UserType.BASIC
)