package com.developer.ivan.easymadbus.presentation.favourites

import com.developer.ivan.usecases.GetBusAndStopsFavourites
import com.developer.ivan.usecases.GetBusStopTime
import com.developer.ivan.usecases.GetToken
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.CoroutineDispatcher


@Module
class FavouriteFragmentModule {

    @Provides
    fun favouriteViewModelProvider(
        stopTime: GetBusStopTime,
        busAndStopsFavourites: GetBusAndStopsFavourites,
        dispatcher: CoroutineDispatcher

    ): FavouriteViewModel = FavouriteViewModel(stopTime, busAndStopsFavourites,dispatcher)
}

@Subcomponent(modules = [(FavouriteFragmentModule::class)])
interface FavouriteFragmentComponent
{
    val favouriteViewModel: FavouriteViewModel
}
