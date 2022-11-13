package com.alankurniadi.storyapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alankurniadi.storyapp.data.StoryRepository
import com.alankurniadi.storyapp.model.ResponseAddNewStory
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun postNewStory(
        token: String,
        storyText: RequestBody,
        imageMultipart: MultipartBody.Part
    ): LiveData<com.alankurniadi.storyapp.data.Result<ResponseAddNewStory>> {
        return storyRepository.postNewStory("Bearer $token", storyText, imageMultipart).asLiveData()
    }
}
