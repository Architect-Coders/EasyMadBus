package com.developer.ivan.testshared

import com.developer.ivan.domain.*
import java.util.concurrent.TimeUnit

val locateMock = Locate(0.0,0.0)
val tokenMock = Token("1234", 9999, System.currentTimeMillis())
val stopFavouriteMock = StopFavourite("1234", "My stop favourite")

val arrivesMock = listOf(Arrive("001", "1", 100, 200, System.currentTimeMillis()),
    Arrive("002", "2", 200, 400, System.currentTimeMillis()))

val linesMock = listOf(Line("001", "01", "A", "10", "3", "MyHome", "Work", listOf()),
    Line("002", "02", "B", "11", "2", "Work", "MyHome", listOf()))


val busStopsMock = listOf(
    BusStop(
        "1",
        Geometry("1", listOf(0.0, 0.0)),
        "Mock1",
        "true",
        listOf()
    ),
    BusStop(
        "2",
        Geometry("2", listOf(0.1, 0.1)),
        "Mock2",
        "false",
        listOf()
    )
)

val lambdaTokenMock : suspend (Token)->Unit = {token: Token -> Unit}