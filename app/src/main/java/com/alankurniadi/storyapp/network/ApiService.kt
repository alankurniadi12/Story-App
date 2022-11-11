package com.alankurniadi.storyapp.network

import com.alankurniadi.storyapp.model.ResponseAddNewStory
import com.alankurniadi.storyapp.model.ResponseAllStories
import com.alankurniadi.storyapp.model.ResponseLogin
import com.alankurniadi.storyapp.model.ResponseRegister
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend  fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseRegister

    @FormUrlEncoded
    @POST("login")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseLogin

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ResponseAllStories

    @Multipart
    @POST("stories")
    suspend fun postNewStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): ResponseAddNewStory
}
