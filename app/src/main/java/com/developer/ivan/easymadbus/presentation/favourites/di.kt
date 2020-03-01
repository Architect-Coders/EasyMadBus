package com.developer.ivan.easymadbus.presentation.favourites

import com.developer.ivan.usecases.GetBusAndStopsFavourites
import com.developer.ivan.usecases.GetBusStopTime
import com.developer.ivan.usecases.GetToken
import dagger.Module
import dagger.Provides
import dagger.Subcomponent


@Module
class FavouriteFragmentModule {

    @Provides
    fun favouriteViewModelProvider(
        accessToken: GetToken,
        stopTime: GetBusStopTime,
        busAndStopsFavourites: GetBusAndStopsFavourites

    ): FavouriteViewModel = FavouriteViewModel(accessToken, stopTime, busAndStopsFavourites)
}

@Subcomponent(modules = [(FavouriteFragmentModule::class)])
interface FavouriteFragmentComponent
{
    val favouriteViewModel: FavouriteViewModel
}
