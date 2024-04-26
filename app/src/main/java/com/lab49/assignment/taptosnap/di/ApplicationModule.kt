package com.lab49.assignment.taptosnap.di

import com.lab49.assignment.taptosnap.DebugHelper
import com.lab49.assignment.taptosnap.repository.labels.LabelsRepository
import com.lab49.assignment.taptosnap.repository.labels.LabelsRepositoryImpl
import com.lab49.assignment.taptosnap.repository.validation.ValidationRepository
import com.lab49.assignment.taptosnap.repository.validation.ValidationRepositoryImpl
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

    @Provides
    @Singleton
    fun provideLabelRepository(impl: LabelsRepositoryImpl): LabelsRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideValidationRepository(impl: ValidationRepositoryImpl) : ValidationRepository {
        return impl
    }
}