package com.developer.ivan.domain

import java.util.concurrent.TimeUnit


data class Token(val accessToken: String, val tokenSecExpiration: Int, val timeStamp: Long)
{
    fun isExpired() =
        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - timeStamp) >= tokenSecExpiration

}

data class BusStop(
    val node: String,
    val geometry: Geometry,
    val name: String,
    val wifi: String,
    val lines: List<Pair<String,List<Arrive>>>)


data class Line(
    val id: String,
    val label: String
)

data class Arrive(val line: String,
                  val stop: String,
                  val estimateArrive: Int,
                  val distanceBus:  Int,
                  val timeStamp: Long)

data class Geometry(val type: String, val coordinates: List<Double>)

data class Locate(val lat: Double, val lng: Double)
data class StopFavourite(val busStopId: String,
                         val name: String?
                         )

data class Incident(val title: String,
                    val description: String,
                    val link: String,
                    val rssAfectaDesde: String,
                    val rssAfectaHasta: String)

sealed class Failure
{
    object ConnectivityError: Failure()
    class JsonException(val body: String): Failure()

    class ServerError(val errorCode: String): Failure()
    class ServerException(val serverException: Throwable): Failure()
    object NullResult: Failure()

    abstract class FailureAbstract: Failure()
}
