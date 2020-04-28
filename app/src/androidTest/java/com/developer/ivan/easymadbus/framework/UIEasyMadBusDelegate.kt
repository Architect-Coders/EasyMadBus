package com.developer.ivan.easymadbus.framework

import com.developer.ivan.easymadbus.App
import com.developer.ivan.easymadbus.di.EasyMadBusComponent
import com.developer.ivan.easymadbus.framework.di.DaggerAndroidTestComponent

open class UIEasyMadBusDelegate : App() {
    override fun getAppComponent(url: String): EasyMadBusComponent {
        return DaggerAndroidTestComponent.factory().create(this, "http://localhost:8080/")
    }
}