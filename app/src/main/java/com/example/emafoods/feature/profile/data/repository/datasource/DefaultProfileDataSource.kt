package com.example.emafoods.feature.profile.data.repository.datasource

import com.example.emafoods.R
import com.example.emafoods.feature.profile.domain.models.ProfileImage
import javax.inject.Inject

class DefaultProfileDataSource @Inject constructor(): ProfileDataSource {

    override fun getProfileImages(): List<ProfileImage> {
        return profileImages
    }

    companion object {
        private val profileImages = listOf<ProfileImage>(
            ProfileImage(0, R.drawable.profilepic1),
            ProfileImage(1, R.drawable.profilepic2),
        )
    }
}