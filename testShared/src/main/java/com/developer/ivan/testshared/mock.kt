package com.developer.ivan.testshared

import com.developer.ivan.domain.*

val locateMock = Locate(0.0, 0.0)
val tokenMock = Token("1234", 9999, System.currentTimeMillis())
val stopFavouriteMock = StopFavourite("1", "My stop favourite")

val arrivesMock = listOf(
    Arrive("001", "1", 100, 200, System.currentTimeMillis()),
    Arrive("002", "2", 200, 400, System.currentTimeMillis())
)

val linesMock = listOf(
    Line("001", "001", "A", "10", "3", "MyHome", "Work", listOf()),
    Line("002", "002", "B", "11", "2", "Work", "MyHome", listOf())
)

/*val incidentsMock = listOf(
    Incident("Linea 1 colapsada", "una descripción", "A", "21/04/2020", "22/04/2020"),
    Incident("Linea 2 colapsada", "otra descripción", "B", "21/05/2020", "21/05/2020")
)*/


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

val busStopsLinesArrivesMock = listOf(
    BusStop(
        "1",
        Geometry("1", listOf(0.0, 0.0)),
        "Mock1",
        "true",
        linesMock.map { line ->
            line.arrives = arrivesMock.filter { arrive -> line.label == arrive.line }
            line
        }
    )
)


