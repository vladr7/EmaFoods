
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import com.example.emafoods.feature.generatefood.domain.usecase.GenerateFoodUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class  GenerateFoodUseCaseTest{

    private lateinit var generateFoodUseCase: GenerateFoodUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        generateFoodUseCase = GenerateFoodUseCase()
    }

	@Test
    fun `when generate food and index is lower than list size expect the next element`() = runTest {
        // Given
        val foodsList = listOf<FoodViewData>(
            FoodViewData(
                id = "molestiae1",
                author = "sit",
                authorUid = "hac",
                description = "tamquam",
                imageRef = "diam",
                categoryType = CategoryType.MAIN_DISH,
                ingredients = listOf(),
                title = "definitionem"
            ),
            FoodViewData(
                id = "molestiae2",
                author = "sit",
                authorUid = "hac",
                description = "tamquam",
                imageRef = "diam",
                categoryType = CategoryType.SOUP,
                ingredients = listOf(),
                title = "definitionem"
            ),
            FoodViewData(
                id = "molestiae3",
                author = "sit",
                authorUid = "hac",
                description = "tamquam",
                imageRef = "diam",
                categoryType = CategoryType.SOUP,
                ingredients = listOf(),
                title = "definitionem"
            )
        )
        val expected =     FoodViewData(
            id = "molestiae2",
            author = "sit",
            authorUid = "hac",
            description = "tamquam",
            imageRef = "diam",
            categoryType = CategoryType.SOUP,
            ingredients = listOf(),
            title = "definitionem"
        )

        // When
        val tested = generateFoodUseCase.execute(foodsList, index = 0)

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `when generate food and index is equal to list size expect the first element`() = runTest {
        // Given
        val foodsList = listOf<FoodViewData>(
            FoodViewData(
                id = "molestiae1",
                author = "sit",
                authorUid = "hac",
                description = "tamquam",
                imageRef = "diam",
                categoryType = CategoryType.MAIN_DISH,
                ingredients = listOf(),
                title = "definitionem"
            ),
            FoodViewData(
                id = "molestiae2",
                author = "sit",
                authorUid = "hac",
                description = "tamquam",
                imageRef = "diam",
                categoryType = CategoryType.SOUP,
                ingredients = listOf(),
                title = "definitionem"
            ),
            FoodViewData(
                id = "molestiae3",
                author = "sit",
                authorUid = "hac",
                description = "tamquam",
                imageRef = "diam",
                categoryType = CategoryType.SOUP,
                ingredients = listOf(),
                title = "definitionem"
            )
        )
        val expected =     FoodViewData(
            id = "molestiae1",
            author = "sit",
            authorUid = "hac",
            description = "tamquam",
            imageRef = "diam",
            categoryType = CategoryType.MAIN_DISH,
            ingredients = listOf(),
            title = "definitionem"
        )

        // When
        val tested = generateFoodUseCase.execute(foodsList, index = 2)

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `when generate food and index is greater than list size expect the first element`() = runTest {
        // Given
        val foodsList = listOf<FoodViewData>(
            FoodViewData(
                id = "molestiae1",
                author = "sit",
                authorUid = "hac",
                description = "tamquam",
                imageRef = "diam",
                categoryType = CategoryType.MAIN_DISH,
                ingredients = listOf(),
                title = "definitionem"
            ),
            FoodViewData(
                id = "molestiae2",
                author = "sit",
                authorUid = "hac",
                description = "tamquam",
                imageRef = "diam",
                categoryType = CategoryType.SOUP,
                ingredients = listOf(),
                title = "definitionem"
            ),
            FoodViewData(
                id = "molestiae3",
                author = "sit",
                authorUid = "hac",
                description = "tamquam",
                imageRef = "diam",
                categoryType = CategoryType.SOUP,
                ingredients = listOf(),
                title = "definitionem"
            )
        )
        val expected =     FoodViewData(
            id = "molestiae1",
            author = "sit",
            authorUid = "hac",
            description = "tamquam",
            imageRef = "diam",
            categoryType = CategoryType.MAIN_DISH,
            ingredients = listOf(),
            title = "definitionem"
        )

        // When
        val tested = generateFoodUseCase.execute(foodsList, index = 3)

        // Then
        assertThat(tested, `is`(expected))
    }

    @After
    fun tearDown() {
        MockitoAnnotations.openMocks(this).close()
    }
}
