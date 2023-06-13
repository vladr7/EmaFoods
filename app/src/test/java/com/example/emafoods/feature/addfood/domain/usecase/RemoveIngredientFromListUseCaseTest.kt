package com.example.emafoods.feature.addfood.domain.usecase

import com.example.emafoods.feature.addfood.domain.models.IngredientResult
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class  RemoveIngredientFromListUseCaseTest{

    private lateinit var removeIngredientFromListUseCase: RemoveIngredientFromListUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        removeIngredientFromListUseCase = RemoveIngredientFromListUseCase()
    }

	@Test
    fun `remove success`() = runTest {
        // Given
        val ingredients = listOf<IngredientViewData>(
            IngredientViewData(1, "name1", 1L, ),
            IngredientViewData(2, "name2", 2L, ),
            IngredientViewData(3, "name3", 3L, ),
        )
        val expected = IngredientResult.Success(listOf<IngredientViewData>(
            IngredientViewData(1, "name1", 1L, ),
            IngredientViewData(3, "name3", 3L, ),
        ))

        // When
        val tested = removeIngredientFromListUseCase.execute(IngredientViewData(2, "name2", 2L, ), ingredients)

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `remove fail ingredient does not exist in the list expect success with current list`() = runTest {
        // Given
        val ingredients = listOf<IngredientViewData>(
            IngredientViewData(1, "name1", 1L, ),
            IngredientViewData(2, "name2", 2L, ),
            IngredientViewData(3, "name3", 3L, ),
        )
        val expected = IngredientResult.Success(listOf<IngredientViewData>(
            IngredientViewData(1, "name1", 1L, ),
            IngredientViewData(2, "name2", 2L, ),
            IngredientViewData(3, "name3", 3L, ),
        ))

        // When
        val tested = removeIngredientFromListUseCase.execute(IngredientViewData(4, "name4", 4L, ), ingredients)

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `remove fail ingredient list is empty expect success with empty list`() = runTest {
        // Given
        val ingredients = listOf<IngredientViewData>()
        val expected = IngredientResult.Success(listOf<IngredientViewData>())

        // When
        val tested = removeIngredientFromListUseCase.execute(IngredientViewData(1, "name1", 1L, ), ingredients)

        // Then
        assertThat(tested, `is`(expected))
    }
}
