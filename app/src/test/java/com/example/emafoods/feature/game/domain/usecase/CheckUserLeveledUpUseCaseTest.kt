package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.model.UserGameDetails
import com.example.emafoods.feature.game.domain.model.UserLevel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class  CheckUserLeveledUpUseCaseTest{

    private lateinit var checkUserLeveledUpUseCase: CheckUserLeveledUpUseCase

    @Mock
    private lateinit var getUserGameDetailsUseCase: GetUserGameDetailsUseCase

    @Mock
    private lateinit var nextLevelUseCase: GetNextLevelUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        checkUserLeveledUpUseCase = CheckUserLeveledUpUseCase(
            getUserGameDetailsUseCase = getUserGameDetailsUseCase,
            nextLevelUseCase = nextLevelUseCase
        )
    }

	@Test
    fun `when current user XP + unspent XP exceeds next level expect positive result`() = runTest {
        // Given
        `when`(getUserGameDetailsUseCase.execute()).thenReturn(
            UserGameDetails(
                userLevel = UserLevel.LEVEL_1, userXp = 3924

            )
        )
        `when`(nextLevelUseCase.execute()).thenReturn(UserLevel.LEVEL_2)
        val expected = true

        // When
        val tested = checkUserLeveledUpUseCase.execute(unspentXp = 1000000L)

        // Then
        assertThat(tested, `is`(expected))
        verify(getUserGameDetailsUseCase, times(numInvocations = 1)).execute()
        verify(nextLevelUseCase, times(numInvocations = 1)).execute()
    }

    @Test
    fun `when current user XP + unspent XP does not exceed next level expect negative result`() = runTest {
        // Given
        `when`(getUserGameDetailsUseCase.execute()).thenReturn(
            UserGameDetails(
                userLevel = UserLevel.LEVEL_1, userXp = 0

            )
        )
        `when`(nextLevelUseCase.execute()).thenReturn(UserLevel.LEVEL_2)
        val expected = false

        // When
        val tested = checkUserLeveledUpUseCase.execute(unspentXp = 10L)

        // Then
        assertThat(tested, `is`(expected))
        verify(getUserGameDetailsUseCase, times(numInvocations = 1)).execute()
        verify(nextLevelUseCase, times(numInvocations = 1)).execute()
    }

    @Test
    fun `when next level is the same as current level (last level already acquired) expect negative result`() = runTest {
        // Given
        `when`(getUserGameDetailsUseCase.execute()).thenReturn(
            UserGameDetails(
                userLevel = UserLevel.LEVEL_1, userXp = 0

            )
        )
        `when`(nextLevelUseCase.execute()).thenReturn(UserLevel.LEVEL_1)
        val expected = false

        // When
        val tested = checkUserLeveledUpUseCase.execute(unspentXp = 10L)

        // Then
        assertThat(tested, `is`(expected))
        verify(getUserGameDetailsUseCase, times(numInvocations = 1)).execute()
        verify(nextLevelUseCase, times(numInvocations = 1)).execute()
    }


    @After
    fun tearDown() {
        MockitoAnnotations.openMocks(this).close()
    }
}
