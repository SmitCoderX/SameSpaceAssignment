package com.smitcoderx.assignment.samespaceassignment.di

import com.smitcoderx.assignment.samespaceassignment.api.SameSpaceAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(SameSpaceAPI.BASE_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideDisneyService(retrofit: Retrofit): SameSpaceAPI {
        return retrofit.create(SameSpaceAPI::class.java)
    }
}