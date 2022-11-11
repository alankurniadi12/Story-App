package com.alankurniadi.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.alankurniadi.storyapp.model.ResponseAddNewStory
import com.alankurniadi.storyapp.model.ResponseAllStories
import com.alankurniadi.storyapp.model.ResponseLogin
import com.alankurniadi.storyapp.model.ResponseRegister
import com.alankurniadi.storyapp.network.ApiService
import com.alankurniadi.storyapp.utils.SettingPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val pref: SettingPreferences
) {

    companion object {
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            pref: SettingPreferences
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, pref)
            }.also { instance = it }
    }

    fun postRegister(
        name: String,
        email: String,
        password: String
    ): Flow<Result<ResponseRegister>> = flow {
        emit(Result.Loading)
        try {
            val responseRegister = apiService.userRegister(name, email, password)
            emit(Result.Success(responseRegister))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postLogin(email: String, password: String): Flow<Result<ResponseLogin>> = flow {
        emit(Result.Loading)
        try {
            val responseLogin = apiService.userLogin(email, password)
            emit(Result.Success(responseLogin))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getListStory(token: String): Flow<Result<ResponseAllStories>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStories("Bearer $token", 1, 10)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postNewStory(
        token: String,
        storyText: RequestBody,
        imageMultipart: MultipartBody.Part
    ): Flow<Result<ResponseAddNewStory>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.postNewStory(token, storyText, imageMultipart)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveToken(token: String) {
        pref.saveTokenKey(token)
    }

    fun getToken(): LiveData<String> {
        return pref.getTokenKey().asLiveData()
    }
}
