package com.example.emafoods.core.domain.datasource

import android.net.Uri
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.data.models.FoodImage
import com.example.emafoods.core.domain.models.State

interface FoodDataSource {

    suspend fun addFood(food: Food): State<Food>

    suspend fun addFoodImageToStorage(food: Food, fileUri: Uri): State<Food>

    suspend fun addFoodImageBytesToStorage(food: Food, bytes: ByteArray): State<Food>

    suspend fun addPendingFood(food: Food): State<Food>

    suspend fun addPendingFoodImageToStorage(food: Food): State<Food>

    suspend fun addPendingFoodImageByteArrayToStorage(food: Food, data: ByteArray): State<Food>

    suspend fun addPendingFoodImageToTemporaryStorage(food: Food): State<Food>

    suspend fun getPendingFoodImageFromTemporaryStorage(authorUid: String): State<Uri>

    suspend fun moveTemporaryImageToPending(food: Food): State<Food>

    suspend fun getAllFoods(): List<Food>

    suspend fun getAllFoodImages(): List<FoodImage>

    suspend fun getAllPendingFoods(): List<Food>

    suspend fun getAllPendingFoodImages(): List<FoodImage>

    suspend fun deletePendingFood(food: Food): State<Food>

    suspend fun deletePendingFoodImage(food: Food): State<Food>

    suspend fun movePendingImageToAllImages(food: Food): State<Food>

    suspend fun updateFood(food: Food): State<Food>
}