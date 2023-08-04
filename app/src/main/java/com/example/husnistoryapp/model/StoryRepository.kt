package com.example.husnistoryapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.husnistoryapp.ui.story.StoryPagingSource
import com.example.husnistoryapp.service.*
import com.example.husnistoryapp.utils.Event
import com.example.husnistoryapp.utils.SessionPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val pref: SessionPreferences
){
    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _uploadResponse = MutableLiveData<AddStoryResponse>()
    val uploadResponse: LiveData<AddStoryResponse> = _uploadResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    private val _list = MutableLiveData<StoriesResponse>()
    val list: LiveData<StoriesResponse> = _list

    fun postRegister(name: String, email: String, password: String) {
        _isLoading.value = true
        val client = apiService.doRegister(name, email, password)

        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _registerResponse.value = response.body()
                    _toastText.value = Event(response.body()?.message.toString())
                } else {
                    _toastText.value = Event(response.message().toString())
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _toastText.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun postLogin(email: String, password: String) {
        _isLoading.value = true
        val client = apiService.doLogin(email, password)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _loginResponse.value = response.body()
                    _toastText.value = Event(response.body()?.message.toString())
                } else {
                    _toastText.value = Event(response.message().toString())
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _toastText.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(pref, apiService)
            }
        ).liveData
    }


    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        val client = apiService.doUploadStory(token, file, description)

        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _uploadResponse.value = response.body()
                    _toastText.value = Event(response.body()?.message.toString())
                } else {
                    _toastText.value = Event(response.message().toString())
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                Log.d("error upload", t.message.toString())
            }

        }
        )
    }

    fun getListStoriesWithLocation(token: String) {
        _isLoading.value = true
        val client = apiService.getListStoriesWithLocation(token)

        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _list.value = response.body()
                } else {
                    _toastText.value = Event(response.message().toString())
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _toastText.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getSession(): LiveData<UserModel> {
        return pref.getSession().asLiveData()
    }

    suspend fun saveSession(session: UserModel) {
        pref.saveSession(session)
    }

    suspend fun login() {
        pref.login()
    }

    suspend fun logout() {
        pref.logout()
    }

    companion object {
        private const val TAG = "StoryRepository"

        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preferences: SessionPreferences,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, preferences)
            }.also { instance = it }
    }
}