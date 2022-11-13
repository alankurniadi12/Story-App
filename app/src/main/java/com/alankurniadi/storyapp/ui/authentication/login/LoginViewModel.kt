package com.alankurniadi.storyapp.ui.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alankurniadi.storyapp.data.Result
import com.alankurniadi.storyapp.data.StoryRepository
import com.alankurniadi.storyapp.model.ResponseLogin

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun postLogin(email: String, password: String): LiveData<Result<ResponseLogin>> {
        return storyRepository.postLogin(email, password).asLiveData()
    }

}
