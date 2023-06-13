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
internal class  AddIngredientToListUseCaseTest{

    private lateinit var addIngredientToListUseCase: AddIngredientToListUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        addIngredientToListUseCase = AddIngredientToListUseCase()
    }

	@Test
    fun `add ingredient success`() = runTest {
        // Given
        val ingredients = listOf<IngredientViewData>(
            IngredientViewData(1, "name1", 1L, ),
            IngredientViewData(2, "name2", 2L, ),
            IngredientViewData(3, "name3", 3L, ),
        )

        val expected = IngredientResult.Success(
            listOf<IngredientViewData>(
                IngredientViewData(1, "name1", 1L, ),
                IngredientViewData(2, "name2", 2L, ),
                IngredientViewData(3, "name3", 3L, ),
                IngredientViewData(4, "name4", 4L, ),
            )
        )

        // When
        val tested = addIngredientToListUseCase.execute(IngredientViewData(4, "name4", 4L, ), ingredients)

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `add ingredient fail ingredient already exist in the list expect error`() = runTest {
        // Given
        val ingredients = listOf<IngredientViewData>(
            IngredientViewData(1, "name1", 1L, ),
            IngredientViewData(2, "name2", 2L, ),
            IngredientViewData(3, "name3", 3L, ),
        )

        val expected = IngredientResult.ErrorAlreadyAdded<Unit>(Unit)

        // When
        val tested = addIngredientToListUseCase.execute(IngredientViewData(2, "name2", 2L, ), ingredients)

        // Then
        assertThat(tested, `is`(expected))
    }
}
