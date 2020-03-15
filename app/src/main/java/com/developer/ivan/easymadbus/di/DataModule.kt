package com.developer.ivan.easymadbus.di

import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.data.repository.*
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

    @Provides
    fun locationRepositoryProvider(
        locationDataSource: LocationDataSource
    ): ILocationRepository = LocationRepository(locationDataSource)
}