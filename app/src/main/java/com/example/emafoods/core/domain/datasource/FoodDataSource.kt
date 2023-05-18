package com.example.emafoods.core.domain.datasource

import android.net.Uri
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.data.models.FoodImage
import com.example.emafoods.core.domain.models.State
import kotlinx.coroutines.flow.Flow

interface FoodDataSource {

    suspend fun addFood(food: Food): State<Food>

    suspend fun addFoodImageToStorage(food: Food, fileUri: Uri): State<Food>

    suspend fun addFoodImageBytesToStorage(food: Food, bytes: ByteArray): State<Food>

    suspend fun addPendingFood(food: Food): State<Food>

    suspend fun addPendingFoodImageToStorage(food: Food): State<Food>

    fun getAllFoods(): Flow<List<Food>>

    fun getAllFoodImages(): Flow<List<FoodImage>>

    fun getAllPendingFoods(): Flow<List<Food>>

    fun getAllPendingFoodImages(): Flow<List<FoodImage>>

    suspend fun deletePendingFood(food: Food): State<Food>

    suspend fun deletePendingFoodImage(food: Food): State<Food>

    suspend fun movePendingImageToAllImages(food: Food): State<Food>
}