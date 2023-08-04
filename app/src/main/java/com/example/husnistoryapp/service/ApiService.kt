package com.example.husnistoryapp.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("login")
    @FormUrlEncoded
    fun doLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("register")
    @FormUrlEncoded
    fun doRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("stories")
    suspend fun getStoryList(
        @Header("Authorization") token:String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<StoriesResponse>

    @Multipart
    @POST("stories")
    fun doUploadStory(
        @Header("Authorization") token:String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryResponse>

    @GET("stories")
    fun getListStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") loc: Int = 1
    ): Call<StoriesResponse>
}