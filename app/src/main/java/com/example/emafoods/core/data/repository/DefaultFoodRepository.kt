package com.example.emafoods.core.data.repository

import android.net.Uri
import com.example.emafoods.core.data.database.FoodDatabase
import com.example.emafoods.core.data.database.asDatabaseModel
import com.example.emafoods.core.data.database.asDomainModel
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.datasource.FoodDataSource
import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.utils.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultFoodRepository @Inject constructor(
    private val database: FoodDatabase,
    private val foodDataSource: FoodDataSource
) : FoodRepository {

    override val foods: Flow<List<Food>> = database.foodDao.getFoods().map { databaseList ->
        databaseList.asDomainModel()
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
                            title = food.title,
                            description = food.description,
                            imageRef = image?.imageRef ?: ""
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

    override suspend fun addFoodImageToStorage(food: Food, fileUri: Uri): State<Food> =
        foodDataSource.addFoodImageToStorage(food = food, fileUri = fileUri)
}
