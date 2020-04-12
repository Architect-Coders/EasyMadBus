package com.developer.ivan.easymadbus.presentation.favourites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.developer.ivan.easymadbus.FakeLocalDataSource
import com.developer.ivan.easymadbus.FakeRemoteDataSource
import com.developer.ivan.easymadbus.di.DaggerEasyMadBusTestComponent
import com.developer.ivan.easymadbus.di.EasyMadBusTestComponent
import com.developer.ivan.easymadbus.framework.CoroutinesMainDispatcherRule
import com.developer.ivan.easymadbus.presentation.models.toUIBusStop
import com.developer.ivan.easymadbus.presentation.models.toUILine
import com.developer.ivan.easymadbus.presentation.models.toUIStopFavourite
import com.developer.ivan.testshared.*
import com.nhaarman.mockitokotlin2.refEq
import com.nhaarman.mockitokotlin2.verify
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavouriteIntegrationsTest {


    private lateinit var localDataSource: FakeLocalDataSource

    private lateinit var remoteDataSource: FakeRemoteDataSource


    @get:Rule
    var coroutinesTestRule = CoroutinesMainDispatcherRule()

    @get:Rule
    var liveDataRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<FavouriteViewModel.FavouriteScreenState>


    private lateinit var component: EasyMadBusTestComponent

    private lateinit var mViewModel: FavouriteViewModel

    @Before
    fun onSetup() {
        component = DaggerEasyMadBusTestComponent
            .create()

        mViewModel = component.plus(FavouriteFragmentModule()).favouriteViewModel
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
    fun `obtainInfo retrieve the favourites from database`() {

        val busExpected = busStopsMock[0].toUIBusStop()
        busExpected.lines = linesMock.map { line ->
            line.copy(arrives = arrivesMock.filter { arrive -> arrive.line == line.line })
                .toUILine()
        }


        val expectedResult =
            listOf(Pair(busExpected, stopFavouriteMock.toUIStopFavourite()))


        coroutinesTestRule.testDispatcher.runBlockingTest {
            mViewModel.favouriteState.observeForever(observer)

            mViewModel.obtainInfo()

            verify(observer).onChanged(
                refEq(
                    FavouriteViewModel.FavouriteScreenState.ShowBusStopFavouriteInfo(expectedResult)
                )
            )
        }

    }


    @Test
    fun `obtainInfo retrieve stops times from remote`() {

        val busExpected = busStopsMock[0].toUIBusStop()
        busExpected.lines = linesMock.map { line ->
            line.copy(arrives = arrivesMock.filter { arrive -> arrive.line == line.line })
                .toUILine()
        }

        val expectedResult =
            Pair(busExpected, stopFavouriteMock.toUIStopFavourite())


        coroutinesTestRule.testDispatcher.runBlockingTest {
            mViewModel.favouriteState.observeForever(observer)

            mViewModel.obtainInfo()

            verify(observer).onChanged(
                refEq(
                    FavouriteViewModel.FavouriteScreenState.ShowBusStopFavouriteLine(expectedResult)
                )
            )
        }

    }

    @Test
    fun `deleteItem removes the favourite from db`() {

        val myFavourite = stopFavouriteMock.copy("23")
        val myBusStop = busStopsMock[0].copy(node = "23")

        localDataSource.busStopsFavourite = listOf(myFavourite)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            mViewModel.favouriteState.observeForever(observer)

            mViewModel.deleteItem(Pair(myBusStop.toUIBusStop(), myFavourite.toUIStopFavourite()))

            assertFalse(localDataSource.busStopsFavourite.contains(myFavourite))

        }

    }


}

