package com.developer.ivan.easymadbus

import android.app.Application
import androidx.room.Room
import com.developer.ivan.easymadbus.data.db.Database

class App : Application() {

    lateinit var database: Database
        private set

    override fun onCreate() {
        database = Room.databaseBuilder(this, Database::class.java, "db").build()
        super.onCreate()
    }
}