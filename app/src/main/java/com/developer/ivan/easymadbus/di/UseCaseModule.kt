package com.developer.ivan.easymadbus.di

import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.data.repository.BusRepository
import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.data.repository.ILocationRepository
import com.developer.ivan.easymadbus.framework.datasource.PlayServicesLocationDataSource
import com.developer.ivan.usecases.*
import dagger.Module
import dagger.Provides
@Module
class UseCaseModule
{
    @Provides
    fun deleteStopFavouriteProvider(busRepository: IBusRepository)=
        DeleteStopFavourite(busRepository)

    @Provides
    fun getBusAndStopsFavouritesProvider(busRepository: IBusRepository)=
        GetBusAndStopsFavourites(busRepository)

    @Provides
    fun getBusStopsProvider(busRepository: IBusRepository)=
        GetBusStops(busRepository)

    @Provides
    fun getBusStopTimProvidere(busRepository: IBusRepository)=
        GetBusStopTime(busRepository)

    @Provides
    fun getCoarseLocationProvider(locationDataSource: ILocationRepository)=
        GetCoarseLocation(locationDataSource)

    @Provides
    fun getFineLocationProvider(locationDataSource: ILocationRepository)=
        GetFineLocation(locationDataSource)

    @Provides
    fun getIncidentsProvider(busRepository: IBusRepository)=
        GetIncidents(busRepository)

    @Provides
    fun getStopFavouriteProvider(busRepository: IBusRepository)=
        GetStopFavourite(busRepository)

    @Provides
    fun getTokenProvider(busRepository: IBusRepository)=
        GetToken(busRepository)

    @Provides
    fun insertStopFavouriteProvider(busRepository: IBusRepository)=
        InsertStopFavourite(busRepository)
}