package com.developer.ivan.easymadbus.di.integrations

import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.data.datasources.NetworkDataSource
import com.developer.ivan.data.datasources.RemoteDataSource
import com.developer.ivan.easymadbus.di.DataModule
import com.developer.ivan.easymadbus.di.UseCaseModule
import com.developer.ivan.easymadbus.presentation.favourites.FavouriteFragmentComponent
import com.developer.ivan.easymadbus.presentation.favourites.FavouriteFragmentModule
import com.developer.ivan.easymadbus.presentation.favourites.detail.FavouriteDetailComponent
import com.developer.ivan.easymadbus.presentation.favourites.detail.FavouriteDetailFragmentModule
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
    fun plus(module: FavouriteDetailFragmentModule): FavouriteDetailComponent

    val localDataSource: LocalDataSource
    val remoteDataSource: RemoteDataSource
    val networkDataSource: NetworkDataSource
    val locationDataSource: LocationDataSource

    @Component.Factory
    interface Factory {
        fun create(): EasyMadBusTestComponent
    }
}