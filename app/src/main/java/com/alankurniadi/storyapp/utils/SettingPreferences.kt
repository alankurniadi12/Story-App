package com.alankurniadi.storyapp.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>){

    private val TOKEN_KEY = stringPreferencesKey("token_key")
    private val NAME_KEY = stringPreferencesKey("name_key")

    fun getTokenKey(): Flow<String> {
        return dataStore.data.map { preference ->
            preference[TOKEN_KEY] ?: ""
        }
    }

    fun getUserName(): Flow<String> {
        return dataStore.data.map { preference ->
            preference[NAME_KEY] ?: ""
        }
    }

    suspend fun saveTokenKey(token: String) {
        dataStore.edit { preference ->
            preference[TOKEN_KEY] = token
        }
    }

    suspend fun saveUserName(name: String) {
        dataStore.edit { preference ->
            preference[NAME_KEY] = name
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
