package com.example.emafoods.feature.pending.presentation

import androidx.lifecycle.ViewModel
import com.example.emafoods.core.presentation.models.FoodViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PendingFoodViewModel @Inject constructor(

) : ViewModel() {

    val dummyFoodsList = listOf<FoodViewData>(
        FoodViewData(
            author = "Author 1",
            description = "Description 1",
            imageRef = "https://picsum.photos/200/300",
        ),
        FoodViewData(
            author = "Author 2",
            description = "Description 2",
            imageRef = "https://picsum.photos/200/300",
        ),
        FoodViewData(
            author = "Author 3",
            description = "Description 3",
            imageRef = "https://picsum.photos/200/300",
        ),
        FoodViewData(
            author = "Author 4",
            description = "Description 4",
            imageRef = "https://picsum.photos/200/300",
        ),
        FoodViewData(
            author = "Author 5",
            description = "Description 5",
            imageRef = "https://picsum.photos/200/300",
        ),
        FoodViewData(
            author = "Author 6",
            description = "Description 6",
            imageRef = "https://picsum.photos/200/300",
        ),
        FoodViewData(
            author = "Author 7",
            description = "Description 7",
            imageRef = "https://picsum.photos/200/300",
        ),
        FoodViewData(
            author = "Author 8",
            description = "Description 8",
            imageRef = "https://picsum.photos/200/300",
        ),
        FoodViewData(
            author = "Author 9",
            description = "Description 9",
            imageRef = "https://picsum.photos/200/300",
        ),
        FoodViewData(
            author = "Author 10",
            description = "Description 10",
            imageRef = "https://picsum.photos/200/300",
        ),
    )

    private val _state: MutableStateFlow<PendingFoodState> = MutableStateFlow(PendingFoodState())
    val state get() = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                food = dummyFoodsList.random(),
            )
        }
    }
}

data class PendingFoodState(
    val isLoading: Boolean = false,
    val error: String = "",
    val food: FoodViewData = FoodViewData(),
)