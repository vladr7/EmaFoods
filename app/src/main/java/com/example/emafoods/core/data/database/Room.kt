package com.example.emafoods.core.data.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao{

    @Query("select * from databasefood")
    fun getFoods(): Flow<List<DatabaseFood>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(foods: List<DatabaseFood>)

    @Query("select * from databasependingfood")
    fun getPendingFoods(): Flow<List<DatabasePendingFood>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPendingFoods(foods: List<DatabasePendingFood>)

    @Query("delete from databasependingfood where id = :id")
    fun deletePendingFood(id: String)

    @Query("delete from databasefood")
    fun deleteAllFoods()
}

@Database(entities = [DatabaseFood::class, DatabasePendingFood::class], version = 1)
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