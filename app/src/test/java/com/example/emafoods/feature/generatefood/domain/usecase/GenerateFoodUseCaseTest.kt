package com.example.emafoods.feature.generatefood.domain.usecase

import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import com.example.emafoods.feature.generatefood.domain.models.GenerateResult
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
internal class GenerateFoodUseCaseTest {

    private lateinit var generateFoodUseCase: GenerateFoodUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        generateFoodUseCase = GenerateFoodUseCase()
    }

    @Test
    fun `when generating MainDish type and index is not equal to MainDish list size expect success result`() =
        runTest {
            // Given
            val expected = GenerateResult.Success<FoodViewData>(
                FoodViewData(
                    id = "ne",
                    author = "aliquip",
                    authorUid = "erat",
                    description = "habitasse",
                    imageRef = "velit",
                    categoryType = CategoryType.MAIN_DISH,
                    ingredients = listOf(
                        IngredientViewData(
                            id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                        )
                    )
                )
            )

            // When
            val tested = generateFoodUseCase.execute(
                categoryType = CategoryType.MAIN_DISH,
                mainDishList = listOf(
                    FoodViewData(
                        id = "ne",
                        author = "aliquip",
                        authorUid = "erat",
                        description = "habitasse",
                        imageRef = "velit",
                        categoryType = CategoryType.MAIN_DISH,
                        ingredients = listOf(
                            IngredientViewData(
                                id = 99, name = "Tim Bates", measurement = 4658, isFocused = false
                            )
                        )
                    ),
                    FoodViewData(
                        id = "ne",
                        author = "aliquip",
                        authorUid = "erat",
                        description = "habitasse",
                        imageRef = "velit",
                        categoryType = CategoryType.MAIN_DISH,
                        ingredients = listOf(
                            IngredientViewData(
                                id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                            )
                        )
                    )
                ),
                dessertList = listOf(),
                breakfastList = listOf(),
                soupList = listOf(),
                indexMainDish = 0,
                indexDessert = 7309,
                indexBreakfast = 1746,
                indexSoup = 2952

            )

            // Then
            assertThat(tested, `is`(expected))
        }

    @Test
    fun `when generating MainDish type and index is equal to MainDish list size expect success and index must be reset result`() =
        runTest {
            // Given
            val expected = GenerateResult.SuccessAndIndexMustBeReset<FoodViewData>(
                FoodViewData(
                    id = "ne",
                    author = "aliquip",
                    authorUid = "erat",
                    description = "habitasse",
                    imageRef = "velit",
                    categoryType = CategoryType.MAIN_DISH,
                    ingredients = listOf(
                        IngredientViewData(
                            id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                        )
                    )
                )
            )

            // When
            val tested = generateFoodUseCase.execute(
                categoryType = CategoryType.MAIN_DISH,
                mainDishList = listOf(
                    FoodViewData(
                        id = "ne",
                        author = "aliquip",
                        authorUid = "erat",
                        description = "habitasse",
                        imageRef = "velit",
                        categoryType = CategoryType.MAIN_DISH,
                        ingredients = listOf(
                            IngredientViewData(
                                id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                            )
                        )
                    ),
                ),
                dessertList = listOf(),
                breakfastList = listOf(),
                soupList = listOf(),
                indexMainDish = 0,
                indexDessert = 7309,
                indexBreakfast = 1746,
                indexSoup = 2952

            )

            // Then
            assertThat(tested, `is`(expected))
        }

    @Test
    fun `when generating Dessert type and index is not equal to Dessert list size expect success result`() =
        runTest {
            // Given
            val expected = GenerateResult.Success<FoodViewData>(
                FoodViewData(
                    id = "ne",
                    author = "aliquip",
                    authorUid = "erat",
                    description = "habitasse",
                    imageRef = "velit",
                    categoryType = CategoryType.DESSERT,
                    ingredients = listOf(
                        IngredientViewData(
                            id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                        )
                    )
                )
            )

            // When
            val tested = generateFoodUseCase.execute(
                categoryType = CategoryType.DESSERT,
                mainDishList = listOf(),
                dessertList = listOf(
                    FoodViewData(
                        id = "ne",
                        author = "aliquip",
                        authorUid = "erat",
                        description = "habitasse",
                        imageRef = "velit",
                        categoryType = CategoryType.DESSERT,
                        ingredients = listOf(
                            IngredientViewData(
                                id = 99, name = "Tim Bates", measurement = 4658, isFocused = false
                            )
                        )
                    ),
                    FoodViewData(
                        id = "ne",
                        author = "aliquip",
                        authorUid = "erat",
                        description = "habitasse",
                        imageRef = "velit",
                        categoryType = CategoryType.DESSERT,
                        ingredients = listOf(
                            IngredientViewData(
                                id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                            )
                        )
                    )
                ),
                breakfastList = listOf(),
                soupList = listOf(),
                indexMainDish = 0,
                indexDessert = 0,
                indexBreakfast = 1746,
                indexSoup = 2952

            )

            // Then
            assertThat(tested, `is`(expected))
        }

    @Test
    fun `when generating Dessert type and index is equal to Dessert list size expect success and index must be reset result`() =
        runTest {
            // Given
            val expected = GenerateResult.SuccessAndIndexMustBeReset<FoodViewData>(
                FoodViewData(
                    id = "ne",
                    author = "aliquip",
                    authorUid = "erat",
                    description = "habitasse",
                    imageRef = "velit",
                    categoryType = CategoryType.DESSERT,
                    ingredients = listOf(
                        IngredientViewData(
                            id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                        )
                    )
                )
            )

            // When
            val tested = generateFoodUseCase.execute(
                categoryType = CategoryType.DESSERT,
                mainDishList = listOf(),
                dessertList = listOf(
                    FoodViewData(
                        id = "ne",
                        author = "aliquip",
                        authorUid = "erat",
                        description = "habitasse",
                        imageRef = "velit",
                        categoryType = CategoryType.DESSERT,
                        ingredients = listOf(
                            IngredientViewData(
                                id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                            )
                        )
                    ),
                ),
                breakfastList = listOf(),
                soupList = listOf(),
                indexMainDish = 0,
                indexDessert = 0,
                indexBreakfast = 1746,
                indexSoup = 2952

            )

            // Then
            assertThat(tested, `is`(expected))
        }

    @Test
    fun `when generating Breakfast type and index is not equal to Breakfast list size expect success result`() =
        runTest {
            // Given
            val expected = GenerateResult.Success<FoodViewData>(
                FoodViewData(
                    id = "ne",
                    author = "aliquip",
                    authorUid = "erat",
                    description = "habitasse",
                    imageRef = "velit",
                    categoryType = CategoryType.BREAKFAST,
                    ingredients = listOf(
                        IngredientViewData(
                            id = 99, name = "Tim Bates", measurement = 4658, isFocused = false
                        )
                    )
                )
            )

            // When
            val tested = generateFoodUseCase.execute(
                categoryType = CategoryType.BREAKFAST,
                mainDishList = listOf(),
                dessertList = listOf(),
                breakfastList = listOf(
                    FoodViewData(
                        id = "ne",
                        author = "aliquip",
                        authorUid = "erat",
                        description = "habitasse",
                        imageRef = "velit",
                        categoryType = CategoryType.BREAKFAST,
                        ingredients = listOf(
                            IngredientViewData(
                                id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                            )
                        )
                    ),
                    FoodViewData(
                        id = "ne",
                        author = "aliquip",
                        authorUid = "erat",
                        description = "habitasse",
                        imageRef = "velit",
                        categoryType = CategoryType.BREAKFAST,
                        ingredients = listOf(
                            IngredientViewData(
                                id = 99, name = "Tim Bates", measurement = 4658, isFocused = false
                            )
                        )
                    )
                ),
                soupList = listOf(),
                indexMainDish = 0,
                indexDessert = 0,
                indexBreakfast = 0,
                indexSoup = 2952

            )

            // Then
            assertThat(tested, `is`(expected))
        }

    @Test
    fun `when generating Breakfast type and index is equal to Breakfast list size expect success and index must be reset result`() =
        runTest {
            // Given
            val expected = GenerateResult.SuccessAndIndexMustBeReset<FoodViewData>(
                FoodViewData(
                    id = "ne",
                    author = "aliquip",
                    authorUid = "erat",
                    description = "habitasse",
                    imageRef = "velit",
                    categoryType = CategoryType.BREAKFAST,
                    ingredients = listOf(
                        IngredientViewData(
                            id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                        )
                    )
                )
            )

            // When
            val tested = generateFoodUseCase.execute(
                categoryType = CategoryType.BREAKFAST,
                mainDishList = listOf(),
                dessertList = listOf(),
                breakfastList = listOf(
                    FoodViewData(
                        id = "ne",
                        author = "aliquip",
                        authorUid = "erat",
                        description = "habitasse",
                        imageRef = "velit",
                        categoryType = CategoryType.BREAKFAST,
                        ingredients = listOf(
                            IngredientViewData(
                                id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                            )
                        )
                    ),
                ),
                soupList = listOf(),
                indexMainDish = 0,
                indexDessert = 0,
                indexBreakfast = 0,
                indexSoup = 2952

            )

            // Then
            assertThat(tested, `is`(expected))
        }

    @Test
    fun `when generating Soup type and index is not equal to Soup list size expect success result`() =
        runTest {
            // Given
            val expected = GenerateResult.Success<FoodViewData>(
                FoodViewData(
                    id = "ne",
                    author = "aliquip",
                    authorUid = "erat",
                    description = "habitasse",
                    imageRef = "velit",
                    categoryType = CategoryType.SOUP,
                    ingredients = listOf(
                        IngredientViewData(
                            id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                        )
                    )
                )
            )

            // When
            val tested = generateFoodUseCase.execute(
                categoryType = CategoryType.SOUP,
                mainDishList = listOf(),
                dessertList = listOf(),
                breakfastList = listOf(),
                soupList = listOf(
                    FoodViewData(
                        id = "ne",
                        author = "aliquip",
                        authorUid = "erat",
                        description = "habitasse",
                        imageRef = "velit",
                        categoryType = CategoryType.SOUP,
                        ingredients = listOf(
                            IngredientViewData(
                                id = 99, name = "Tim Bates", measurement = 4658, isFocused = false
                            )
                        )
                    ),
                    FoodViewData(
                        id = "ne",
                        author = "aliquip",
                        authorUid = "erat",
                        description = "habitasse",
                        imageRef = "velit",
                        categoryType = CategoryType.SOUP,
                        ingredients = listOf(
                            IngredientViewData(
                                id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                            )
                        )
                    )
                ),
                indexMainDish = 0,
                indexDessert = 0,
                indexBreakfast = 0,
                indexSoup = 0

            )

            // Then
            assertThat(tested, `is`(expected))
        }

    @Test
    fun `when generating Soup type and index is equal to Soup list size expect success and index must be reset result`() =
        runTest {
            // Given
            val expected = GenerateResult.SuccessAndIndexMustBeReset<FoodViewData>(
                FoodViewData(
                    id = "ne",
                    author = "aliquip",
                    authorUid = "erat",
                    description = "habitasse",
                    imageRef = "velit",
                    categoryType = CategoryType.SOUP,
                    ingredients = listOf(
                        IngredientViewData(
                            id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                        )
                    )
                )
            )

            // When
            val tested = generateFoodUseCase.execute(
                categoryType = CategoryType.SOUP,
                mainDishList = listOf(),
                dessertList = listOf(),
                breakfastList = listOf(),
                soupList = listOf(
                    FoodViewData(
                        id = "ne",
                        author = "aliquip",
                        authorUid = "erat",
                        description = "habitasse",
                        imageRef = "velit",
                        categoryType = CategoryType.SOUP,
                        ingredients = listOf(
                            IngredientViewData(
                                id = 8495, name = "Tim Bates", measurement = 4658, isFocused = false
                            )
                        )
                    ),
                ),
                indexMainDish = 0,
                indexDessert = 0,
                indexBreakfast = 0,
                indexSoup = 0

            )

            // Then
            assertThat(tested, `is`(expected))
        }

    @Test
    fun `when generating soup type and soup list is empty expect error result`() =
        runTest {
            // Given
            val expected = GenerateResult.ErrorEmptyList<Unit>(Unit)

            // When
            val tested = generateFoodUseCase.execute(
                categoryType = CategoryType.SOUP,
                mainDishList = listOf(),
                dessertList = listOf(),
                breakfastList = listOf(),
                soupList = listOf(),
                indexMainDish = 0,
                indexDessert = 0,
                indexBreakfast = 0,
                indexSoup = 0

            )

            // Then
            assertThat(tested, `is`(expected))
        }

    @Test
    fun `when generating breakfast type and breakfast list is empty expect error result`() =
        runTest {
            // Given
            val expected = GenerateResult.ErrorEmptyList<Unit>(Unit)

            // When
            val tested = generateFoodUseCase.execute(
                categoryType = CategoryType.BREAKFAST,
                mainDishList = listOf(),
                dessertList = listOf(),
                breakfastList = listOf(),
                soupList = listOf(),
                indexMainDish = 0,
                indexDessert = 0,
                indexBreakfast = 0,
                indexSoup = 0

            )

            // Then
            assertThat(tested, `is`(expected))
        }

    @Test
    fun `when generating dessert type and dessert list is empty expect error result`() =
        runTest {
            // Given
            val expected = GenerateResult.ErrorEmptyList<Unit>(Unit)

            // When
            val tested = generateFoodUseCase.execute(
                categoryType = CategoryType.DESSERT,
                mainDishList = listOf(),
                dessertList = listOf(),
                breakfastList = listOf(),
                soupList = listOf(),
                indexMainDish = 0,
                indexDessert = 0,
                indexBreakfast = 0,
                indexSoup = 0

            )

            // Then
            assertThat(tested, `is`(expected))
        }

    @Test
    fun `when generating main dish type and main dish list is empty expect error result`() =
        runTest {
            // Given
            val expected = GenerateResult.ErrorEmptyList<Unit>(Unit)

            // When
            val tested = generateFoodUseCase.execute(
                categoryType = CategoryType.MAIN_DISH,
                mainDishList = listOf(),
                dessertList = listOf(),
                breakfastList = listOf(),
                soupList = listOf(),
                indexMainDish = 0,
                indexDessert = 0,
                indexBreakfast = 0,
                indexSoup = 0

            )

            // Then
            assertThat(tested, `is`(expected))
        }
}
