package com.developer.ivan.easymadbus.ui.favourite

import android.widget.ProgressBar
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
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.data.db.models.DBStopFavourite
import com.developer.ivan.easymadbus.framework.ApiService
import com.developer.ivan.easymadbus.framework.UIEasyMadBusDelegate
import com.developer.ivan.easymadbus.framework.di.AndroidTestComponent
import com.developer.ivan.easymadbus.framework.network.MockServerDispatcher
import com.developer.ivan.easymadbus.framework.network.rule.MockServerTestRule
import com.developer.ivan.easymadbus.presentation.MainActivity
import com.developer.ivan.easymadbus.presentation.adapters.FavouritesAdapter
import com.developer.ivan.easymadbus.utils.matchers.atPosition
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
class UIFavouriteScreenTest {



    @get:Rule
    val mockWebServerRule = MockServerTestRule()

    lateinit var mockWebServer: MockWebServer
    private lateinit var http: OkHttp3IdlingResource



    lateinit var component: AndroidTestComponent


    @get:Rule
    var mActivityRule: ActivityScenarioRule<MainActivity> = activityScenarioRule()


    @Before
    fun onSetup() {


        mockWebServer = mockWebServerRule.server

        mockWebServer.dispatcher =
            MockServerDispatcher(ApplicationProvider.getApplicationContext()).Response()


        mockWebServer.url("${ApiService.MOBILITY_LABS_ENDPOINT}+${ApiService.USERS_ENDPOINT}+${ApiService.GET_LOGIN}")
        mockWebServer.url("${ApiService.TRANSPORT_ENDPOINT}+${ApiService.STOPS_ENDPOINT}+${ApiService.POST_STOPS}")
        mockWebServer.url("${ApiService.TRANSPORT_ENDPOINT}${ApiService.STOPS_ENDPOINT}1/${ApiService.GET_DETAIL}")



        mActivityRule.scenario.onActivity {

            component = (it.application as UIEasyMadBusDelegate).component as AndroidTestComponent

            http = OkHttp3IdlingResource.create("OkHttp", component.okHttpClient)
            IdlingRegistry.getInstance().register(http)


            component.database.stopFavourite().updateFavourite(DBStopFavourite("1", "myStop"))
        }
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

        mActivityRule.scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.rcvFavourites)).check(matches(atPosition(0, hasDescendant(withText("myStop")))))


    }


}