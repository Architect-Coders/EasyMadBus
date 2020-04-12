package com.developer.ivan.easymadbus.di

import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.data.repository.RemoteDataSource
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
interface EasyMadBusTestComponent
{
    fun plus(module: BusMapFragmentModule): BusMapFragmentComponent
    fun plus(module: FavouriteFragmentModule): FavouriteFragmentComponent
    fun plus(module: NotificationsFragmentModule): NotificationsFragmentComponent

    val localDataSource: LocalDataSource
    val remoteDataSource: RemoteDataSource
    val locationDataSource: LocationDataSource

    @Component.Factory
    interface Factory {
        fun create(): EasyMadBusTestComponent
    }
}