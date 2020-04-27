package com.developer.ivan.easymadbus

import android.os.Bundle
import android.util.Log
import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.data.datasources.NetworkDataSource
import com.developer.ivan.data.datasources.RemoteDataSource
import com.developer.ivan.domain.*
import com.developer.ivan.easymadbus.framework.*
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker


class FakeNetworkDataSource(var connected: Boolean = true) : NetworkDataSource {
    override fun isConnected(): Boolean = connected

}

class FakePermissionChecker : PermissionChecker {
    var permissionGranted = true

    override fun check(permission: String): Boolean = permissionGranted

}

class FakeLocationDataSource : LocationDataSource {

    var location: Locate? = null

    override suspend fun findLastLocation(): Either<Failure, Locate> {
        return if (location == null)
            Either.Left(Failure.NullResult)
        else
            Either.Right(location!!)

    }

    override suspend fun findLocationUpdates(): Either<Failure, Locate> {
        return if (location == null)
            Either.Left(Failure.NullResult)
        else
            Either.Right(location!!)

    }

}

class FakeRemoteDataSource :
    RemoteDataSource {


    lateinit var token: Token

    var busStops: List<BusStop> = emptyList()
    var lines: List<Line> = emptyList()

    var arrives: List<Arrive> = emptyList()
    var incidents: List<Incident> = emptyList()

    override suspend fun getLogin(headers: Map<String, String>): Either<Failure, Token> =
        Either.Right(token)


    override suspend fun getBusStops(headers: Map<String, String>): Either<Failure, List<BusStop>> =
        Either.Right(busStops)

    override suspend fun getLines(
        headers: Map<String, String>,
        busStop: String
    ): Either<Failure, List<Line>> = Either.Right(lines)

    override suspend fun getArrives(
        busStop: String,
        headers: Map<String, String>,
        body: Map<String, String>
    ): Either<Failure, List<Arrive>> =
        Either.Right(arrives)

    override suspend fun getIncidents(headers: Map<String, String>): Either<Failure, List<Incident>> =
        Either.Right(incidents)

}

class FakeLocalDataSource : LocalDataSource {

    var token: Token? = null
    var busStops: List<BusStop> = emptyList()

    var lines: List<Line> = emptyList()

    var busStopsFavourite: List<StopFavourite> = emptyList()
    var incidents: List<Incident> = emptyList()

    override suspend fun getBusStops(): List<BusStop> = busStops

    override suspend fun getBusStopWithLines(busStop: String): BusStop? =
        busStops.find { it.node == busStop }

    override suspend fun getBusStopById(busStop: String): BusStop? =
        busStops.find { it.node == busStop }

    override suspend fun getCountBusStops(): Int =
        busStops.size

    override suspend fun getCountLines(busStop: String): Int =
        lines.size

    override suspend fun insertBusStops(busStops: List<BusStop>) {
        this.busStops = busStops
    }

    override suspend fun insertBusStopLines(busStop: String, lines: List<Line>) {
        busStops.map { if (it.node == busStop) it.copy(lines = lines) else it }
    }

    override suspend fun getToken(): Token? = token

    override suspend fun insertToken(token: Token) {
        this.token = token
    }

    override suspend fun getFavourites(id: Int?): List<StopFavourite> {
        return if (id != null)
            busStopsFavourite.filter { it.busStopId == id.toString() }
        else
            busStopsFavourite
    }


    override suspend fun getFavouritesAndBusStops(id: String?): List<Pair<BusStop, StopFavourite?>> {

        return if (id != null) {
            listOf(
                Pair(
                    busStops.find { it.node == id }!!.copy(lines = lines),
                    busStopsFavourite.find { it.busStopId == id })
            )
        } else {
            busStops.filter { it.node in busStopsFavourite.map { it.busStopId } }
                .map { it.copy(lines = lines) }
                .map { element ->
                    Pair(
                        element,
                        busStopsFavourite.find { element.node == it.busStopId })
                }
        }
    }

    override suspend fun updateFavourite(favourite: StopFavourite) {

        if (this.busStopsFavourite.find { it.busStopId == favourite.busStopId } != null) {
            this.busStopsFavourite =
                this.busStopsFavourite.filterNot { it.busStopId == favourite.busStopId } + favourite
        } else
            this.busStopsFavourite += favourite

    }

    override suspend fun deleteFavourite(favourite: StopFavourite) {
        this.busStopsFavourite -= favourite
    }

    override suspend fun insertIncidents(incidents: List<Incident>) {
        this.incidents = incidents
    }

    override suspend fun getIncidents(): List<Incident> = incidents

    override suspend fun getCountIncidents(): Int = this.incidents.size
}

class FakeMapManager : IMapManager {

    var mListenerMapReady: OnMapReady? = null
    var mListenerMapEvents: OnMapEvent? = null

    val mData: MutableList<UIBusStop> = mutableListOf()

    override fun setMapReadyListener(listener: OnMapReady) {
        mListenerMapReady = listener
    }

    override fun setMapEventsListener(listener: OnMapEvent) {
        mListenerMapEvents = listener
    }

    override fun moveToDefaultLocation() {
        Log.i("Map", "Move to default location " + Constants.EMTApi.MADRID_LOC.toString())
    }

    override fun moveToLocation(location: LatLng) {
        Log.i("Map", "Move to location $location")
    }

    override fun addPoints(items: List<UIBusStop>) {
        mData.addAll(items)
    }

    override fun findMarker(markerId: String): Marker? {
        return null
    }

    override fun onCreate(
        savedInstanceState: Bundle?,
        defaultPoints: List<UIBusStop>,
        mapConfiguration: MapManager.MapConfiguration
    ) {
        addPoints(defaultPoints)
    }

    override fun onMapReady(p0: GoogleMap?) {
        Log.i("Map", "Map ready! ")
    }
}
