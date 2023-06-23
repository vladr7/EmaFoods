package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType
import com.example.emafoods.feature.game.presentation.model.IncreaseXpResult
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class  IncreaseXpUseCaseTest{

    private lateinit var increaseXpUseCase: IncreaseXpUseCase

    @Mock
    private lateinit var storeUnspentUserXpUseCase: StoreUnspentUserXpUseCase

    @Mock
    private lateinit var getUnspentUserXpUseCase: GetUnspentUserXpUseCase

    @Mock
    private lateinit var storeUserXpUseCase: StoreUserXpUseCase

    @Mock
    private lateinit var resetUnspentUserXpUseCase: ResetUnspentUserXpUseCase

    @Mock
    private lateinit var checkUserLeveledUpUseCase: CheckUserLeveledUpUseCase

    @Mock
    private lateinit var storeUserLevelUseCase: StoreUserLevelUseCase

    @Mock
    private lateinit var nextLevelUseCase: GetNextLevelUseCase


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        increaseXpUseCase = IncreaseXpUseCase(
            storeUnspentUserXpUseCase,
            getUnspentUserXpUseCase,
            storeUserXpUseCase,
            resetUnspentUserXpUseCase,
            checkUserLeveledUpUseCase,
            storeUserLevelUseCase,
            nextLevelUseCase
        )
    }

    @Test
    fun `when unspent xp higher than next level then trigger next level event`() = runTest {
        // Given
        val unspentXp = 30000L
        `when`(getUnspentUserXpUseCase.execute()).thenReturn(unspentXp)
        `when`(checkUserLeveledUpUseCase.execute(unspentXp)).thenReturn(true)
        `when`(nextLevelUseCase.execute()).thenReturn(UserLevel.LEVEL_2)
        val expected = IncreaseXpResult.LeveledUp<UserLevel>(
            levelAcquired = UserLevel.LEVEL_2
        )

        // When
        val tested = increaseXpUseCase.execute(
            increaseXpActionType = IncreaseXpActionType.RECIPE_ACCEPTED,
            nrOfRewards = 0
        )

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `when unspent xp lower than next level and exceeded xp threshold then trigger ExceededUnspent event`() = runTest {
        // Given
        val unspentXp = 300L
        `when`(getUnspentUserXpUseCase.execute()).thenReturn(unspentXp)
        `when`(checkUserLeveledUpUseCase.execute(unspentXp)).thenReturn(false)
        val expected = IncreaseXpResult.ExceededUnspentThreshold<Long>(unspentXp)

        // When
        val tested = increaseXpUseCase.execute(
            increaseXpActionType = IncreaseXpActionType.RECIPE_ACCEPTED,
            nrOfRewards = 0
        )

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `when unspent xp lower than next level and not exceeded xp threshold then trigger Increased event`() = runTest {
        // Given
        val unspentXp = 5L
        `when`(getUnspentUserXpUseCase.execute()).thenReturn(unspentXp)
        `when`(checkUserLeveledUpUseCase.execute(unspentXp)).thenReturn(false)
        val expected = IncreaseXpResult.NotExceededUnspentThreshold<Long>(unspentXp)

        // When
        val tested = increaseXpUseCase.execute(
            increaseXpActionType = IncreaseXpActionType.RECIPE_ACCEPTED,
            nrOfRewards = 0
        )

        // Then
        assertThat(tested, `is`(expected))
    }

    @After
    fun tearDown() {
        MockitoAnnotations.openMocks(this).close()
    }
}