package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.core.domain.constants.DateConstants
import java.util.Calendar
import javax.inject.Inject

class UpdateFireStreaksUseCase @Inject constructor(
    private val lastTimeUserOpenedAppUseCase: LastTimeUserOpenedAppUseCase,
    private val updateLastTimeUserOpenedAppUseCase: UpdateLastTimeUserOpenedAppUseCase,
    private val updateConsecutiveDaysAppOpenedUseCase: UpdateConsecutiveDaysAppOpenedUseCase,
    private val resetConsecutiveDaysAppOpenedUseCase: ResetConsecutiveDaysAppOpenedUseCase
) {

    suspend fun execute() {
        val lastTimeUserOpenedApp = lastTimeUserOpenedAppUseCase.execute()
        val currentTime = Calendar.getInstance().timeInMillis
        if(currentTime - lastTimeUserOpenedApp > 2 * DateConstants.DAY) {
            resetConsecutiveDaysAppOpenedUseCase.execute()
            updateLastTimeUserOpenedAppUseCase.execute()
            return
        }
        if (currentTime - lastTimeUserOpenedApp >= DateConstants.DAY) {
            updateConsecutiveDaysAppOpenedUseCase.execute()
        }
        updateLastTimeUserOpenedAppUseCase.execute()
    }
}