package com.example.emafoods.feature.profile.data.repository.datasource

import com.example.emafoods.feature.profile.domain.models.ProfileImage

interface ProfileDataSource {

    fun getProfileImages(): List<ProfileImage>
}