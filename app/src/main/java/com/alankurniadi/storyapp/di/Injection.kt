package com.alankurniadi.storyapp.di

import android.content.Context
import com.alankurniadi.storyapp.data.StoryRepository
import com.alankurniadi.storyapp.dataStore
import com.alankurniadi.storyapp.network.ApiConfig
import com.alankurniadi.storyapp.utils.SettingPreferences

object Injection {

    fun provideStoryRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val pref = SettingPreferences.getInstance(context.dataStore)
        return StoryRepository.getInstance(apiService, pref)
    }
}
