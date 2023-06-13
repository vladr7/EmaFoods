package com.example.emafoods.feature.addfood.domain.usecase

import com.example.emafoods.fake.FakeLogHelper
import com.example.emafoods.feature.addfood.domain.models.Ingredient
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
internal class SerializeIngredientsUseCaseTest {

    private lateinit var serializeIngredientsUseCase: SerializeIngredientsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        serializeIngredientsUseCase = SerializeIngredientsUseCase(
            logHelper = FakeLogHelper()
        )
    }

	@Test
    fun `serialize success`() = runTest {
        // Given
        val ingredientsList = listOf<Ingredient>(
            Ingredient(
                id = 1,
                name = "name",
                measurement = 1
            ),
            Ingredient(
                id = 2,
                name = "name",
                measurement = 1
            )
        )

        val expected = "[{\"id\":1,\"name\":\"name\",\"measurement\":1},{\"id\":2,\"name\":\"name\",\"measurement\":1}]"

        // When
        val tested = serializeIngredientsUseCase.execute(ingredientsList)

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `serialize empty list`() = runTest {
        // Given
        val ingredientsList = listOf<Ingredient>()
        val expected = "[]"

        // When
        val tested = serializeIngredientsUseCase.execute(ingredientsList)

        // Then
        assertThat(tested, `is`(expected))
    }
}
