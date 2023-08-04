package com.example.husnistoryapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.husnistoryapp.model.StoryRepository
import com.example.husnistoryapp.service.RegisterResponse
import com.example.husnistoryapp.utils.Event
import kotlinx.coroutines.launch

class RegisterViewModel(private val repo: StoryRepository) : ViewModel() {

    val registerResponse: LiveData<RegisterResponse> = repo.registerResponse
    val isLoading: LiveData<Boolean> = repo.isLoading
    val toastText: LiveData<Event<String>> = repo.toastText

    fun doRegister(name: String, email: String, password: String) {
        viewModelScope.launch {
            repo.postRegister(name, email, password)
        }
    }
}