package com.developer.ivan.easymadbus.di

import android.app.Application
import com.developer.ivan.easymadbus.presentation.favourites.FavouriteFragmentComponent
import com.developer.ivan.easymadbus.presentation.favourites.FavouriteFragmentModule
import com.developer.ivan.easymadbus.presentation.map.BusMapFragmentComponent
import com.developer.ivan.easymadbus.presentation.map.BusMapFragmentModule
import com.developer.ivan.easymadbus.presentation.notifications.NotificationsFragmentComponent
import com.developer.ivan.easymadbus.presentation.notifications.NotificationsFragmentModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, UseCaseModule::class])
interface EasyMadBusComponent {

    fun plus(module: BusMapFragmentModule): BusMapFragmentComponent
    fun plus(module: FavouriteFragmentModule): FavouriteFragmentComponent
    fun plus(module: NotificationsFragmentModule): NotificationsFragmentComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): EasyMadBusComponent
    }
}