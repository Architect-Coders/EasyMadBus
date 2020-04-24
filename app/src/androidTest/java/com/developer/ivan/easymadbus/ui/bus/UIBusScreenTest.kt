package com.developer.ivan.easymadbus.ui.bus

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.di.EasyMadBusComponent
import com.developer.ivan.easymadbus.framework.ApiService
import com.developer.ivan.easymadbus.framework.ApiService.Factory.GET_DETAIL
import com.developer.ivan.easymadbus.framework.ApiService.Factory.GET_LOGIN
import com.developer.ivan.easymadbus.framework.ApiService.Factory.MOBILITY_LABS_ENDPOINT
import com.developer.ivan.easymadbus.framework.ApiService.Factory.POST_STOPS
import com.developer.ivan.easymadbus.framework.ApiService.Factory.STOPS_ENDPOINT
import com.developer.ivan.easymadbus.framework.ApiService.Factory.TRANSPORT_ENDPOINT
import com.developer.ivan.easymadbus.framework.ApiService.Factory.USERS_ENDPOINT
import com.developer.ivan.easymadbus.framework.UIEasyMadBusDelegate
import com.developer.ivan.easymadbus.framework.di.AndroidTestComponent
import com.developer.ivan.easymadbus.framework.network.MockServerDispatcher
import com.developer.ivan.easymadbus.framework.network.rule.MockServerTestRule
import com.developer.ivan.easymadbus.presentation.MainActivity
import com.developer.ivan.easymadbus.presentation.map.BusMapFragmentModule
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.espresso.OkHttp3IdlingResource
import junit.framework.Assert.assertEquals
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.concurrent.thread


@RunWith(AndroidJUnit4::class)
class UIBusScreenTest {


    @get:Rule
    val mockWebServerRule = MockServerTestRule()

    lateinit var mockWebServer: MockWebServer

    lateinit var component: AndroidTestComponent

    @get:Rule
    var mActivityRule: ActivityScenarioRule<MainActivity> = activityScenarioRule()

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        )


    @Before
    fun onSetup() {


        mockWebServer = mockWebServerRule.server



        mockWebServer.dispatcher =
            MockServerDispatcher(ApplicationProvider.getApplicationContext()).Response()


        mockWebServer.url("$MOBILITY_LABS_ENDPOINT+$USERS_ENDPOINT+$GET_LOGIN")
        mockWebServer.url("$TRANSPORT_ENDPOINT+$STOPS_ENDPOINT+$POST_STOPS")
        mockWebServer.url("${TRANSPORT_ENDPOINT}${STOPS_ENDPOINT}1/${GET_DETAIL}")


        mActivityRule.scenario.onActivity {

            component = (it.application as UIEasyMadBusDelegate).component as AndroidTestComponent

            val resource = OkHttp3IdlingResource.create("OkHttp", component.okHttpClient)
            IdlingRegistry.getInstance().register(resource)

        }


    }

    @Test
    fun uiAutomatorMarkClick() {
        val scenario = mActivityRule.scenario

        val device = UiDevice.getInstance(getInstrumentation())
        var marker: UiObject? = null

        scenario.moveToState(Lifecycle.State.RESUMED)
        val mapManager = component.plus(BusMapFragmentModule()).mapManager


        scenario.onActivity {
            mapManager.moveToLocation((LatLng(40.4701435453176,-3.78288324038992)))

        }

        marker = device.findObject(UiSelector().descriptionContains("Avenida Valdemarín-Blanca de Castilla"))

        marker?.click()

    }


}