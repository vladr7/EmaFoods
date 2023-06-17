package com.example.emafoods.feature.profile.domain.usecase

import com.example.emafoods.feature.profile.domain.models.ProfileImage
import com.example.emafoods.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class StoreProfileImageChoiceUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
        suspend fun execute(profileImage: ProfileImage) {
            profileRepository.storeProfileImageChoice(profileImage)
        }
}