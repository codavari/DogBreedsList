package com.example.dogbreedslist.di

import com.example.dogbreedslist.data.repository.DataRepository
import com.example.dogbreedslist.data.local.LocalData
import com.example.dogbreedslist.data.network.RemoteData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideDataRepository(remoteDataSource: RemoteData, localDataSource: LocalData) =
        DataRepository(remoteDataSource, localDataSource)

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}