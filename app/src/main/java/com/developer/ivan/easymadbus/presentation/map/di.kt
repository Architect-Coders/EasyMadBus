package com.developer.ivan.easymadbus.presentation.map

import android.app.Application
import com.developer.ivan.easymadbus.di.EasyMadBusComponent
import com.developer.ivan.easymadbus.framework.AndroidPermissionChecker
import com.developer.ivan.easymadbus.framework.IMapManager
import com.developer.ivan.easymadbus.framework.MapManager
import com.developer.ivan.easymadbus.framework.PermissionChecker
import com.developer.ivan.usecases.*
import com.google.android.gms.maps.MapView
import dagger.*
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named
import javax.inject.Singleton

@Module
class BusMapFragmentModule {

    @Provides
    fun busMapFragmentViewModelProvider(
        busStops: GetBusStops,
        busStopDetail: GetStopDetail,
        stopTime: GetBusStopTime,
        busAndStopsFavourites: GetBusAndStopsFavourites,
        insertStopFavourite: InsertStopFavourite,
        deleteStopFavourite: DeleteStopFavourite,
        location: GetCoarseLocation,
        fineLocation: GetFineLocation,
        permissionChecker: PermissionChecker,
        dispatcher: CoroutineDispatcher
    ): BusMapViewModel =
        BusMapViewModel(busStops,
            busStopDetail,
            stopTime,
            busAndStopsFavourites,
            insertStopFavourite,
            deleteStopFavourite,
            location,
            fineLocation,
            permissionChecker,
            dispatcher)



}
@Subcomponent(modules = [BusMapFragmentModule::class])
interface BusMapFragmentComponent {
    val busMapViewmodel: BusMapViewModel
    val mapManager: IMapManager

}
