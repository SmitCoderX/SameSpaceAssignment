package com.smitcoderx.assignment.samespaceassignment.api

import com.smitcoderx.assignment.samespaceassignment.forYou.Data
import com.smitcoderx.assignment.samespaceassignment.forYou.ForYouModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SameSpaceAPI {

    companion object {
        const val BASE_API = "https://cms.samespace.com/"
        const val IMAGE_URL = "https://cms.samespace.com/assets/"
    }

    @GET("items/songs")
    suspend fun getSongsList(): Response<ForYouModel?>

    @GET("items/songs/{id}")
    suspend fun getSongData(@Path("id") id: Int): Response<Data?>

}