package com.lab49.assignment.taptosnap.di

import com.lab49.assignment.taptosnap.repository.labels.LabelsRepository
import com.lab49.assignment.taptosnap.repository.labels.LabelsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun provideLabelRepository(impl: LabelsRepositoryImpl): LabelsRepository {
        return impl
    }

}
