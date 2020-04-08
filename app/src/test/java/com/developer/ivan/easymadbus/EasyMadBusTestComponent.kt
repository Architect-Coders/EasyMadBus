package com.developer.ivan.easymadbus

import com.developer.ivan.easymadbus.di.DataModule
import com.developer.ivan.easymadbus.di.UseCaseModule
import com.developer.ivan.easymadbus.presentation.favourites.FavouriteFragmentComponent
import com.developer.ivan.easymadbus.presentation.favourites.FavouriteFragmentModule
import com.developer.ivan.easymadbus.presentation.map.BusMapFragmentComponent
import com.developer.ivan.easymadbus.presentation.map.BusMapFragmentModule
import com.developer.ivan.easymadbus.presentation.notifications.NotificationsFragmentComponent
import com.developer.ivan.easymadbus.presentation.notifications.NotificationsFragmentModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MockedAppModule::class, DataModule::class, UseCaseModule::class])
interface EasyMadBusTestComponent {

    fun plus(module: BusMapFragmentModule): BusMapFragmentComponent
    fun plus(module: FavouriteFragmentModule): FavouriteFragmentComponent
    fun plus(module: NotificationsFragmentModule): NotificationsFragmentComponent


    @Component.Factory
    interface Factory {
        fun create(): EasyMadBusTestComponent
    }
}