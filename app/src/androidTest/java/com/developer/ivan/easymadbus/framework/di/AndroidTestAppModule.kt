package com.developer.ivan.easymadbus.framework.di

import android.app.Application
import androidx.room.Room
import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.data.datasources.NetworkDataSource
import com.developer.ivan.data.datasources.RemoteDataSource
import com.developer.ivan.easymadbus.data.db.Database
import com.developer.ivan.easymadbus.data.server.ServerMapper
import com.developer.ivan.easymadbus.framework.*
import com.developer.ivan.easymadbus.framework.datasource.AndroidNetworkDataSource
import com.developer.ivan.easymadbus.framework.datasource.PlayServicesLocationDataSource
import com.developer.ivan.easymadbus.framework.datasource.RetrofitDataSource
import com.developer.ivan.easymadbus.framework.datasource.RoomDataSource
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AndroidTestAppModule
{

    @Provides
    @Singleton
    fun okHttpProvider() = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()


    @Provides
    @Singleton
    fun retrofitProvider(
        client: OkHttpClient,
        @Named("endpoint") endpoint: String
    ) =
        Retrofit.Builder().baseUrl(endpoint)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build().create(ApiService::class.java)

    @Provides
    @Singleton
    fun serverMapperProvider() = ServerMapper

    @Provides
    @Singleton
    fun databaseProvider(app: Application) =
        Room.databaseBuilder(app, Database::class.java, "db").allowMainThreadQueries().build()

    @Provides
    fun localDataSourceProvider(db: Database): LocalDataSource =
        RoomDataSource(db)

    @Provides
    fun remoteDataSourceProvider(apiService: ApiService,
                                 serverMapper: ServerMapper
    ): RemoteDataSource =
        RetrofitDataSource(apiService,serverMapper)

    @Provides
    fun locationDataSourceProvider(app: Application): LocationDataSource =
        PlayServicesLocationDataSource(app)

    @Provides
    fun networkDataSourceProvider(app: Application): NetworkDataSource =
        AndroidNetworkDataSource(app)



    @Provides
    fun permisionCheckerProvider(app: Application): PermissionChecker =
        AndroidPermissionChecker(app)

    @Singleton
    @Provides
    fun dispatcherProvides(): CoroutineDispatcher = Dispatchers.Main

    @Singleton
    @Provides
    fun mapManagerProvider(
        application: Application
    ): IMapManager = MapManager(application)

}