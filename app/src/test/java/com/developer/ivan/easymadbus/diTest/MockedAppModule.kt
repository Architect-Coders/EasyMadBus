package com.developer.ivan.easymadbus.diTest

import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.data.datasources.NetworkDataSource
import com.developer.ivan.data.datasources.RemoteDataSource
import com.developer.ivan.easymadbus.*
import com.developer.ivan.easymadbus.framework.IMapManager
import com.developer.ivan.easymadbus.framework.PermissionChecker
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class MockedAppModule {

    @Singleton
    @Provides
    fun localDataSourceProvider(): LocalDataSource =
        FakeLocalDataSource()

    @Singleton
    @Provides
    fun remoteDataSourceProvider(): RemoteDataSource =
        FakeRemoteDataSource()

    @Singleton
    @Provides
    fun locationDataSourceProvider(): LocationDataSource =
        FakeLocationDataSource()

    @Singleton
    @Provides
    fun networkDataSourceProvider(): NetworkDataSource =
        FakeNetworkDataSource()

    @Singleton
    @Provides
    fun permisionCheckerProvider(): PermissionChecker =
        FakePermissionChecker()

    @Singleton
    @Provides
    fun mapManagerProvider(): IMapManager = FakeMapManager()




    @Singleton
    @Provides
    fun dispatcherProvides(): CoroutineDispatcher = Dispatchers.Main



}