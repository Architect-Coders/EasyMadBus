package com.developer.ivan.easymadbus.core

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.developer.ivan.easymadbus.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationBottomUtil(activity: AppCompatActivity)
{

    private val mNavHostMap by lazy { activity.findNavController(R.id.nav_host_map) }
    private val mNavHostFavourite by lazy { activity.findNavController(R.id.nav_host_favourite) }
    private val mNavHostNotification by lazy { activity.findNavController(R.id.nav_host_notification) }
    private var mCurrentNavHost : NavController?=null


    interface IBottomNavigation{
        fun onClickOnMap()
        fun onClickOnFavourite()
        fun onClickOnNotification()
    }

    private var mListener: IBottomNavigation? = null


    private val onBottomNavigationSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {

        with(activity)
        {
            when (it.itemId) {
                R.id.mapFragment -> {
                    val appBarConfiguration = AppBarConfiguration(setOf(R.id.mapFragment))
                    setupActionBarWithNavController(mNavHostMap, appBarConfiguration)
                    mListener?.onClickOnMap()
                    mCurrentNavHost = mNavHostMap
                    true
                }
                R.id.favouriteFragment -> {
                    val appBarConfiguration = AppBarConfiguration(setOf(R.id.favouriteFragment))
                    setupActionBarWithNavController(mNavHostFavourite, appBarConfiguration)
                    mListener?.onClickOnFavourite()
                    mCurrentNavHost = mNavHostFavourite
                    true
                }
                R.id.notificationFragment -> {
                    val appBarConfiguration = AppBarConfiguration(setOf(R.id.notificationFragment))
                    setupActionBarWithNavController(mNavHostNotification, appBarConfiguration)
                    mListener?.onClickOnNotification()
                    mCurrentNavHost = mNavHostNotification
                    true
                }
                else -> false
            }
        }

    }

    fun backPressed()
    {
        mCurrentNavHost?.navigateUp()
    }

    fun setupBottomNav(navView: BottomNavigationView, listener: IBottomNavigation)
    {
        this.mListener = listener
        navView.setOnNavigationItemSelectedListener(onBottomNavigationSelectedListener)
    }

    fun destroy()
    {
        mListener=null
        mCurrentNavHost=null
    }
}