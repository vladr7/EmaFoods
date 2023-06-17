package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.model.UserLevel
import javax.inject.Inject

class CalculateUserLevelFromXpUseCase @Inject constructor(

) {
    
    fun execute(xp: Long): UserLevel =
        when(xp) {
            in Int.MIN_VALUE until UserLevel.LEVEL_2.xp ->  UserLevel.LEVEL_1
            in UserLevel.LEVEL_2.xp until UserLevel.LEVEL_3.xp ->  UserLevel.LEVEL_2
            in UserLevel.LEVEL_3.xp until Int.MAX_VALUE ->  UserLevel.LEVEL_3
            else -> UserLevel.LEVEL_1
        }
}