package com.example.emafoods.feature.game.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class  UpdateFireStreaksUseCaseTest {

    private lateinit var updateFireStreaksUseCase: UpdateFireStreaksUseCase

    @Mock
    private lateinit var lastTimeUserOpenedAppUseCase: LastTimeUserOpenedAppUseCase

    @Mock
    private lateinit var updateLastTimeUserOpenedAppUseCase: UpdateLastTimeUserOpenedAppUseCase

    @Mock
    private lateinit var updateConsecutiveDaysAppOpenedUseCase: UpdateConsecutiveDaysAppOpenedUseCase

    @Mock
    private lateinit var resetConsecutiveDaysAppOpenedUseCase: ResetConsecutiveDaysAppOpenedUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        updateFireStreaksUseCase = UpdateFireStreaksUseCase(
            lastTimeUserOpenedAppUseCase,
            updateLastTimeUserOpenedAppUseCase,
            updateConsecutiveDaysAppOpenedUseCase,
            resetConsecutiveDaysAppOpenedUseCase
        )
    }

	@Test
    fun `when time difference is more than 2 days then reset fire streaks`() = runTest {
        // Given
        `when`(lastTimeUserOpenedAppUseCase.execute()).thenReturn(
            Date().time - 3 * 24 * 60 * 60 * 1000 // days * hours * minutes * seconds * milliseconds
        )

        // When
        updateFireStreaksUseCase.execute()

        // Then
        verify(resetConsecutiveDaysAppOpenedUseCase, times(numInvocations = 1)).execute()
        verify(updateLastTimeUserOpenedAppUseCase, times(numInvocations = 1)).execute()
    }

    @Test
    fun `when time difference is less than 2 days and more than 1 day then update fire streaks`() = runTest {
        // Given
        val lastTimeUserOpenedApp = Date().time - 1 * 30 * 60 * 60 * 1000
        `when`(lastTimeUserOpenedAppUseCase.execute()).thenReturn(
            lastTimeUserOpenedApp
        )

        // When
        updateFireStreaksUseCase.execute()

        // Then
        verify(updateConsecutiveDaysAppOpenedUseCase, times(numInvocations = 1)).execute()
        verify(updateLastTimeUserOpenedAppUseCase, times(numInvocations = 1)).execute()
    }

    @Test
    fun `when time difference is less than 1 day then do not update fire streaks`() = runTest {
        // Given
        `when`(lastTimeUserOpenedAppUseCase.execute()).thenReturn(
            Date().time - 1 * 20 * 60 * 60 * 1000
        )

        // When
        updateFireStreaksUseCase.execute()

        // Then
        verify(updateConsecutiveDaysAppOpenedUseCase, times(numInvocations = 0)).execute()
        verify(updateLastTimeUserOpenedAppUseCase, times(numInvocations = 1)).execute()
    }

    @After
    fun tearDown() {
        MockitoAnnotations.openMocks(this).close()
    }
}
