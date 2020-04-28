package com.developer.ivan.easymadbus.ui.favourite.detail

import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.data.db.models.DBStopFavourite
import com.developer.ivan.easymadbus.framework.ApiService
import com.developer.ivan.easymadbus.framework.UIEasyMadBusDelegate
import com.developer.ivan.easymadbus.framework.di.AndroidTestComponent
import com.developer.ivan.easymadbus.utils.MockServerDispatcher
import com.developer.ivan.easymadbus.utils.rules.network.MockServerTestRule
import com.developer.ivan.easymadbus.presentation.MainActivity
import com.developer.ivan.easymadbus.presentation.adapters.FavouritesAdapter
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UIFavouriteDetailScreenTest {

    @get:Rule
    val mockWebServerRule =
        MockServerTestRule()



    private lateinit var mockWebServer: MockWebServer

    private lateinit var component: AndroidTestComponent

    private lateinit var http: OkHttp3IdlingResource




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


        mockWebServer.url("${ApiService.MOBILITY_LABS_ENDPOINT}+${ApiService.USERS_ENDPOINT}+${ApiService.GET_LOGIN}")
        mockWebServer.url("${ApiService.TRANSPORT_ENDPOINT}+${ApiService.STOPS_ENDPOINT}+${ApiService.POST_STOPS}")
        mockWebServer.url("${ApiService.TRANSPORT_ENDPOINT}${ApiService.STOPS_ENDPOINT}1/${ApiService.GET_DETAIL}")


        mActivityRule.scenario.onActivity {

            component = (it.application as UIEasyMadBusDelegate).component as AndroidTestComponent

            http = OkHttp3IdlingResource.create("OkHttp", component.okHttpClient)
            IdlingRegistry.getInstance().register(http)

            component.database.stopFavourite().updateFavourite(DBStopFavourite("1", "myStop"))
        }


        onView(withId(R.id.favouriteFragment)).perform(click())


    }

    @After
    fun closeIdling() {
        IdlingRegistry.getInstance().unregister(http)
    }

    @Test
    fun clickOnRecyclerShowNewWindowWithBusDetail() {

        mActivityRule.scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.rcvFavourites)).perform(
            RecyclerViewActions.actionOnItemAtPosition<FavouritesAdapter.FavouriteViewHolder>(
                0,
                click()
            )
        )

        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar)))).check(
            matches(
                withText(
                    ApplicationProvider.getApplicationContext<UIEasyMadBusDelegate>().getString(R.string.stop_detail)
                )
            )
        )

    }


}