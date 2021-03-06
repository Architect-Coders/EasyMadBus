package com.developer.ivan.easymadbus.di

import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.data.datasources.NetworkDataSource
import com.developer.ivan.data.datasources.RemoteDataSource
import com.developer.ivan.data.repository.BusRepository
import com.developer.ivan.data.repository.ILocationRepository
import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.data.repository.LocationRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule
{
    @Provides
    fun busRepositoryProvider(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        networkDataSource: NetworkDataSource
    ): IBusRepository = BusRepository(remoteDataSource, localDataSource,networkDataSource)

    @Provides
    fun locationRepositoryProvider(
        locationDataSource: LocationDataSource
    ): ILocationRepository = LocationRepository(locationDataSource)
}
