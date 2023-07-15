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
    fun `when time difference is more than 3 days then reset fire streaks`() = runTest {
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
    fun `when last time opened app time is less than midnight time then increase firestreaks count`() = runTest {
        // Given
        `when`(lastTimeUserOpenedAppUseCase.execute()).thenReturn(
            Date().time - 25 * 60 * 60 * 1000 // days * hours * minutes * seconds * milliseconds
        )

        // When
        updateFireStreaksUseCase.execute()

        // Then
        verify(updateConsecutiveDaysAppOpenedUseCase, times(numInvocations = 1)).execute()
        verify(updateLastTimeUserOpenedAppUseCase, times(numInvocations = 1)).execute()
    }

    @Test
    fun `when last time opened app time is more than midnight time then do only update last time opened app`() = runTest {
        // Given
        `when`(lastTimeUserOpenedAppUseCase.execute()).thenReturn(
            Date().time - 10 * 60 * 60 * 1000 // days * hours * minutes * seconds * milliseconds
        )

        // When
        updateFireStreaksUseCase.execute()

        // Then
        verify(updateConsecutiveDaysAppOpenedUseCase, times(numInvocations = 0)).execute()
        verify(resetConsecutiveDaysAppOpenedUseCase, times(numInvocations = 0)).execute()
        verify(updateLastTimeUserOpenedAppUseCase, times(numInvocations = 1)).execute()
    }

    @After
    fun tearDown() {
        MockitoAnnotations.openMocks(this).close()
    }
}
