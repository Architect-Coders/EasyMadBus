package com.developer.ivan.easymadbus.presentation.map

import android.Manifest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.developer.ivan.domain.*
import com.developer.ivan.easymadbus.framework.CoroutinesMainDispatcherRule
import com.developer.ivan.easymadbus.framework.AndroidPermissionChecker
import com.developer.ivan.easymadbus.presentation.map.BusMapViewModel.BusStopScreenState
import com.developer.ivan.easymadbus.presentation.models.*
import com.developer.ivan.testshared.*
import com.developer.ivan.usecases.*
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BusMapViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesMainDispatcherRule()

    @get:Rule
    var liveDataRule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<BusStopScreenState>

    @Mock
    lateinit var busStops: GetBusStops

    @Mock
    lateinit var accessToken: GetToken

    @Mock
    lateinit var stopTime: GetBusStopTime

    @Mock
    lateinit var busAndStopsFavourites: GetBusAndStopsFavourites

    @Mock
    lateinit var busStopDetail: GetStopDetail

    @Mock
    lateinit var insertStopFavourite: InsertStopFavourite

    @Mock
    lateinit var deleteStopFavourite: DeleteStopFavourite

    @Mock
    lateinit var coarseLocation: GetCoarseLocation

    @Mock
    lateinit var fineLocation: GetFineLocation

    @Mock
    lateinit var permissionChecker: AndroidPermissionChecker


    private lateinit var viewModel: BusMapViewModel


    @Before
    fun setUp() {
        viewModel = BusMapViewModel(
            busStops,
            busStopDetail,
            stopTime,
            busAndStopsFavourites,
            insertStopFavourite,
            deleteStopFavourite,
            coarseLocation,
            fineLocation,
            permissionChecker,
            coroutinesTestRule.testDispatcher
        )
    }


    @Test
    fun `fusedLocation requests coarse location perm if it is not granted before`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {

            val expectedLocate = Either.Right(locateMock.copy(0.1, 0.1))
            viewModel.busState.observeForever(observer)
            whenever(permissionChecker.check(Manifest.permission.ACCESS_COARSE_LOCATION)).thenReturn(
                false
            )
            viewModel.fusedLocation()

            verify(observer).onChanged(BusStopScreenState.RequestCoarseLocation)

        }

    @Test
    fun `fusedLocation observer returns the location if exists and permissions are granted`() {

        val expectedLocate = locateMock.copy(0.1, 0.1)

        whenever(permissionChecker.check(Manifest.permission.ACCESS_COARSE_LOCATION)).thenReturn(
            true
        )
        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.busState.observeForever(observer)

            whenever(coarseLocation.execute(Unit)).thenReturn(Either.Right(expectedLocate))

            viewModel.fusedLocation()
        }

