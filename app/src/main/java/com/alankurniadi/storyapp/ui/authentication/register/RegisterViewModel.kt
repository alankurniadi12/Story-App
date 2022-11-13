package com.alankurniadi.storyapp.ui.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alankurniadi.storyapp.data.Result
import com.alankurniadi.storyapp.data.StoryRepository
import com.alankurniadi.storyapp.model.ResponseRegister

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun postRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<ResponseRegister>> {
        return storyRepository.postRegister(name, email, password).asLiveData()
    }
}
