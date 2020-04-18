package com.developer.ivan.easymadbus.core

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.developer.ivan.easymadbus.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationBottomUtil(activity: AppCompatActivity) {
    private val mNavHostMap by lazy {
        activity.supportFragmentManager.findFragmentById(R.id.nav_host_map) as NavHostFragment }
    private val mNavHostFavorite by lazy {
        activity.supportFragmentManager.findFragmentById(R.id.nav_host_favourite) as NavHostFragment }
    private val mNavHostNotification by lazy {
        activity.supportFragmentManager.findFragmentById(R.id.nav_host_notification) as NavHostFragment }

    private val mNavControllerMap by lazy { mNavHostMap.navController }
    private val mNavControllerFavourite by lazy { mNavHostFavorite.navController }
    private val mNavControllerNotification by lazy { mNavHostNotification.navController }
    var mCurrentNavHost: NavController? = null


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
                        mCurrentNavHost = mNavControllerMap
                        val appBarConfiguration = AppBarConfiguration(setOf(R.id.mapFragment))
                        setupActionBarWithNavController(mNavControllerMap, appBarConfiguration)
                        mListener?.onClickOnMap(
                            mNavHostMap.childFragmentManager.fragments.getOrNull(
                                0
                            )
                        )
                        true
                    }
                    R.id.favouriteFragment -> {
                        mCurrentNavHost = mNavControllerFavourite
                        val appBarConfiguration = AppBarConfiguration(setOf(R.id.favouriteFragment))
                        setupActionBarWithNavController(
                            mNavControllerFavourite,
                            appBarConfiguration
                        )
                        mListener?.onClickOnFavourite(
                            mNavHostFavorite.childFragmentManager.fragments.getOrNull(
                                0
                            )
                        )
                        true
                    }
                    R.id.notificationFragment -> {
                        mCurrentNavHost = mNavControllerNotification
                        val appBarConfiguration =
                            AppBarConfiguration(setOf(R.id.notificationFragment))
                        setupActionBarWithNavController(
                            mNavControllerNotification,
                            appBarConfiguration
                        )
                        mListener?.onClickOnNotification(
                            mNavHostNotification.childFragmentManager.fragments.getOrNull(
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