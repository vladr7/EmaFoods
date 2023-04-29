package com.example.emafoods.core.domain.datasource

import android.net.Uri
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.data.models.FoodImage
import com.example.emafoods.core.utils.State
import kotlinx.coroutines.flow.Flow

interface FoodDataSource {

    suspend fun addFood(food: Food): State<Food>

    suspend fun addFoodImageToStorage(food: Food, fileUri: Uri): State<Food>

    fun getAllFoods(): Flow<List<Food>>

    fun getAllFoodImages(): Flow<List<FoodImage>>
}