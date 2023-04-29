package com.example.emafoods.core.data.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao{

    @Query("select * from databasefood")
    fun getFoods(): Flow<List<DatabaseFood>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(foods: List<DatabaseFood>)
}

@Database(entities = [DatabaseFood::class], version = 1)
abstract class FoodDatabase: RoomDatabase() {
    abstract val foodDao: FoodDao
}

private lateinit var INSTANCE: FoodDatabase

fun getFoodDatabase(context: Context): FoodDatabase {
    synchronized(FoodDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                FoodDatabase::class.java,
                "food").build()
        }
    }
    return INSTANCE
}