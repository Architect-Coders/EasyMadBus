package com.developer.ivan.easymadbus.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.IntegerRes
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.NavigationBottomUtil
import com.developer.ivan.easymadbus.core.hide
import com.developer.ivan.easymadbus.core.show
import kotlinx.android.synthetic.main.activity_main.*
import org.intellij.lang.annotations.Identifier

class MainActivity : AppCompatActivity(), NavigationBottomUtil.IBottomNavigation {

    private lateinit var navBottomUtil: NavigationBottomUtil

    @Identifier
    private var mBottomPosition=R.id.mapFragment

    companion object
    {
        const val ARG_BOTTOM_POSITION = "argBottomPosition"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navBottomUtil = NavigationBottomUtil(this)

        setSupportActionBar(toolbar)
        setupBottomNavigationItems()


        mBottomPosition = when
        {
            savedInstanceState?.getInt(ARG_BOTTOM_POSITION)!=null -> savedInstanceState.getInt(ARG_BOTTOM_POSITION)
            else -> R.id.mapFragment
        }
        nav_view.selectedItemId = mBottomPosition
    }

    override fun onBackPressed() {
        navBottomUtil.backPresed()
    }

    private fun setupBottomNavigationItems() {

        navBottomUtil.setupBottomNav(nav_view,this)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ARG_BOTTOM_POSITION,mBottomPosition)
        super.onSaveInstanceState(outState)

    }

    override fun onDestroy() {
        super.onDestroy()
        navBottomUtil.destroy()
    }

    override fun onClickOnMap() {
        mBottomPosition = R.id.mapFragment
        frame_host_map_wrapper.show()
        frame_host_favourite_wrapper.hide()
        frame_host_notification_wrapper.hide()
    }

    override fun onClickOnFavourite() {
        mBottomPosition = R.id.favouriteFragment
        frame_host_map_wrapper.hide()
        frame_host_favourite_wrapper.show()
        frame_host_notification_wrapper.hide()
    }

    override fun onClickOnNotification() {
        mBottomPosition = R.id.notificationFragment
        frame_host_map_wrapper.hide()
        frame_host_favourite_wrapper.hide()
        frame_host_notification_wrapper.show()
    }


}
