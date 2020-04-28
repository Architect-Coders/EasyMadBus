@file:Suppress("Annotator")

package com.developer.ivan.easymadbus.presentation.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.developer.ivan.domain.StopFavourite
import com.developer.ivan.easymadbus.FakeLocalDataSource
import com.developer.ivan.easymadbus.FakeRemoteDataSource
import com.developer.ivan.easymadbus.diTest.DaggerEasyMadBusTestComponent
import com.developer.ivan.easymadbus.diTest.EasyMadBusTestComponent
import com.developer.ivan.easymadbus.framework.CoroutinesMainDispatcherRule
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.developer.ivan.easymadbus.presentation.models.UIStopFavourite
import com.developer.ivan.easymadbus.presentation.models.toUIBusStop
import com.developer.ivan.easymadbus.presentation.models.toUIStopFavourite
import com.developer.ivan.testshared.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class BusMapIntegrationsTest {


    private lateinit var localDataSource: FakeLocalDataSource

    private lateinit var remoteDataSource: FakeRemoteDataSource


    @get:Rule
    var coroutinesTestRule = CoroutinesMainDispatcherRule()

    @get:Rule
    var liveDataRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<BusMapViewModel.BusStopScreenState>


    private lateinit var component: EasyMadBusTestComponent

    private lateinit var mViewModel: BusMapViewModel


    @Before
    fun onSetup() {
        component = DaggerEasyMadBusTestComponent.factory().create()
        mViewModel = component.plus(BusMapFragmentModule()).busMapViewmodel
        localDataSource = component.localDataSource as FakeLocalDataSource
        remoteDataSource = component.remoteDataSource as FakeRemoteDataSource

        initSources()
//        (remoteDataSource as FakeRemoteDataSource).busStops = busStopsMock
    }

    private fun initSources() {
        localDataSource.token = tokenMock
        localDataSource.busStops = busStopsMock
        localDataSource.lines = linesMock
        localDataSource.busStopsFavourite = listOf(stopFavouriteMock.copy("1"))

        remoteDataSource.arrives = arrivesMock
        remoteDataSource.token = tokenMock
        remoteDataSource.lines = linesMock

    }

    @Test
    fun `clickInMark update localDatabase when retrieve the lines detail`() {

        val busStopArgument = busStopsMock[0].node

        localDataSource.busStops = localDataSource.busStops.map {
            it.lines = listOf()
            it
        }
        localDataSource.lines = emptyList()

        coroutinesTestRule.testDispatcher.runBlockingTest {

            mViewModel.busState.observeForever(observer)

            mViewModel.clickInMark("0", busStopArgument)

            assertTrue(localDataSource.getBusStopWithLines(busStopArgument)?.lines?.isNotEmpty() == true)


        }

    }

    @Test
    fun `clickInInfoWindow add a new favourite in localdatasource`() {

        val newFavourite = Pair<UIBusStop, UIStopFavourite?>(
            busStopsMock[0].copy("2").toUIBusStop(), null
        )

        val expectedResult = stopFavouriteMock.copy("2", busStopsMock[0].name)

        coroutinesTestRule.testDispatcher.runBlockingTest {

            mViewModel.busState.observeForever(observer)

            mViewModel.clickInInfoWindow("-1", newFavourite)

            assertEquals(
                expectedResult,
                localDataSource.getFavourites(expectedResult.busStopId.toInt())[0]
            )
        }

    }

    @Test
    fun `clickInInfoWindow removes favourite in localdatasource`() {

        val newFavourite = Pair(
            busStopsMock[0].copy("2").toUIBusStop(),
            stopFavouriteMock.copy("2", busStopsMock[0].name).toUIStopFavourite()
        )

        coroutinesTestRule.testDispatcher.runBlockingTest {

            mViewModel.busState.observeForever(observer)

            mViewModel.clickInInfoWindow("-1", newFavourite)

            assertEquals(
                emptyList<StopFavourite>(),
                localDataSource.getFavourites(newFavourite.second.busStopId.toInt())
            )
        }

    }


}