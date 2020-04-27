package com.developer.ivan.easymadbus.di

import android.app.Application
import android.hardware.usb.UsbEndpoint
import com.developer.ivan.easymadbus.data.db.Database
import com.developer.ivan.easymadbus.presentation.favourites.FavouriteFragmentComponent
import com.developer.ivan.easymadbus.presentation.favourites.FavouriteFragmentModule
import com.developer.ivan.easymadbus.presentation.favourites.detail.FavouriteDetailComponent
import com.developer.ivan.easymadbus.presentation.favourites.detail.FavouriteDetailFragmentModule
import com.developer.ivan.easymadbus.presentation.favourites.detail.FavouriteDetailViewModel
import com.developer.ivan.easymadbus.presentation.map.BusMapFragmentComponent
import com.developer.ivan.easymadbus.presentation.map.BusMapFragmentModule
import com.developer.ivan.easymadbus.presentation.notifications.NotificationsFragmentComponent
import com.developer.ivan.easymadbus.presentation.notifications.NotificationsFragmentModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, UseCaseModule::class])
interface EasyMadBusComponent {

    fun plus(module: BusMapFragmentModule): BusMapFragmentComponent
    fun plus(module: FavouriteFragmentModule): FavouriteFragmentComponent
    fun plus(module: NotificationsFragmentModule): NotificationsFragmentComponent
    fun plus(module: FavouriteDetailFragmentModule): FavouriteDetailComponent

    val database: Database



    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application,
                   @BindsInstance @Named("endpoint") endpoint: String): EasyMadBusComponent
    }
}
