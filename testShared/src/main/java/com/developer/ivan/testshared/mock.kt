package com.developer.ivan.testshared

import com.developer.ivan.domain.*
import java.util.concurrent.TimeUnit

val tokenMock = Token("1234", 9999, System.currentTimeMillis())
val stopFavouriteMock = StopFavourite("1234", "My stop favourite")

val arrivesMock = listOf(Arrive("001", "1", 100, 200, System.currentTimeMillis()),
    Arrive("002", "2", 200, 400, System.currentTimeMillis()))

val busStopsMock = listOf(
    BusStop(
        "1",
        Geometry("1", listOf(0.0, 0.0)),
        "Mock1",
        "true",
        listOf(Pair("001", listOf(arrivesMock[0])))
    ),
    BusStop(
        "2",
        Geometry("2", listOf(0.1, 0.1)),
        "Mock2",
        "false",
        listOf(Pair("002", listOf(arrivesMock[1])))
    )
)