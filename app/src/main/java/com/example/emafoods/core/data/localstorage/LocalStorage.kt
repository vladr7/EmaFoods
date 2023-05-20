package com.example.emafoods.core.data.localstorage

interface LocalStorage {

    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String, defaultValue: String?): String?
    suspend fun putInt(key: String, value: Int)
    suspend fun getInt(key: String, defaultValue: Int): Int?
    suspend fun putLong(key: String, value: Long)
    suspend fun getLong(key: String, defaultValue: Long): Long?
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean?
    suspend fun putFloat(key: String, value: Float)
    suspend fun getFloat(key: String, defaultValue: Float): Float?
    suspend fun clearData()
}