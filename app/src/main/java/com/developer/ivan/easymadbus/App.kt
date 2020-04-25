package com.developer.ivan.easymadbus

import android.app.Application
import androidx.room.Room
import com.developer.ivan.domain.Constants
import com.developer.ivan.easymadbus.data.db.Database
import com.developer.ivan.easymadbus.di.DaggerEasyMadBusComponent
import com.developer.ivan.easymadbus.di.EasyMadBusComponent

open class App : Application() {

    lateinit var database: Database
        private set

    lateinit var component : EasyMadBusComponent
        private set

    override fun onCreate() {
        component = getAppComponent(Constants.EMTApi.ENDPOINT)

        database = component.database

        super.onCreate()
    }

    open fun getAppComponent(url: String): EasyMadBusComponent =
        DaggerEasyMadBusComponent
            .factory()
            .create(this, url)
}