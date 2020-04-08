package com.developer.ivan.easymadbus.presentation.map

import com.developer.ivan.easymadbus.framework.AndroidPermissionChecker
import com.developer.ivan.easymadbus.framework.PermissionChecker
import com.developer.ivan.usecases.*
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.CoroutineDispatcher

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
}