package com.developer.ivan.easymadbus.presentation.map

import com.developer.ivan.easymadbus.framework.PermissionChecker
import com.developer.ivan.usecases.*
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class BusMapFragmentModule {

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

}
@Subcomponent(modules = [BusMapFragmentModule::class])
interface BusMapFragmentComponent {
    val busMapViewmodel: BusMapViewModel
}