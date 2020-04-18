package com.developer.ivan.easymadbus.presentation.favourites.detail

import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.developer.ivan.easymadbus.presentation.models.UIStopFavourite
import com.developer.ivan.usecases.GetBusStopTime
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
class FavouriteDetailFragmentModule(val data: Pair<UIBusStop,UIStopFavourite>){


    @Provides
    fun favouriteDetailViewModelProvider(getBusStopTime: GetBusStopTime, dispatcher: CoroutineDispatcher) =
        FavouriteDetailViewModel(data,getBusStopTime,dispatcher)
}

@Subcomponent(modules = [FavouriteDetailFragmentModule::class])
interface FavouriteDetailComponent{
    val favouriteDetailViewModel: FavouriteDetailViewModel
}