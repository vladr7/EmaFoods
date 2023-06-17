package com.example.emafoods.feature.profile.domain.usecase

import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.feature.profile.domain.models.ProfileImage
import com.example.emafoods.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetNextProfileImageUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val logHelper: LogHelper
) {
    suspend fun execute(profileImage: ProfileImage): ProfileImage {
        val profileImages = profileRepository.getProfileImages()
        return try {
            if(profileImages.isEmpty()) {
                return profileImage
            }
            if(profileImage.id < profileImages.size - 1) {
                return profileImages[profileImage.id + 1]
            }
            return profileImages[0]
        } catch (e: Exception) {
            logHelper.reportCrash(Throwable("Error getting next profile image choice", e))
            profileImage
        }
    }

}