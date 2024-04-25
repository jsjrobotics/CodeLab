package com.lab49.assignment.taptosnap.di

import com.lab49.assignment.taptosnap.DebugHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    fun provideGsonConverterFactory() = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideDebugHelper() = DebugHelper()

    @Singleton
    @Provides
    fun provideFragmentFactory(debugHelper: DebugHelper) = TapToSnapFragmentFactory(debugHelper)
}