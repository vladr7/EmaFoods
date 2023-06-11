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
internal class DeserializeIngredientsUseCaseTest {

    private lateinit var deserializeIngredientsUseCase: DeserializeIngredientsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        deserializeIngredientsUseCase = DeserializeIngredientsUseCase(
            logHelper = FakeLogHelper()
        )
    }

	@Test
    fun `deserialize success`() = runTest {
        // Given
        val ingredientsStringList = "[{\"id\":1,\"name\":\"name\",\"measurement\":1},{\"id\":2,\"name\":\"name\",\"measurement\":1}]"
        val expected = listOf<Ingredient>(
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

        // When
        val tested = deserializeIngredientsUseCase.execute(ingredientsStringList)

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `deserialize empty string`() = runTest {
        // Given
        val ingredientsStringList = ""
        val expected = listOf<Ingredient>()

        // When
        val tested = deserializeIngredientsUseCase.execute(ingredientsStringList)

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `deserialize invalid string`() = runTest {
        // Given
        val ingredientsStringList = "invalid"
        val expected = listOf<Ingredient>()

        // When
        val tested = deserializeIngredientsUseCase.execute(ingredientsStringList)

        // Then
        assertThat(tested, `is`(expected))
    }
}
