package com.example.emafoods.feature.profile.data.repository

import com.example.emafoods.core.domain.localstorage.LocalStorage
import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.feature.profile.data.repository.datasource.ProfileDataSource
import com.example.emafoods.feature.profile.domain.models.ProfileImage
import com.example.emafoods.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class DefaultProfileRepository @Inject constructor(
    private val localStorage: LocalStorage,
    private val profileDataSource: ProfileDataSource,
) : ProfileRepository {

    override suspend fun storeProfileImageChoice(profileImage: ProfileImage) {
        localStorage.putInt(PROFILE_IMAGE_ID_KEY, profileImage.id)
    }

    override suspend fun getProfileImages(): List<ProfileImage> {
        return profileDataSource.getProfileImages()
    }

    override suspend fun getCurrentProfileImageIndex(): Int {
        return localStorage.getInt(PROFILE_IMAGE_ID_KEY, 0)
    }

    companion object {
        private const val PROFILE_IMAGE_ID_KEY = "profile_image_key"
    }
}