package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.core.presentation.common.alert.XP_INCREASE_THRESHOLD
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType
import com.example.emafoods.feature.game.presentation.model.IncreaseXpResult
import javax.inject.Inject

class IncreaseXpUseCase @Inject constructor(
    private val storeUnspentUserXpUseCase: StoreUnspentUserXpUseCase,
    private val getUnspentUserXpUseCase: GetUnspentUserXpUseCase,
    private val storeUserXpUseCase: StoreUserXpUseCase,
    private val resetUnspentUserXpUseCase: ResetUnspentUserXpUseCase,
) {

    suspend fun execute(increaseXpActionType: IncreaseXpActionType): IncreaseXpResult<Int> {
        storeUnspentUserXpUseCase.execute(increaseXpActionType.xp)
        val unspentUserXp = getUnspentUserXpUseCase.execute()
        return if(unspentUserXp >= XP_INCREASE_THRESHOLD) {
            storeUserXpUseCase.execute(unspentUserXp)
            resetUnspentUserXpUseCase.execute()
            IncreaseXpResult.ExceededThreshold(unspentUserXp)
        } else {
            IncreaseXpResult.NotExceededThreshold(unspentUserXp)
        }
    }
}