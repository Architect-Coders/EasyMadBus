package com.developer.ivan.easymadbus.presentation.favourites

import com.developer.ivan.usecases.DeleteStopFavourite
import com.developer.ivan.usecases.GetBusAndStopsFavourites
import com.developer.ivan.usecases.GetBusStopTime
import com.developer.ivan.usecases.GetToken
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


@Module
class FavouriteFragmentModule {

    @Provides
    fun favouriteViewModelProvider(
        stopTime: GetBusStopTime,
        busAndStopsFavourites: GetBusAndStopsFavourites,
        deleteStopFavourite: DeleteStopFavourite,
        dispatcher: CoroutineDispatcher

    ): FavouriteViewModel = FavouriteViewModel(stopTime, busAndStopsFavourites,deleteStopFavourite,dispatcher)

}

@Subcomponent(modules = [(FavouriteFragmentModule::class)])
interface FavouriteFragmentComponent
{
    val favouriteViewModel: FavouriteViewModel
}