//        Using refEq for equal object contents
        verify(observer).onChanged(refEq(BusStopScreenState.ShowFusedLocation(expectedLocate)))
    }

    @Test
    fun `fineLocation requests fine location perm if it is not granted before`() {

        whenever(permissionChecker.check(Manifest.permission.ACCESS_FINE_LOCATION)).thenReturn(false)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.busState.observeForever(observer)

            viewModel.fineLocation()

            verify(observer).onChanged(BusStopScreenState.RequestFineLocation)
        }

    }

    @Test
    fun `fineLocation observer returns the location if exists and permissions are granted`() {

        val expectedLocate = Either.Right(locateMock.copy(1.1, 1.1))

        whenever(permissionChecker.check(Manifest.permission.ACCESS_FINE_LOCATION)).thenReturn(true)

        coroutinesTestRule.testDispatcher.runBlockingTest {

            viewModel.busState.observeForever(observer)

            whenever(fineLocation.execute(Unit)).thenReturn(expectedLocate)

            viewModel.fineLocation()

            verify(observer).onChanged(refEq(BusStopScreenState.ShowFusedLocation(expectedLocate.b)))

        }

    }

    @Test
    fun `busStops observer returns bus stops`() {

        val expectedBusStops =
            Either.Right(busStopsMock.mapIndexed { index, item -> item.copy(node = index.toString()) })

        val uiBusStops = expectedBusStops.b.map { it.toUIBusStop() }


        coroutinesTestRule.testDispatcher.runBlockingTest {

            viewModel.busState.observeForever(observer)
            whenever(busStops.execute(any())).thenReturn(eq(expectedBusStops))

            viewModel.busStops()
            verify(observer).onChanged(refEq(BusStopScreenState.ShowBusStops(uiBusStops)))


        }


    }

    @Test
    fun `clickInMark observer returns bus stops`() {
        val stopId = "1"
        val markId = "1"

        val expectedArrives =
            Either.Right(arrivesMock.mapIndexed { index: Int, arrive: Arrive -> arrive.copy(line = index.toString()) })

        val expectedBusStopsAndFavourites =
            Either.Right(listOf(Pair(busStopsMock[0].copy("1"), stopFavouriteMock.copy("1"))))

        val expectedBusLines =
            Either.Right(
                busStopsMock[0].copy(
                    "1",
                    lines = linesMock.mapIndexed { index, item -> item.copy(line = index.toString()) })
            )


        coroutinesTestRule.testDispatcher.runBlockingTest {

            viewModel.busState.observeForever(observer)

            doReturn(expectedArrives).whenever(stopTime).execute(any())
            doReturn(expectedBusStopsAndFavourites).whenever(busAndStopsFavourites).execute(any())
            doReturn(expectedBusLines).whenever(busStopDetail).execute(any())

            viewModel.clickInMark(markId, stopId)

            verify(observer).onChanged(
                refEq(
                    BusStopScreenState.ShowBusStopInfo(
                        markId,
                        Pair(
                            expectedBusLines.b.toUIBusStop(),
                            expectedBusStopsAndFavourites.b.first().second.toUIStopFavourite()
                        ),
                        expectedArrives.b.map { it.toUIArrive() }
                    )
                )
            )
        }


    }

    @Test
    fun `clickInInfoWindow insert a new stop favourite if UIStopFavourite is null and update observer`() {

        val myFavorite = Either.Right(stopFavouriteMock.copy("1"))
        val lines = Either.Right(linesMock)
        val myMarkId = "-1"
        val busData = Pair<UIBusStop, UIStopFavourite?>(busStopsMock[0].toUIBusStop(), null)


        coroutinesTestRule.testDispatcher.runBlockingTest {

            viewModel.busState.observeForever(observer)

            doReturn(myFavorite).whenever(insertStopFavourite).execute(any())

            viewModel.clickInInfoWindow(myMarkId, busData)
            verify(observer).onChanged(
                refEq(
                    (
                            BusStopScreenState.ShowBusStopInfo(
                                myMarkId,
                                busData.copy(busData.first, myFavorite.b.toUIStopFavourite()),
                                busData.first.lines.flatMap { line -> line.arrives })
                            )
                )
            )
        }

    }

    @Test
    fun `clickInInfoWindow delete an existing favourite if UIStopFavourite is != null and update observer`() {

        val myFavorite = Either.Right(stopFavouriteMock.copy("1"))
        val myMarkId = "-1"
        val busData = Pair<UIBusStop, UIStopFavourite?>(
            busStopsMock[0].toUIBusStop(),
            stopFavouriteMock.toUIStopFavourite()
        )


        coroutinesTestRule.testDispatcher.runBlockingTest {

            viewModel.busState.observeForever(observer)

            doReturn(myFavorite).whenever(deleteStopFavourite).execute(any())

            viewModel.clickInInfoWindow(myMarkId, busData)
            verify(observer).onChanged(
                refEq(
                    (
                            BusStopScreenState.ShowBusStopInfo(
                                myMarkId,
                                busData.copy(busData.first, null),
                                busData.first.lines.flatMap { line -> line.arrives })
                            )
                )
            )
        }

    }


}