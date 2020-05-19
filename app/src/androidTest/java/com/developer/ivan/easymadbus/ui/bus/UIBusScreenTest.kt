package com.developer.ivan.easymadbus.ui.bus

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.developer.ivan.easymadbus.framework.ApiService.Factory.GET_DETAIL
import com.developer.ivan.easymadbus.framework.ApiService.Factory.GET_LOGIN
import com.developer.ivan.easymadbus.framework.ApiService.Factory.MOBILITY_LABS_ENDPOINT
import com.developer.ivan.easymadbus.framework.ApiService.Factory.POST_STOPS
import com.developer.ivan.easymadbus.framework.ApiService.Factory.STOPS_ENDPOINT
import com.developer.ivan.easymadbus.framework.ApiService.Factory.TRANSPORT_ENDPOINT
import com.developer.ivan.easymadbus.framework.ApiService.Factory.USERS_ENDPOINT
import com.developer.ivan.easymadbus.framework.UIEasyMadBusDelegate
import com.developer.ivan.easymadbus.framework.di.AndroidTestComponent
import com.developer.ivan.easymadbus.presentation.MainActivity
import com.developer.ivan.easymadbus.presentation.map.BusMapFragmentModule
import com.developer.ivan.easymadbus.utils.MockServerDispatcher
import com.developer.ivan.easymadbus.utils.rules.network.MockServerTestRule
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UIBusScreenTest {


    private lateinit var http: OkHttp3IdlingResource
    @get:Rule
    val mockWebServerRule =
        MockServerTestRule()

    private lateinit var mockWebServer: MockWebServer

    private lateinit var component: AndroidTestComponent

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
            MockServerDispatcher(
                ApplicationProvider.getApplicationContext()
            ).Response()


        mockWebServer.url("$MOBILITY_LABS_ENDPOINT+$USERS_ENDPOINT+$GET_LOGIN")
        mockWebServer.url("$TRANSPORT_ENDPOINT+$STOPS_ENDPOINT+$POST_STOPS")
        mockWebServer.url("${TRANSPORT_ENDPOINT}${STOPS_ENDPOINT}1/${GET_DETAIL}")


        mActivityRule.scenario.onActivity {

            component = (it.application as UIEasyMadBusDelegate).component as AndroidTestComponent
            http = OkHttp3IdlingResource.create("OkHttp", component.okHttpClient)
            IdlingRegistry.getInstance().register(http)
            component.database.clearAllTables()

        }


    }

    @After
    fun closeIdling() {
        IdlingRegistry.getInstance().unregister(http)
    }



    @Test
    fun uiAutomatorMarkClick() {
        val scenario = mActivityRule.scenario

        val device = UiDevice.getInstance(getInstrumentation())
        var marker: UiObject? = null

        val mapManager = component.plus(BusMapFragmentModule()).mapManager


        scenario.onActivity {
            mapManager.moveToLocation((LatLng(40.4701435453176, -3.78288324038992)))

        }

        marker =
            device.findObject(UiSelector().descriptionContains("Avenida Valdemarín-Blanca de Castilla"))

        marker?.click()

    }


}