package com.example.emafoods.core.data.localstorage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DefaultDataStore.DATASTORE_PREFERENCES_NAME
)

class DefaultDataStore @Inject constructor (
    private val context: Context
): LocalStorage {

    companion object {
        const val DATASTORE_PREFERENCES_NAME = "DATASTORE_PREFERENCES_EMAFOODS"
    }

    override suspend fun putString(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    override suspend fun getString(key: String, defaultValue: String?): String? {
        val prefKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[prefKey]
    }

    override suspend fun putInt(key: String, value: Int) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value.toString()
        }
    }

    override suspend fun getInt(key: String, defaultValue: Int): Int {
        val prefKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[prefKey]?.toInt() ?: defaultValue
    }

    override suspend fun putLong(key: String, value: Long) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value.toString()
        }
    }

    override suspend fun getLong(key: String, defaultValue: Long): Long {
        val prefKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[prefKey]?.toLong() ?: defaultValue
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value.toString()
        }
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        val prefKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[prefKey]?.toBoolean() ?: defaultValue
    }

    override suspend fun putFloat(key: String, value: Float) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value.toString()
        }
    }

    override suspend fun getFloat(key: String, defaultValue: Float): Float {
        val prefKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[prefKey]?.toFloat() ?: defaultValue
    }

    override suspend fun clearData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}