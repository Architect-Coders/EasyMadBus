package com.developer.ivan.easymadbus.ui.favourite

import android.widget.ProgressBar
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.developer.ivan.domain.BusStop
import com.developer.ivan.domain.Geometry
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.data.db.models.DBBusStop
import com.developer.ivan.easymadbus.data.db.models.DBGeometry
import com.developer.ivan.easymadbus.data.db.models.DBStopFavourite
import com.developer.ivan.easymadbus.framework.ApiService
import com.developer.ivan.easymadbus.framework.UIEasyMadBusDelegate
import com.developer.ivan.easymadbus.framework.di.AndroidTestComponent
import com.developer.ivan.easymadbus.presentation.MainActivity
import com.developer.ivan.easymadbus.utils.MockServerDispatcher
import com.developer.ivan.easymadbus.utils.fromJson
import com.developer.ivan.easymadbus.utils.matchers.atPosition
import com.developer.ivan.easymadbus.utils.rules.network.MockServerTestRule
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UIFavouriteScreenTest {


    @get:Rule
    val mockWebServerRule =
        MockServerTestRule()

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        )

    private lateinit var mockWebServer: MockWebServer
    private lateinit var http: OkHttp3IdlingResource


    private lateinit var component: AndroidTestComponent


    @get:Rule
    var mActivityRule: ActivityScenarioRule<MainActivity> = activityScenarioRule()


    @Before
    fun onSetup() {


        mockWebServer = mockWebServerRule.server

        /*mockWebServer.dispatcher =
            MockServerDispatcher(
                ApplicationProvider.getApplicationContext()
            ).Response()*/






        mActivityRule.scenario.onActivity {

            component = (it.application as UIEasyMadBusDelegate).component as AndroidTestComponent

            http = OkHttp3IdlingResource.create("OkHttp", component.okHttpClient)
            IdlingRegistry.getInstance().register(http)

            mockWebServer.enqueue(
                MockResponse().fromJson(
                    ApplicationProvider.getApplicationContext(),
                    "login.json"
                ).apply {
                    setResponseCode(200)
                }
            )

            mockWebServer.enqueue(
                MockResponse().fromJson(
                    ApplicationProvider.getApplicationContext(),
                    "bus_list.json"
                ).apply {
                    setResponseCode(200)
                }
            )
            mockWebServer.enqueue(
                MockResponse().fromJson(
                    ApplicationProvider.getApplicationContext(),
                    "bus_detail.json"
                ).apply {
                    setResponseCode(200)
                }
            )


            component.database.busStopDao().insertBusStops(listOf(DBBusStop("1", DBGeometry("Point",
                -3.78288324038992,
                    40.4701435453176),"Avenida Valdemarín-Blanca de Castilla","0")
            ))

            component.database.stopFavourite().updateFavourite(DBStopFavourite("1", "myStop"))


        }
        mActivityRule.scenario.moveToState(Lifecycle.State.RESUMED)

    }

    @After
    fun closeIdling() {
        IdlingRegistry.getInstance().unregister(http)
    }

    @Test
    fun windowLoaderIsNotShow() {

        onView(withId(R.id.favouriteFragment)).perform(click())

        onView(
            allOf(
                instanceOf(ProgressBar::class.java),
                isDescendantOfA(withId(R.id.swipeRefresh))
            )
        ).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun recyclerMatchTheFavouriteElement() {

        onView(withId(R.id.favouriteFragment)).perform(click())


        onView(withId(R.id.rcvFavourites)).check(
            matches(
                atPosition(
                    0,
                    hasDescendant(withText("myStop"))
                )
            )
        )


    }


    @Test
    fun swipeFavouriteAndAcceptDialogRemoveIt() {

        onView(withId(R.id.favouriteFragment)).perform(click())


        onView(withId(R.id.rcvFavourites)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.swipeRight()
            )
        )

        onView(
            withText(
                ApplicationProvider.getApplicationContext<UIEasyMadBusDelegate>().getString(
                    R.string.yes
                )
            )
        ).perform(click())

        onView(withId(R.id.rcvFavourites)).check(matches(not(isDisplayed())))

    }

    @Test
    fun swipeFavouriteAndCancelDialogDisplayIt() {

        onView(withId(R.id.favouriteFragment)).perform(click())


        onView(withId(R.id.rcvFavourites)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.swipeRight()
            )
        )

        onView(
            withText(
                ApplicationProvider.getApplicationContext<UIEasyMadBusDelegate>().getString(
                    R.string.no
                )
            )
        ).perform(click())

        onView(withId(R.id.rcvFavourites)).check(matches(hasDescendant(withText("myStop"))))

    }


}