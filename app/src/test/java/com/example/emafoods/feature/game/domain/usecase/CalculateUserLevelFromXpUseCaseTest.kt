package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.model.UserLevel
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class CalculateUserLevelFromXpUseCaseTest {

    private lateinit var calculateUserLevelFromXpUseCase: CalculateUserLevelFromXpUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        calculateUserLevelFromXpUseCase = CalculateUserLevelFromXpUseCase()
    }

    @Test
    fun `execute returns LEVEL_1 when xp is less than LEVEL_2`() {
        // Given
        val expected = UserLevel.LEVEL_1
        val xp = (UserLevel.LEVEL_2.xp - 1).toLong()

        // When
        val tested = calculateUserLevelFromXpUseCase.execute(xp)

        // Then
        assertEquals(tested, expected)
    }

    @Test
    fun `execute returns LEVEL_2 when xp is less than LEVEL_3`() {
        // Given
        val expected = UserLevel.LEVEL_2
        val xp = (UserLevel.LEVEL_3.xp - 1).toLong()

        // When
        val tested = calculateUserLevelFromXpUseCase.execute(xp)

        // Then
        assertEquals(tested, expected)
    }

    @Test
    fun `execute returns LEVEL_3 when xp is higher than level 3`() {
        // Given
        val expected = UserLevel.LEVEL_3
        val xp = (UserLevel.LEVEL_3.xp + 1000).toLong()

        // When
        val tested = calculateUserLevelFromXpUseCase.execute(xp)

        // Then
        assertEquals(tested, expected)
    }

    @After
    fun tearDown() {
        MockitoAnnotations.openMocks(this).close()
    }
}
