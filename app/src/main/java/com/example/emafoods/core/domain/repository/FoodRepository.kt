package com.example.emafoods.core.domain.repository

import android.net.Uri
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.utils.State
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    val foods: Flow<List<Food>>

    val pendingFoods: Flow<List<Food>>

    suspend fun refreshFoods()

    suspend fun addFood(food: Food): State<Food>

    suspend fun addFoodImageToStorage(food: Food, fileUri: Uri): State<Food>

    suspend fun refreshPendingFoods()

    suspend fun addPendingFood(food: Food): State<Food>

    suspend fun addPendingFoodImageToStorage(food: Food): State<Food>

    suspend fun deletePendingFood(food: Food): State<Food>

    suspend fun movePendingFoodToAllFoods(food: Food): State<Food>
}