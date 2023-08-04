package com.example.husnistoryapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.husnistoryapp.model.StoryRepository
import com.example.husnistoryapp.model.UserModel
import com.example.husnistoryapp.service.LoginResponse
import com.example.husnistoryapp.utils.Event
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: StoryRepository) : ViewModel() {

    val loginResponse: LiveData<LoginResponse> = repo.loginResponse
    val isLoading: LiveData<Boolean> = repo.isLoading
    val toastText: LiveData<Event<String>> = repo.toastText

    fun doLogin(email: String, password: String) {
        viewModelScope.launch {
            repo.postLogin(email, password)
        }
    }

    fun saveSession(session: UserModel) {
        viewModelScope.launch {
            repo.saveSession(session)
        }
    }

    fun login() {
        viewModelScope.launch {
            repo.login()
        }
    }
}