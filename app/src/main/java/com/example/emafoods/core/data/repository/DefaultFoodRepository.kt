package com.example.emafoods.core.data.repository

import android.net.Uri
import com.example.emafoods.core.data.database.FoodDatabase
import com.example.emafoods.core.data.database.asDatabaseModel
import com.example.emafoods.core.data.database.asDatabasePendingModel
import com.example.emafoods.core.data.database.asDomainModel
import com.example.emafoods.core.data.database.asDomainPendingModel
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.datasource.FoodDataSource
import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.domain.models.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
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
            foodDataSource.getAllFoods()
                .combine(foodDataSource.getAllFoodImages()) { foods, images ->
                    val mutableListOfFoods = mutableListOf<Food>()
                    foods.forEach { food ->
                        val image = images.find { image ->
                            food.id == image.id
                        }
                        val newFood = Food(
                            id = food.id,
                            author = food.author,
                            description = food.description,
                            imageRef = image?.imageRef ?: "",
                            addedDateInSeconds = food.addedDateInSeconds,
                        )
                        if (newFood.imageRef.isNotEmpty()) {
                            mutableListOfFoods.add(newFood)
                        }
                    }
                    if (mutableListOfFoods.isNotEmpty()) {
                        database.foodDao.insertAll(
                            mutableListOfFoods.toList().asDatabaseModel()
                        )
                    }
                }.collect()
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
            foodDataSource.getAllPendingFoods()
                .combine(foodDataSource.getAllPendingFoodImages()) { foods, images ->
                    val mutableListOfFoods = mutableListOf<Food>()
                    foods.forEach { food ->
                        val image = images.find { image ->
                            food.id == image.id
                        }
                        val newFood = Food(
                            id = food.id,
                            author = food.author,
                            description = food.description,
                            imageRef = image?.imageRef ?: "",
                            addedDateInSeconds = food.addedDateInSeconds,
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
                }.collect()
        }
    }

    override suspend fun addPendingFood(food: Food): State<Food> =
        foodDataSource.addPendingFood(food = food)


    override suspend fun addPendingFoodImageToStorage(
        food: Food,
    ): State<Food> =
        foodDataSource.addPendingFoodImageToStorage(food = food)

    override suspend fun deletePendingFood(food: Food): State<Food> {
        database.foodDao.deletePendingFood(id = food.id)
        foodDataSource.deletePendingFood(food = food)
        return foodDataSource.deletePendingFoodImage(food = food)
    }

    override suspend fun movePendingFoodToAllFoods(food: Food): State<Food> {
        foodDataSource.addFood(food = food)
        foodDataSource.deletePendingFood(food = food)
        database.foodDao.deletePendingFood(id = food.id)
        return foodDataSource.movePendingImageToAllImages(food = food)
    }

}
