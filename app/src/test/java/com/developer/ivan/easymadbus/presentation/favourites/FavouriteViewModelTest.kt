package com.developer.ivan.easymadbus.presentation.favourites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.developer.ivan.domain.Arrive
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.easymadbus.framework.CoroutinesMainDispatcherRule
import com.developer.ivan.easymadbus.presentation.models.toUIBusStop
import com.developer.ivan.easymadbus.presentation.models.toUIStopFavourite
import com.developer.ivan.testshared.arrivesMock
import com.developer.ivan.testshared.busStopsMock
import com.developer.ivan.testshared.stopFavouriteMock
import com.developer.ivan.usecases.DeleteStopFavourite
import com.developer.ivan.usecases.GetBusAndStopsFavourites
import com.developer.ivan.usecases.GetBusStopTime
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavouriteViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesMainDispatcherRule()

    @get:Rule
    var liveDataRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<FavouriteViewModel.FavouriteScreenState>


    @Mock
    private lateinit var stopTime: GetBusStopTime

    @Mock
    private lateinit var busAndStopsFavourites: GetBusAndStopsFavourites

    @Mock
    private lateinit var deleteStopFavourite: DeleteStopFavourite

    private lateinit var viewModel: FavouriteViewModel

    @Before
    fun setUp() {
        viewModel =
            FavouriteViewModel(
                stopTime,
                busAndStopsFavourites,
                deleteStopFavourite,
                coroutinesTestRule.testDispatcher
            )
    }

    @Test
    fun `obtainInfo observer obtains the user's favourite list`() {

        val busAndStops = listOf(Pair(busStopsMock[0], stopFavouriteMock))
        val busAndStopsUi = busAndStops.map {
            Pair(it.first.toUIBusStop(), it.second.toUIStopFavourite())
        }

        coroutinesTestRule.testDispatcher.runBlockingTest {

            viewModel.favouriteState.observeForever(observer)

            doReturn(Either.Right(busAndStops)).whenever(busAndStopsFavourites).execute(any())
//            To prevent error
            doReturn(Either.Right(mutableListOf<Deferred<Either<Failure, List<Arrive>>>>())).whenever(
                stopTime
            ).execute(any())

            viewModel.obtainInfo()

            verify(observer).onChanged(FavouriteViewModel.FavouriteScreenState.Loading)


            verify(observer).onChanged(
                refEq(
                    FavouriteViewModel.FavouriteScreenState.ShowBusStopFavouriteInfo(busAndStopsUi)
                )
            )
        }

    }

    @Test
    fun `obtainInfo obtains the line's times for each favourite`() {

        val busAndStops = listOf(Pair(busStopsMock[0], stopFavouriteMock))
        val listArrives =
            arrivesMock.mapIndexed { index, element -> element.copy(index.toString()) }

        coroutinesTestRule.testDispatcher.runBlockingTest {

            viewModel.favouriteState.observeForever(observer)

            doReturn(Either.Right(busAndStops)).whenever(busAndStopsFavourites).execute(any())

            doReturn(Either.Right(listArrives)).whenever(
                stopTime
            ).execute(any())

            viewModel.obtainInfo()


            verify(observer).onChanged(FavouriteViewModel.FavouriteScreenState.Loading)

            verify(stopTime, times(busAndStops.size)).execute(any())

        }

    }
}