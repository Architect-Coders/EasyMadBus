package com.developer.ivan.easymadbus.framework.di

import android.app.Application
import com.developer.ivan.easymadbus.di.DataModule
import com.developer.ivan.easymadbus.di.EasyMadBusComponent
import com.developer.ivan.easymadbus.di.UseCaseModule
import dagger.BindsInstance
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidTestAppModule::class, UseCaseModule::class, DataModule::class])
interface AndroidTestComponent : EasyMadBusComponent {
    //    Idling resources
    val okHttpClient: OkHttpClient

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance app: Application,
            @BindsInstance @Named("endpoint") endpoint: String
        ): EasyMadBusComponent
    }
}