package com.developer.ivan.easymadbus.core

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.developer.ivan.easymadbus.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationBottomUtil(activity: AppCompatActivity) {
    private val mNavHostMap by lazy { activity.supportFragmentManager.findFragmentById(R.id.nav_host_map) }
    private val mNavHostFavorite by lazy { activity.supportFragmentManager.findFragmentById(R.id.nav_host_favourite) }
    private val mNavHostNotification by lazy { activity.supportFragmentManager.findFragmentById(R.id.nav_host_notification) }

    private val mNavControllerMap by lazy { activity.findNavController(R.id.nav_host_map) }
    private val mNavControllerFavourite by lazy { activity.findNavController(R.id.nav_host_favourite) }
    private val mNavControllerNotification by lazy { activity.findNavController(R.id.nav_host_notification) }
    private var mCurrentNavHost: NavController? = null


    interface IBottomNavigation {
        fun onClickOnMap(currentFragment: Fragment?)
        fun onClickOnFavourite(currentFragment: Fragment?)
        fun onClickOnNotification(currentFragment: Fragment?)
    }

    private var mListener: IBottomNavigation? = null


    private val onBottomNavigationSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {

            with(activity)
            {
                when (it.itemId) {
                    R.id.mapFragment -> {
                        val appBarConfiguration = AppBarConfiguration(setOf(R.id.mapFragment))
                        setupActionBarWithNavController(mNavControllerMap, appBarConfiguration)
                        mCurrentNavHost = mNavControllerMap
                        mListener?.onClickOnMap(
                            mNavHostMap?.childFragmentManager?.fragments?.getOrNull(
                                0
                            )
                        )
                        true
                    }
                    R.id.favouriteFragment -> {
                        val appBarConfiguration = AppBarConfiguration(setOf(R.id.favouriteFragment))
                        setupActionBarWithNavController(
                            mNavControllerFavourite,
                            appBarConfiguration
                        )
                        mCurrentNavHost = mNavControllerFavourite
                        mListener?.onClickOnFavourite(
                            mNavHostFavorite?.childFragmentManager?.fragments?.getOrNull(
                                0
                            )
                        )
                        true
                    }
                    R.id.notificationFragment -> {
                        val appBarConfiguration =
                            AppBarConfiguration(setOf(R.id.notificationFragment))
                        setupActionBarWithNavController(
                            mNavControllerNotification,
                            appBarConfiguration
                        )
                        mCurrentNavHost = mNavControllerNotification
                        mListener?.onClickOnNotification(
                            mNavHostNotification?.childFragmentManager?.fragments?.getOrNull(
                                0
                            )
                        )
                        true
                    }
                    else -> false
                }
            }

        }

    fun backPressed() {
        mCurrentNavHost?.navigateUp()
    }

    fun setupBottomNav(navView: BottomNavigationView, listener: IBottomNavigation) {
        this.mListener = listener
        navView.setOnNavigationItemSelectedListener(onBottomNavigationSelectedListener)
    }

    fun destroy() {
        mListener = null
        mCurrentNavHost = null
    }
}