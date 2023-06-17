package com.example.emafoods.feature.profile.domain.usecase

import com.example.emafoods.R
import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.feature.profile.domain.models.ProfileImage
import com.example.emafoods.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetCurrentProfileImageUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val logHelper: LogHelper
) {
    suspend fun execute(): ProfileImage {
        val profileImages = profileRepository.getProfileImages()
        val currentProfileImageIndex = profileRepository.getCurrentProfileImageIndex()
        return try {
            return profileImages[currentProfileImageIndex]
        } catch (e: Exception) {
            logHelper.reportCrash(Throwable("Error getting current profile image choice", e))
            ProfileImage(
                id = 0,
                image = R.drawable.profilepic1
            )
        }
    }
}