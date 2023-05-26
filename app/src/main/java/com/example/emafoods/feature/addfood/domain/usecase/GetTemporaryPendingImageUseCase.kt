package com.example.emafoods.feature.addfood.domain.usecase

import android.net.Uri
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import javax.inject.Inject

class GetTemporaryPendingImageUseCase @Inject constructor(
    private val foodRepository: FoodRepository,
    private val getUserDetailsUseCase: GetUserDetailsUseCase
) {

    suspend fun execute(): State<Uri> {
        val userDetails = getUserDetailsUseCase.execute()
        return foodRepository.getPendingFoodImageFromTemporaryStorage(authorUid = userDetails.uid)
    }
}
