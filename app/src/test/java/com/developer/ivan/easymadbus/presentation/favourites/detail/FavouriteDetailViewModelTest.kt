package com.developer.ivan.easymadbus.presentation.favourites.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.developer.ivan.domain.Either
import com.developer.ivan.easymadbus.framework.CoroutinesMainDispatcherRule
import com.developer.ivan.easymadbus.presentation.models.convertToBusArrives
import com.developer.ivan.easymadbus.presentation.models.toUIArrive
import com.developer.ivan.easymadbus.presentation.models.toUIBusStop
import com.developer.ivan.easymadbus.presentation.models.toUIStopFavourite
import com.developer.ivan.testshared.arrivesMock
import com.developer.ivan.testshared.busStopsMock
import com.developer.ivan.testshared.linesMock
import com.developer.ivan.testshared.stopFavouriteMock
import com.developer.ivan.usecases.GetBusStopTime
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.refEq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class FavouriteDetailViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesMainDispatcherRule()

    @get:Rule
    var liveDataRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getBusStopTime: GetBusStopTime

    @Mock
    lateinit var mObserver: Observer<FavouriteDetailViewModel.FavouriteDetailScreenState>

    private lateinit var mViewModel: FavouriteDetailViewModel

    private var busInfo = Pair(
        busStopsMock[0].apply { lines = linesMock }.toUIBusStop(),
        stopFavouriteMock.toUIStopFavourite()
    )

    @Before
    fun setUp() {
        mViewModel = FavouriteDetailViewModel(
            busInfo,
            getBusStopTime,
            coroutinesTestRule.testDispatcher

        )

        coroutinesTestRule.testDispatcher.runBlockingTest {

            mViewModel.favouriteDetailState.observeForever(mObserver)
            verify(mObserver).onChanged(refEq(FavouriteDetailViewModel.FavouriteDetailScreenState.ShowBusData(busInfo)))
        }

    }

    @Test
    fun `obtainLineInfo update the observer with timeline`() {


        val expectedResult = busInfo.first.copy(
            lines = convertToBusArrives(
                busStop = busInfo.first,
                arrives = arrivesMock.map { it.toUIArrive() })
        )

        coroutinesTestRule.testDispatcher.runBlockingTest {

            mViewModel.favouriteDetailState.observeForever(mObserver)

            whenever(getBusStopTime.execute(any())).thenReturn(Either.Right(arrivesMock))

            mViewModel.obtainLineInfo()

            verify(mObserver).onChanged(
                refEq(
                    FavouriteDetailViewModel.FavouriteDetailScreenState.ShowBusLine(
                        expectedResult
                    )
                )
            )

        }

    }



}