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
    private val checkUserLeveledUpUseCase: CheckUserLeveledUpUseCase,
    private val storeUserLevelUseCase: StoreUserLevelUseCase,
    private val nextLevelUseCase: GetNextLevelUseCase
) {

    suspend fun execute(increaseXpActionType: IncreaseXpActionType, nrOfRewards: Int = 0): IncreaseXpResult<Int> {
        val xpToBeAdded = if(nrOfRewards != 0) nrOfRewards * increaseXpActionType.xp else increaseXpActionType.xp
        storeUnspentUserXpUseCase.execute(xpToBeAdded)
        val unspentUserXp = getUnspentUserXpUseCase.execute()
        if (checkUserLeveledUpUseCase.execute(unspentUserXp)) {
            val nextLevel = nextLevelUseCase.execute()
            storeUserXpUseCase.execute(unspentUserXp)
            storeUserLevelUseCase.execute(nextLevel)
            resetUnspentUserXpUseCase.execute()
            return IncreaseXpResult.LeveledUp(nextLevel)
        }
        if (unspentUserXp >= XP_INCREASE_THRESHOLD) {
            storeUserXpUseCase.execute(unspentUserXp)
            resetUnspentUserXpUseCase.execute()
            return IncreaseXpResult.ExceededUnspentThreshold(unspentUserXp)
        }
        return IncreaseXpResult.NotExceededUnspentThreshold(unspentUserXp)
    }
}