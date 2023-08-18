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
        if(currentTime - lastTimeUserOpenedApp > 14 * DateConstants.DAY) {
            resetConsecutiveDaysAppOpenedUseCase.execute()
            updateLastTimeUserOpenedAppUseCase.execute()
            return
        }
        val midnightTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
        }.timeInMillis
        if(lastTimeUserOpenedApp < midnightTime) {
            updateConsecutiveDaysAppOpenedUseCase.execute()
        }
        updateLastTimeUserOpenedAppUseCase.execute()
    }
}

