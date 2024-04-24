package com.lab49.assignment.taptosnap.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    fun provideGsonConverterFactory() = GsonConverterFactory.create()
}