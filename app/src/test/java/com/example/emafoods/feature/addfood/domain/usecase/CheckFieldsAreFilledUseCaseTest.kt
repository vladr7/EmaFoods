package com.example.emafoods.feature.addfood.domain.usecase

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.test.core.app.ApplicationProvider
import com.example.emafoods.R
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
internal class CheckFieldsAreFilledUseCaseTest {

    private lateinit var checkFieldsAreFilledUseCase: CheckFieldsAreFilledUseCase

    private val context = ApplicationProvider.getApplicationContext<Context>()


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        checkFieldsAreFilledUseCase = CheckFieldsAreFilledUseCase(context)
    }

    @Test
    fun `when uri is empty expect add image error`() = runTest {
        // Given
        val expected = State.Failed<String>(context.getString(R.string.please_add_image))

        // When
        val tested = checkFieldsAreFilledUseCase.execute(
            foodDescription = "nunc",
            title = "varius",
            fileUri = Uri.EMPTY,
            ingredients = listOf()
        )

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `when description is less than 10 chars expect description error`() = runTest {
        // Given
        val expected = State.Failed<String>(context.getString(R.string.please_add_description))

        // When
        val tested = checkFieldsAreFilledUseCase.execute(
            foodDescription = "nunc",
            title = "varius",
            fileUri = "uri".toUri(),
            ingredients = listOf()
        )

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `when title is less than 5 chars expect title error`() = runTest {
        // Given
        val expected = State.Failed<String>(context.getString(R.string.please_add_title))

        // When
        val tested = checkFieldsAreFilledUseCase.execute(
            foodDescription = "nuncsadfasdfasfd",
            title = "vus",
            fileUri = "fasdf".toUri(),
            ingredients = listOf()
        )

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `when ingredients list is empty expect ingredients error`() = runTest {
        // Given
        val expected = State.Failed<String>(context.getString(R.string.please_add_ingredient))

        // When
        val tested = checkFieldsAreFilledUseCase.execute(
            foodDescription = "nuncsadfasdfasfd",
            title = "vusasdfsd",
            fileUri = "fasdf".toUri(),
            ingredients = listOf()
        )

        // Then
        assertThat(tested, `is`(expected))
    }

    @Test
    fun `when everything matches expect success`() = runTest {
        // Given
        val expected = State.success(context.getString(R.string.all_fields_are_filled))

        // When
        val tested = checkFieldsAreFilledUseCase.execute(
            foodDescription = "nuncsadfasdfasfd",
            title = "vusasdfsd",
            fileUri = "fasdf".toUri(),
            ingredients = listOf(
                IngredientViewData(
                    id = 5882, name = "Mamie Phelps", measurement = 1428, isFocused = false

                )
            )
        )

        // Then
        assertThat(tested, `is`(expected))
    }


    @After
    fun tearDown() {
        MockitoAnnotations.openMocks(this).close()
    }
}
