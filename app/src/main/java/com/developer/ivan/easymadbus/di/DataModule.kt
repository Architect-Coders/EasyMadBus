package com.developer.ivan.easymadbus.di

import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.repository.BusRepository
import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.data.repository.RemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class DataModule
{
    @Provides
    fun busRepositoryProvider(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): IBusRepository = BusRepository(remoteDataSource, localDataSource)
}