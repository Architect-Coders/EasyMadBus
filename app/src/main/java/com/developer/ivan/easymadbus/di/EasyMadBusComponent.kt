package com.developer.ivan.easymadbus.di

import android.app.Application
import com.developer.ivan.easymadbus.presentation.favourites.FavouriteViewModel
import com.developer.ivan.easymadbus.presentation.map.BusMapViewModel
import com.developer.ivan.easymadbus.presentation.notifications.NotificationsViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, UseCaseModule::class, ViewModelsModule::class])
interface EasyMadBusComponent {

    val busMapViewModel : BusMapViewModel
    val favouriteViewModel: FavouriteViewModel
    val notificationsViewModel: NotificationsViewModel

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): EasyMadBusComponent
    }
}