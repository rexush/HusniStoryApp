package com.example.husnistoryapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.husnistoryapp.model.StoryRepository
import com.example.husnistoryapp.model.UserModel
import com.example.husnistoryapp.service.ListStoryItem
import com.example.husnistoryapp.service.StoriesResponse
import com.example.husnistoryapp.utils.Event
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: StoryRepository) : ViewModel() {

    val isLoading: LiveData<Boolean> = repo.isLoading
    val toastText: LiveData<Event<String>> = repo.toastText
    val getListStories: LiveData<PagingData<ListStoryItem>> =
        repo.getStories().cachedIn(viewModelScope)
    val list: LiveData<StoriesResponse> = repo.list

    fun getListStoriesWithLocation(token: String) {
        viewModelScope.launch {
            repo.getListStoriesWithLocation(token)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repo.getSession()
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }
}