package com.developer.ivan.easymadbus.di

import com.developer.ivan.easymadbus.framework.PermissionChecker
import com.developer.ivan.easymadbus.presentation.favourites.FavouriteViewModel
import com.developer.ivan.easymadbus.presentation.map.BusMapViewModel
import com.developer.ivan.easymadbus.presentation.notifications.NotificationsViewModel
import com.developer.ivan.usecases.*
import dagger.Module
import dagger.Provides

@Module
class ViewModelsModule
{
    @Provides
    fun busMapFragmentViewModelProvider(
        busStops: GetBusStops,
        accessToken: GetToken,
        stopTime: GetBusStopTime,
        busAndStopsFavourites: GetBusAndStopsFavourites,
        insertStopFavourite: InsertStopFavourite,
        deleteStopFavourite: DeleteStopFavourite,
        location: GetCoarseLocation,
        fineLocation: GetFineLocation,
        permissionChecker: PermissionChecker
    ): BusMapViewModel =
        BusMapViewModel(busStops,
            accessToken,
            stopTime,
            busAndStopsFavourites,
            insertStopFavourite,
            deleteStopFavourite,
            location,
            fineLocation,
            permissionChecker)
    @Provides
    fun favouriteViewModelProvider(
        accessToken: GetToken,
        stopTime: GetBusStopTime,
        busAndStopsFavourites: GetBusAndStopsFavourites

    ): FavouriteViewModel = FavouriteViewModel(accessToken, stopTime, busAndStopsFavourites)

    @Provides
    fun notificationsViewModelProvider(
        accessToken: GetToken,
        incidents: GetIncidents

    ): NotificationsViewModel = NotificationsViewModel(accessToken,incidents)

}