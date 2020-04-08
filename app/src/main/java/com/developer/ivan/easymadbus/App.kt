package com.developer.ivan.easymadbus

import android.app.Application
import androidx.room.Room
import com.developer.ivan.easymadbus.data.db.Database
import com.developer.ivan.easymadbus.di.DaggerEasyMadBusComponent
import com.developer.ivan.easymadbus.di.EasyMadBusComponent

class App : Application() {

    lateinit var database: Database
        private set

    lateinit var component : EasyMadBusComponent
        private set

    override fun onCreate() {
        component = DaggerEasyMadBusComponent
            .factory()
            .create(this)

        database = Room.databaseBuilder(this, Database::class.java, "db").build()

        super.onCreate()
    }
}