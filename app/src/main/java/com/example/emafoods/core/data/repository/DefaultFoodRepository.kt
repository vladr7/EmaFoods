package com.example.emafoods.core.data.repository

import android.net.Uri
import com.example.emafoods.core.data.database.FoodDatabase
import com.example.emafoods.core.data.database.asDatabaseModel
import com.example.emafoods.core.data.database.asDatabasePendingModel
import com.example.emafoods.core.data.database.asDomainModel
import com.example.emafoods.core.data.database.asDomainPendingModel
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.datasource.FoodDataSource
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.repository.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultFoodRepository @Inject constructor(
    private val database: FoodDatabase,
    private val foodDataSource: FoodDataSource
) : FoodRepository {

    override val foods: Flow<List<Food>> = database.foodDao.getFoods().map { databaseList ->
        databaseList.asDomainModel()
    }
    override val pendingFoods: Flow<List<Food>> =
        database.foodDao.getPendingFoods().map { databaseList ->
            databaseList.asDomainPendingModel()
        }

    override suspend fun refreshFoods() {
        withContext(Dispatchers.IO) {
            val foods = foodDataSource.getAllFoods()
            val images = foodDataSource.getAllFoodImages()
            val mutableListOfFoods = mutableListOf<Food>()
            foods.forEach { food ->
                val image = images.find { image ->
                    food.id == image.id
                }
                val newFood = Food(
                    id = food.id,
                    author = food.author,
                    authorUid = food.authorUid,
                    description = food.description,
                    imageRef = image?.imageRef ?: "",
                    addedDateInSeconds = food.addedDateInSeconds,
                    category = food.category,
                    ingredients = food.ingredients,
                    title = food.title
                )
                if (newFood.imageRef.isNotEmpty()) {
                    mutableListOfFoods.add(newFood)
                }
            }
            if (mutableListOfFoods.isNotEmpty()) {
                database.foodDao.deleteAllFoods()
                database.foodDao.insertAll(
                    mutableListOfFoods.toList().asDatabaseModel()
                )
            }
        }
    }

    override suspend fun addFood(food: Food): State<Food> =
        foodDataSource.addFood(food = food)

    override suspend fun addFoodImageToStorage(
        food: Food,
        fileUri: Uri
    ): State<Food> =
        foodDataSource.addFoodImageToStorage(food = food, fileUri = fileUri)

    override suspend fun refreshPendingFoods() {
        withContext(Dispatchers.IO) {
            val foods = foodDataSource.getAllPendingFoods()
            val images = foodDataSource.getAllPendingFoodImages()
            val mutableListOfFoods = mutableListOf<Food>()
            foods.forEach { food ->
                val image = images.find { image ->
                    food.id == image.id
                }
                val newFood = Food(
                    id = food.id,
                    author = food.author,
                    authorUid = food.authorUid,
                    description = food.description,
                    imageRef = image?.imageRef ?: "",
                    addedDateInSeconds = food.addedDateInSeconds,
                    category = food.category,
                    ingredients = food.ingredients,
                    title = food.title
                )
                if (newFood.imageRef.isNotEmpty()) {
                    mutableListOfFoods.add(newFood)
                }
            }
            if (mutableListOfFoods.isNotEmpty()) {
                database.foodDao.insertAllPendingFoods(
                    mutableListOfFoods.toList().asDatabasePendingModel()
                )
            }
        }
    }

    override suspend fun addPendingFood(food: Food): State<Food> =
        foodDataSource.addPendingFood(food = food)


    override suspend fun addPendingFoodImageToStorage(
        food: Food,
    ): State<Food> =
        foodDataSource.addPendingFoodImageToStorage(food = food)

    override suspend fun addPendingFoodImageToTemporaryStorage(food: Food): State<Food> {
        return foodDataSource.addPendingFoodImageToTemporaryStorage(food = food)
    }

    override suspend fun getPendingFoodImageFromTemporaryStorage(authorUid: String): State<Uri> {
        return foodDataSource.getPendingFoodImageFromTemporaryStorage(authorUid = authorUid)
    }

    override suspend fun moveTemporaryImageToPending(food: Food): State<Food> {
        return foodDataSource.moveTemporaryImageToPending(food = food)
    }

    override suspend fun deletePendingFood(food: Food): State<Food> {
        database.foodDao.deletePendingFood(id = food.id)
        foodDataSource.deletePendingFood(food = food)
        return foodDataSource.deletePendingFoodImage(food = food)
    }

    override suspend fun movePendingFoodToAllFoods(food: Food): State<Food> {
        val addImageResult = foodDataSource.movePendingImageToAllImages(food = food)
        if (addImageResult is State.Failed) {
            return addImageResult
        }
        val addFoodResult = foodDataSource.addFood(food = food)
        if (addFoodResult is State.Failed) {
            return addFoodResult
        }

        database.foodDao.deletePendingFood(id = food.id)
        foodDataSource.deletePendingFood(food = food)
        return addFoodResult
    }

    override suspend fun updateFood(food: Food): State<Food> {
        return foodDataSource.updateFood(food = food)
    }

}
