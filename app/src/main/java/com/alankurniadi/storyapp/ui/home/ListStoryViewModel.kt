package com.alankurniadi.storyapp.ui.home

import androidx.lifecycle.*
import com.alankurniadi.storyapp.data.Result
import com.alankurniadi.storyapp.data.StoryRepository
import com.alankurniadi.storyapp.model.ResponseAllStories
import kotlinx.coroutines.launch

class ListStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getAllStory(token: String): LiveData<Result<ResponseAllStories>> {
        return storyRepository.getListStory(token).asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            storyRepository.saveToken(token)
        }
    }

    fun getToken() = storyRepository.getToken()
}
