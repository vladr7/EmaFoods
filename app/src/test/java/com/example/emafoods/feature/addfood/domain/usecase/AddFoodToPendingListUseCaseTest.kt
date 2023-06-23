package com.example.emafoods.feature.addfood.domain.usecase

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.repository.FoodRepository
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
internal class AddFoodToPendingListUseCaseTest {

    private lateinit var addFoodToPendingListUseCase: AddFoodToPendingListUseCase

    @Mock
    private lateinit var foodRepository: FoodRepository

    @Mock
    private lateinit var moveTempImageToPendingUseCase: MoveTempImageToPendingUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        addFoodToPendingListUseCase =
            AddFoodToPendingListUseCase(foodRepository, moveTempImageToPendingUseCase)
    }

    @Test
    fun `when add food to pending successfully and do not add to temporary expect positive result and collaborators called correctly`() =
        runTest {
            // Given
            val food = Food(
                id = "pertinax",
                description = "ullamcorper",
                addedDateInSeconds = 1133,
                author = "urna",
                authorUid = "salutatus",
                category = "referrentur",
                imageRef = "te",
                ingredients = "interesset",
                title = "sociis"

            )
            `when`(foodRepository.addPendingFoodImageToStorage(food = food)).thenReturn(
                State.Success(
                    food
                )
            )
            val expected = State.Success(food)

            // When
            val tested = addFoodToPendingListUseCase.execute(
                food = food,
                shouldAddImageFromTemporary = false
            )

            // Then
            assertThat(tested, `is`(expected))
            verify(foodRepository, times(numInvocations = 1)).addPendingFood(food)
            verify(foodRepository, times(numInvocations = 1)).addPendingFoodImageToStorage(food)

        }

    @Test
    fun `when add food to pending successfully and add to temporary expect positive result and collaborators called correctly`() =
        runTest {
            // Given
            val food = Food(
                id = "pertinax",
                description = "ullamcorper",
                addedDateInSeconds = 1133,
                author = "urna",
                authorUid = "salutatus",
                category = "referrentur",
                imageRef = "te",
                ingredients = "interesset",
                title = "sociis"

            )
            `when`(moveTempImageToPendingUseCase.execute(food = food)).thenReturn(
                State.Success(
                    food
                )
            )
            val expected = State.Success(food)

            // When
            val tested = addFoodToPendingListUseCase.execute(
                food = food,
                shouldAddImageFromTemporary = true
            )

            // Then
            assertThat(tested, `is`(expected))
            verify(foodRepository, times(numInvocations = 1)).addPendingFood(food)
            verify(foodRepository, times(numInvocations = 0)).addPendingFoodImageToStorage(food)
            verify(moveTempImageToPendingUseCase, times(numInvocations = 1)).execute(food)
        }

    @Test
    fun `when add food to pending failed and add to temporary expect negative result and collaborators called correctly`() =
        runTest {
            // Given
            val food = Food(
                id = "pertinax",
                description = "ullamcorper",
                addedDateInSeconds = 1133,
                author = "urna",
                authorUid = "salutatus",
                category = "referrentur",
                imageRef = "te",
                ingredients = "interesset",
                title = "sociis"

            )
            `when`(moveTempImageToPendingUseCase.execute(food = food)).thenReturn(
                State.Failed(message = "error")
            )
            val expected = State.Failed<String>(message = "error")


            // When
            val tested = addFoodToPendingListUseCase.execute(
                food = food,
                shouldAddImageFromTemporary = true
            )

            // Then
            assertThat(tested, `is`(expected))
            verify(foodRepository, times(numInvocations = 1)).addPendingFood(food)
            verify(foodRepository, times(numInvocations = 0)).addPendingFoodImageToStorage(food)
            verify(moveTempImageToPendingUseCase, times(numInvocations = 1)).execute(food)
        }

    @Test
    fun `when add food to pending failed and do not add to temporary expect negative result and collaborators called correctly`() =
        runTest {
            // Given
            val food = Food(
                id = "pertinax",
                description = "ullamcorper",
                addedDateInSeconds = 1133,
                author = "urna",
                authorUid = "salutatus",
                category = "referrentur",
                imageRef = "te",
                ingredients = "interesset",
                title = "sociis"

            )
            `when`(foodRepository.addPendingFoodImageToStorage(food = food)).thenReturn(
                State.Failed(message = "error")
            )
            val expected = State.Failed<String>(message = "error")


            // When
            val tested = addFoodToPendingListUseCase.execute(
                food = food,
                shouldAddImageFromTemporary = false
            )

            // Then
            assertThat(tested, `is`(expected))
            verify(foodRepository, times(numInvocations = 1)).addPendingFood(food)
            verify(foodRepository, times(numInvocations = 1)).addPendingFoodImageToStorage(food)
            verify(moveTempImageToPendingUseCase, times(numInvocations = 0)).execute(food)
        }

    @After
    fun tearDown() {
        MockitoAnnotations.openMocks(this).close()
    }
}
