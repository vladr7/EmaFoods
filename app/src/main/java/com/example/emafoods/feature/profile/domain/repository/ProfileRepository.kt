package com.example.emafoods.feature.profile.domain.repository

import com.example.emafoods.feature.profile.domain.models.ProfileImage

interface ProfileRepository {

    suspend fun storeProfileImageChoice(profileImage: ProfileImage)

    suspend fun getProfileImages(): List<ProfileImage>

    suspend fun getCurrentProfileImageIndex(): Int
}