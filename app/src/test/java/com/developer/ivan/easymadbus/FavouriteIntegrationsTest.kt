package com.developer.ivan.easymadbus

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.repository.RemoteDataSource
import com.developer.ivan.easymadbus.di.DaggerEasyMadBusComponent
import com.developer.ivan.easymadbus.di.EasyMadBusComponent
import com.developer.ivan.easymadbus.framework.CoroutinesMainDispatcherRule
import com.developer.ivan.easymadbus.presentation.favourites.FavouriteFragmentModule
import com.developer.ivan.easymadbus.presentation.favourites.FavouriteViewModel
import com.developer.ivan.testshared.busStopsMock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Inject

@RunWith(MockitoJUnitRunner::class)
class FavouriteIntegrationsTest {


    @get:Rule
    var coroutinesTestRule = CoroutinesMainDispatcherRule()

    @get:Rule
    var liveDataRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<FavouriteViewModel.FavouriteScreenState>


    @Inject
    private lateinit var localDataSource: LocalDataSource

    @Inject
    private lateinit var remoteDataSource: RemoteDataSource

    private lateinit var component: EasyMadBusComponent

    private lateinit var mViewModel: FavouriteViewModel

    @Before
    fun onSetup() {
        component = DaggerEasyMadBusComponent
            .factory()
            .create()

        mViewModel = component.plus(FavouriteFragmentModule()).favouriteViewModel
        (remoteDataSource as FakeRemoteDataSource).busStops = busStopsMock
    }

    @Test
    fun `busStops are loaded from database`() {

        (localDataSource as FakeLocalDataSource).busStops = emptyList()

        coroutinesTestRule.testDispatcher.runBlockingTest {
            mViewModel.favouriteState.observeForever(observer)

            mViewModel.obtainInfo()

            verify(observer).onChanged(
                FavouriteViewModel.FavouriteScreenState.ShowBusStopFavouriteInfo(
                    emptyList()
                )
            )
        }

    }


}